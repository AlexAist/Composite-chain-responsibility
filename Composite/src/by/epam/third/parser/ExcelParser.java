package by.epam.third.parser;

import by.epam.third.composite.CompositeImpl;
import by.epam.third.composite.LeafImpl;
import by.epam.third.exception.BasicException;
import by.epam.third.validator.Validator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelParser {

    private static final Logger LOG = LogManager.getLogger();
    private int maxHeight = 0;

    public void parse(XSSFSheet sheet, CompositeImpl parent, int rowIndex, int columnIndex, int maxWeight) throws BasicException {
        if(columnIndex > maxWeight && maxWeight != 0){
            return;
        }
        Row row = sheet.getRow(rowIndex);
        Cell cell = row.getCell(columnIndex);
        CellRangeAddress region = findRegion(cell);
        int lastColumn = columnIndex;
        int lastRow = rowIndex;
        if (region != null) {
            lastColumn = region.getLastColumn();
            lastRow = region.getLastRow();
            if (lastRow > maxHeight) {
                maxHeight = lastRow;
            }
            CompositeImpl composite = new CompositeImpl(convertToString(cell));
            parent.add(composite);
            if(lastColumn == columnIndex){
                readLeaf(sheet, composite);
            }
            parse(sheet, composite, lastRow + 1, columnIndex, lastColumn);
        } else {
            if (lastRow <= maxHeight) {
                CompositeImpl composite = new CompositeImpl(convertToString(cell));
                parent.add(composite);
                parse(sheet, composite, lastRow + 1, columnIndex, lastColumn);
            } else {
                readLeaf(sheet, parent);
            }
        }
        System.out.println(parent);
    }


    private CompositeImpl parseSheet(XSSFSheet sheet, CellRangeAddress region, CompositeImpl parent) throws BasicException {
        int firstColumn = region.getFirstColumn();
        int lastColumn = region.getLastColumn();
        int lastRow = region.getLastRow();
        Row row = sheet.getRow(lastRow + 1);
        Iterator<Cell> cellIterator = row.cellIterator();
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            int tempColumn = cell.getAddress().getColumn();
            if (tempColumn >= firstColumn && tempColumn <= lastColumn) {
                if (!isMerged(cell)) {
                    if (cell.getCellType() == CellType.NUMERIC) {
                        String info = convertToString(cell);
                        for (int i = lastRow + 1; i <= sheet.getLastRowNum(); i++) {
                            LeafImpl leaf = readColumnLeafWithColor(sheet, i, cell.getColumnIndex());
                            if (!info.equals("0") && leaf != null) {
                                parent.add(leaf);
                            }
                        }
                    } else {
                        String unitType = convertToString(cell);
                        if (!unitType.equals("0")) {
                            CompositeImpl leaf = new CompositeImpl(unitType);
                            parent.add(leaf);
                        }
                    }
                } else {
                    String info = convertToString(cell);
                    if (!info.equals("0")) {
                        CompositeImpl leaf = new CompositeImpl(info);
                        parent.add(leaf);
                    }
                }
            }
        }
        return parent;
    }


    private boolean isMerged(Cell cell) {
        Sheet sheet = cell.getSheet();
        Row row = cell.getRow();
        for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
            CellRangeAddress region = sheet.getMergedRegion(i);
            if (region.isInRange(row.getRowNum(), cell.getColumnIndex())) {
                return true;
            }
        }
        return false;
    }

    private CellRangeAddress findRegion(Cell cell) {
        Sheet sheet = cell.getSheet();
        Row row = cell.getRow();
        for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
            CellRangeAddress region = sheet.getMergedRegion(i);
            if (region.isInRange(row.getRowNum(), cell.getColumnIndex())) {
                return region;
            }
        }
        return null;
    }

    private static Cell findCell(XSSFSheet sheet, String text) {
        for (Row row : sheet) {
            for (Cell cell : row) {
                if (text.equals(cell.getStringCellValue()))
                    return cell;
            }
        }
        return null;
    }

    private String convertToString(Cell cell) throws BasicException {
        String tempStr = null;
        CellType cellType = cell.getCellType();
        switch (cellType) {
            case BOOLEAN: {
                tempStr = String.valueOf(cell.getBooleanCellValue());
                break;
            }
            case NUMERIC: {
                tempStr = String.valueOf(cell.getNumericCellValue());
                break;
            }
            case STRING: {
                tempStr = cell.getStringCellValue();
                break;
            }
            case BLANK: {
                tempStr = "0";
                break;
            }
            case _NONE: {
                tempStr = "0";
                break;
            }
            case FORMULA: {
                break;
            }
            default: {
                throw new BasicException("Illegal argument");
            }
        }
        return tempStr;
    }

    private CompositeImpl readNode(XSSFSheet sheet, int rowIndex) throws BasicException {
        CompositeImpl parent = new CompositeImpl();
        if (Validator.checkRowIndex(rowIndex, sheet.getLastRowNum())) {
            Row row = sheet.getRow(rowIndex);
            Iterator<Cell> cellIterator = row.cellIterator();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                if (isMerged(cell)) {
                    String info = convertToString(cell);
                    if (!info.equals("0")) {
                        CompositeImpl child = new CompositeImpl(info);
                        parent.add(child);

                    }
                }
            }
        } else {
            LOG.log(Level.ERROR, "Invalid index");
        }
        return parent;
    }

    private LeafImpl readColumnLeafWithColor(XSSFSheet sheet, int rowIndex, int columnIndex) throws BasicException {
        Row row = sheet.getRow(rowIndex);
        Iterator<Cell> cellIterator = row.cellIterator();
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            if (cell.getColumnIndex() == columnIndex) {
                String color = convertToString(row.getCell(0));
                String tmp = convertToString(cell);
                //TODO
                //return new LeafImpl(color, Double.parseDouble(tmp));
                LeafImpl leaf;
                try {
                    leaf = new LeafImpl(color, Double.parseDouble(tmp));
                    leaf.setExist(true);
                }catch (NumberFormatException e){
                    leaf = new LeafImpl(color);
                    leaf.setExist(false);
                }
                return leaf;
            }
        }
        return null;
    }

    private void readLeaf(XSSFSheet sheet, CompositeImpl composite) throws BasicException {
        Cell cell = findCell(sheet, composite.getInfo());
        if (cell != null) {
            for (int k = cell.getRowIndex() + 1; k <= sheet.getLastRowNum(); k++) {
                composite.add(readColumnLeafWithColor(sheet, k, cell.getColumnIndex()));
            }
        }
    }

    private void notMergedNodes(XSSFSheet sheet, List<List<CompositeImpl>> nodes) throws BasicException {
        int[] masNumsOfColumn = {2, 4, 6, 12};
        int colNums = 4;
        for (int j = 0; j < colNums; j++) {
            for (int i = 0; i < 2; i++) {
                CompositeImpl leaf = (CompositeImpl) nodes.get(2).get(masNumsOfColumn[j]).getChild(i);
                readLeaf(sheet, leaf);
            }
        }
    }

    public CompositeImpl createComposite(XSSFSheet sheet) throws BasicException {
        List<List<CompositeImpl>> nodes = createListsForComposite(sheet);
        CompositeImpl main = new CompositeImpl();
        for (int i = 0; i < 3; i++) {
            main.add(nodes.get(0).get(i));
        }
        for (int i = 0; i < 3; i++) {
            CompositeImpl composite1 = nodes.get(2).get(i);
            CompositeImpl composite2 = nodes.get(2).get(i + 3);
            main.getChild(1).getChild(0).add(composite1);
            main.getChild(1).getChild(1).add(composite2);
            CompositeImpl composite3 = nodes.get(2).get(i + 6);
            CompositeImpl composite4 = nodes.get(2).get(i + 9);
            main.getChild(2).getChild(0).add(composite3);
            main.getChild(2).getChild(1).add(composite4);
        }
        CompositeImpl dragon = nodes.get(2).get(12);
        main.getChild(2).getChild(1).add(dragon);
        return main;
    }

    private List<List<CompositeImpl>> createListsForComposite(XSSFSheet sheet) throws BasicException {
        List<List<CompositeImpl>> nodes = new ArrayList<>();
        CompositeImpl composite;
        int stringsWithNodes = 3;
        for (int i = 0; i < stringsWithNodes; i++) {
            List<CompositeImpl> strCellList = new ArrayList<>();
            nodes.add(strCellList);
            composite = readNode(sheet, i);
            for (int j = 0; j < composite.getSize(); j++) {
                CompositeImpl tmp = (CompositeImpl) composite.getChild(j);
                Cell cell = findCell(sheet, tmp.getInfo());
                if (cell != null) {
                    CellRangeAddress region = findRegion(cell);
                    if (region != null) {
                        CompositeImpl leaf = parseSheet(sheet, region, tmp);
                        strCellList.add(leaf);
                    }
                }
            }
        }
        notMergedNodes(sheet, nodes);
        CompositeImpl leaf = (CompositeImpl) nodes.get(2).get(4).getChild(2);
        readLeaf(sheet, leaf);
        return nodes;
    }
}

