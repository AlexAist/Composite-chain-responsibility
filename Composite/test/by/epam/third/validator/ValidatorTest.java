package by.epam.third.validator;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class ValidatorTest {

    @Test
    public void testCheckFile() {
        assertFalse(Validator.checkFile("D://aaa.xlsx"));
    }

    @Test
    public void testCheckRowIndex() {
        assertFalse(Validator.checkRowIndex(7, 3));
    }

    @Test
    public void testCheckRowIndexTrue() {
        assertTrue(Validator.checkRowIndex(7, 10));
    }
}