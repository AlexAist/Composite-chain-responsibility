package by.epam.third.parser;

import by.epam.third.composite.CompositeImpl;
import by.epam.third.composite.LeafImpl;
import by.epam.third.exception.BasicException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class ExcelParser {

    private int maxHeight = 0;

    public void parse(XSSFSheet sheet, CompositeImpl parent, int rowIndex, int columnIndex, int maxWeight) throws BasicException {
        if (columnIndex > maxWeight && maxWeight != 0) {
            return;
        }
        Row row = sheet.getRow(rowIndex);
        Cell cell = row.getCell(columnIndex);
        if(columnIndex > row.getLastCellNum() - 1){
            return;
        }
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
            if (lastColumn == columnIndex) {
                parseLeaf(sheet, composite, columnIndex, lastRow + 1);
            } else {
                parse(sheet, composite, lastRow + 1, columnIndex, lastColumn);
            }
        } else {
            if (lastRow <= maxHeight) {
                CompositeImpl composite = new CompositeImpl(convertToString(cell));
                parent.add(composite);
                parseLeaf(sheet, composite, columnIndex, lastRow + 1);
            } else {
                parseLeaf(sheet, parent, columnIndex, rowIndex + 1);
            }
        }
        parse(sheet, parent, rowIndex, lastColumn + 1, maxWeight);
    }

    public void parseLeaf(XSSFSheet sheet, CompositeImpl composite, int columnInd, int rowInd) throws BasicException {
        for (int i = rowInd; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            Cell cell = row.getCell(columnInd);
            String color = convertToString(row.getCell(0));
            String tmp = convertToString(cell);
            LeafImpl leaf;
            try {
                leaf = new LeafImpl(color, Double.parseDouble(tmp));
                leaf.setExist(true);
            } catch (NumberFormatException e) {
                leaf = new LeafImpl(color);
                leaf.setExist(false);
            }
            composite.add(leaf);
        }
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
}

