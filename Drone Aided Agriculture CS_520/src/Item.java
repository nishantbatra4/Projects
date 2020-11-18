import javafx.scene.paint.Color;

public class Item extends Containers implements Observer
{

    public final static Color COLOR = Color.DARKBLUE;
    private int count;
    private double marketVal = 0;

    public Item (String name) {
        super(name);
    }

    public Color getColor() {
        return COLOR;
    }

    public void setMarketVal(double val){
        marketVal = val;
    }

    public double getMarketVal(){
        return marketVal;
    }

    @Override
    @Deprecated
    public void addItem(Containers item) {
        throw new UnsupportedOperationException("This is an Item!");
    }

    @Override
    @Deprecated
    public void deleteItem(Containers item) {
        throw new UnsupportedOperationException("This is an Item!");
    }

    @Override
    public void update(int count) {
        this.count = count;
        System.out.println(getName() + " has count " + this.count);
    }

    @Override
    public void accept(ContainerVisitor visitor) {
        visitor.visit(this);
    }
}
