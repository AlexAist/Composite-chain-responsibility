package by.epam.third.writer;

import by.epam.third.composite.CompositeImpl;
import by.epam.third.composite.LeafImpl;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.TextAlign;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;

import static org.apache.poi.xssf.usermodel.TextAlign.CENTER;

public class ExcelWriter {

    public void write(String file, CompositeImpl composite, int rowIndex, int columnIndex) throws IOException {
        Workbook book = new XSSFWorkbook();
        Sheet sheet = book.createSheet("ExcelRecover");
        Row row;
        XSSFCellStyle style = (XSSFCellStyle) book.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        for (int i = 0; i < 100; i++) {
            row = sheet.createRow(i);
            for (int j = 0; j < 100; j++) {
                row.createCell(j).setCellStyle(style);
            }
        }
        writeLogic(sheet, composite, rowIndex, columnIndex);
        book.write(new FileOutputStream(file));
        book.close();
    }

    private void writeLogic(Sheet sheet, CompositeImpl composite, int columnIndex, int rowIndex){
        Row row;
        int height = hatHeight(composite) - 1;
        for (int i = 0; i < composite.getSize(); i++) {
            if(composite.getChild(i) instanceof CompositeImpl){
                int tempWeight = hatWeight((CompositeImpl) composite.getChild(i)) - 1;
                int tempHeight = hatHeight((CompositeImpl) composite.getChild(i)) - 1;
                int cellHeight = height - tempHeight;
                cellHeight -= 1;
                row = sheet.getRow(rowIndex);
                Cell cell = row.getCell(columnIndex);
                cell.setCellValue(((CompositeImpl) composite.getChild(i)).getInfo());
                if(cellHeight != 0 || tempWeight != 0){
                    CellRangeAddress address = new CellRangeAddress(rowIndex, rowIndex + cellHeight, columnIndex, columnIndex + tempWeight);
                    sheet.addMergedRegion(address);
                }
                writeLogic(sheet, (CompositeImpl) composite.getChild(i), columnIndex, rowIndex + cellHeight + 1);
                columnIndex += tempWeight + 1;
            }else{
                for (int j = 0; j < composite.getSize(); j++) {
                    row = sheet.getRow(rowIndex + j);
                    Cell cell = row.getCell(columnIndex);
                    if(((LeafImpl) composite.getChild(j)).isExist()) {
                        cell.setCellValue(((LeafImpl) composite.getChild(j)).getInfo());
                    }else {
                        cell.setCellValue(((LeafImpl) composite.getChild(j)).getColor());
                    }
                }
            }
        }
    }

    public void setCenter(XSSFSheet sheet){

    }

    private int hatHeight(CompositeImpl composite) {
        int counter = 0;
        for (int i = 0; i < composite.getSize(); i++) {
            if (composite.getChild(i) instanceof CompositeImpl) {
                int tmp = hatHeight((CompositeImpl) composite.getChild(i));
                if (tmp > counter) {
                    counter = tmp;
                }
            }
        }
        return ++counter;
    }

    private int hatWeight(CompositeImpl composite) {
        int counter = 0;
        if (composite.getSize() == 0) {
            return 1;
        }
        for (int i = 0; i < composite.getSize(); i++) {
            if (composite.getChild(i) instanceof CompositeImpl) {
                counter += hatWeight((CompositeImpl) composite.getChild(i));
            } else {
                return 1;
            }
        }
        return counter;
    }
}
