package by.epam.third.composite;

import by.epam.third.exception.OperationException;

public interface Component {
    void operation() throws OperationException;
    void add(Component c) throws OperationException;
    void remove(Component c) throws OperationException;
    Component getChild (int index) throws OperationException;
}
