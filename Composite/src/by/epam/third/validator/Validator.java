package by.epam.third.validator;

import java.io.File;

public class Validator {

    public static boolean checkFile(final String fileName) {
        final File file = new File(fileName);
        return file.exists() && file.canRead();
    }

    public static boolean checkRowIndex(int index, int lastRowIndex){
        return index >= 0 && index <= lastRowIndex;
    }
}
