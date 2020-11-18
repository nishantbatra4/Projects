import javafx.scene.paint.Color;

public abstract class Containers implements Cloneable, Element {

    protected String name;
    protected double price;
    public final static Color COLOR = Color.BLACK;
    protected double locationX = 5;
    protected double locationY = 5;
    protected double length = 10;
    protected double width = 10;

    public Containers(String name) {
        this.name = name;
    }

    public Color getColor() {
        return COLOR;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getLocationX() {
        return locationX;
    }

    public void setLocationX(double locationX) {
        this.locationX = locationX;
    }

    public double getLocationY() {
        return locationY;
    }

    public void setLocationY(double locationY) {
        this.locationY = locationY;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public abstract void addItem(Containers item);

    public abstract void deleteItem(Containers item);

    public abstract void accept(ContainerVisitor visitor);

    // makes sure the container name is displayed in the tree hierarchy
    public String toString() {
        return this.getName();
    }

    // making object cloneable, for saving the current state into history list.
    public Containers clone() {
        try {
            return (Containers) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;

    }

}
