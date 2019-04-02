package by.epam.third.composite;

public class LeafImpl implements Component {

    private String color;
    //private String unitType;
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

/*    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }*/

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
    public void operation() {

    }

    @Override
    public void add(Component c) {
        throw new UnsupportedOperationException("Add is not valid for leaf");
    }

    @Override
    public void remove(Component c) {
        throw new UnsupportedOperationException("remove is not valid for leaf");
    }

    @Override
    public Component getChild(int index) {
        throw new UnsupportedOperationException("getChild is not valid for leaf");
    }

    @Override
    public String toString() {
        return "LeafImpl{" +
                "color='" + color + '\'' +
                ", info=" + info +
                '}';
    }
}
