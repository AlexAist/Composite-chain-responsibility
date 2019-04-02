package by.epam.third.interpreter;

import by.epam.third.exception.BasicException;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class InterpreterBitExpTest {

    @Test
    public void testConvertToPolishNotation() {
        InterpreterBitExp bitExp = new InterpreterBitExp();
        String expression = "(7^5|1&2<<(2|5>>2&71))|1200";
        String expected = "7 5 ^ 1 2 2 5 2 >> 71 & | << & | 1200 |";
        String actual = bitExp.convertToPolishNotation(expression);
        assertEquals(actual, expected);
    }

    @Test
    public void testResult() throws BasicException {
        InterpreterBitExp bitExp = new InterpreterBitExp();
        String expression = "(7^5|1&2<<(2|5>>2&71))|1200";
        String polish = bitExp.convertToPolishNotation(expression);
        String result = bitExp.result(polish);
        String expected = "1202";
        assertEquals(result, expected);
    }
}