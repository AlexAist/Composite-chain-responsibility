package by.epam.third.composite;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class CompositeImpl implements Component {

    private static final Logger LOG = LogManager.getLogger();
    private ArrayList<Component> components = new ArrayList<>();
    private String info;
    private int size = 0;
    private final int ADD_UNIT = 15;

    public CompositeImpl() {
    }

    public CompositeImpl(String info) {
        this.info = info;
    }

    @Override
    public void operation() {
        for (int i = 0; i < components.size(); i++) {
            if (components.get(i) instanceof CompositeImpl) {
                components.get(i).operation();
            } else {
                for (int j = 0; j < components.size(); j++) {
                    LeafImpl leaf = (LeafImpl) components.get(j);
                    leaf.setInfo(leaf.getInfo() + ADD_UNIT);
                }
                break;
            }
        }
    }

    public ArrayList<Component> getComponents() {
        return components;
    }

    public void setComponents(ArrayList<Component> components) {
        this.components = components;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getSize() {
        return size;
    }

    @Override
    public void add(Component c) {
        if (c != null) {
            size++;
            components.add(c);
        } else {
            LOG.log(Level.ERROR, "component is null!");
        }
    }

    @Override
    public void remove(Component c) {
        if (c != null) {
            components.remove(c);
            size--;
        } else {
            LOG.log(Level.ERROR, "component is null!");
        }
    }

    @Override
    public Component getChild(int index) {
        Component obj = null;
        if (index >= 0) {
            obj = components.get(index);
        } else {
            LOG.log(Level.ERROR, "Invalid index!");
        }
        return obj;
    }

    @Override
    public String toString() {
        return "CompositeImpl{" +
                "name=" + info +
                ", components='" + components +
                '}' + "\t";
    }
}
