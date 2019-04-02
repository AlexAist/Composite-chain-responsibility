package by.epam.third.reader;

import by.epam.third.exception.BasicException;
import by.epam.third.validator.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ExcelReader {

    private static final Logger LOG = LogManager.getLogger();

    public XSSFSheet readExcelSheet(String path) {
        XSSFSheet sheet;
        try {
            if (!Validator.checkFile(path)) {
                throw new BasicException("File doesn't exist!");
            }
            File excel = new File(path);
            FileInputStream fis = new FileInputStream(excel);
            XSSFWorkbook book = new XSSFWorkbook(fis);
            sheet = book.getSheetAt(0);
        } catch (IOException | BasicException e) {
            LOG.error("read error", e);
            throw new RuntimeException();
        }
        return sheet;
    }
}
