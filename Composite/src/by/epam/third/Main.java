package by.epam.third;


import by.epam.third.composite.CompositeImpl;
import by.epam.third.composite.LeafImpl;
import by.epam.third.exception.BasicException;
import by.epam.third.interpreter.InterpreterBitExp;
import by.epam.third.parser.ExcelParser;
import by.epam.third.reader.ExcelReader;
import by.epam.third.writer.ExcelWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.io.IOException;

public class Main {

    private static final Logger LOG = LogManager.getLogger();

    public static void main(String[] args) throws BasicException, IOException {

        ExcelReader reader = new ExcelReader();
        XSSFSheet sheet = reader.readExcelSheet(0, "D://CompositeNew.xlsx");
        ExcelParser parser = new ExcelParser();
        //CompositeImpl composite = parser.createComposite(sheet);//5| 1&2& 3| 4& 1 5|6&47 ^ |3 | ~89&4| 42&7 |1
        CompositeImpl composite = new CompositeImpl();
        parser.parse(sheet, composite, 0 ,0, 0);
        composite.operation();
        //composite.getChild(0).getChild(0).add(new LeafImpl("jopa"));
        ExcelWriter writer = new ExcelWriter();
        writer.write("D://CompositeNew111.xlsx", composite, 0, 0);
    }
}
