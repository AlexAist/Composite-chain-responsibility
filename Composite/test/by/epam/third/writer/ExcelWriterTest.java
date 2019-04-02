package by.epam.third.writer;

import by.epam.third.composite.CompositeImpl;
import by.epam.third.exception.BasicException;
import by.epam.third.parser.ExcelParser;
import by.epam.third.reader.ExcelReader;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class ExcelWriterTest {

    @Test
    public void testWrite() throws BasicException {
        ExcelReader reader = new ExcelReader();
        XSSFSheet sheet = reader.readExcelSheet("D://CompositeNew.xlsx");
        ExcelParser parser = new ExcelParser();
        CompositeImpl composite = new CompositeImpl();
        parser.parse(sheet, composite, 0 ,0, 0);
        composite.operation();
        ExcelWriter writer = new ExcelWriter();
        writer.write("D://CompositeNew111.xlsx", composite, 0, 0);
    }
}