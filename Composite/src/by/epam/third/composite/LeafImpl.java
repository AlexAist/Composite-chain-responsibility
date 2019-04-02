package by.epam.third.composite;

import by.epam.third.exception.OperationException;

public class LeafImpl implements Component {

    private String color;
    private double info;
    private boolean isExist;

    public LeafImpl() {
    }

    public LeafImpl(String color) {
        this.color = color;
    }

    public LeafImpl(String color, double info) {
        this.color = color;
        this.info = info;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public double getInfo() {
        return info;
    }

    public void setInfo(double info) {
        this.info = info;
    }

    public boolean isExist() {
        return isExist;
    }

    public void setExist(boolean isExist) {
        this.isExist = isExist;
    }

    @Override
    public void operation() throws OperationException {
            throw new OperationException("Operation is not valid for leaf");
    }

    @Override
    public void add(Component c) throws OperationException {
            throw new OperationException("Add is not valid for leaf");
    }

    @Override
    public void remove(Component c) throws OperationException {
            throw new OperationException("remove is not valid for leaf");
    }

    @Override
    public Component getChild(int index) throws OperationException {
        throw new OperationException("getChild is not valid for leaf");
    }

    @Override
    public String toString() {
        return "LeafImpl{" +
                "color='" + color + '\'' +
                ", info=" + info +
                '}';
    }
}
