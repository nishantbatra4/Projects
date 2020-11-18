import javafx.scene.paint.Color;

import java.util.ArrayList;

public class ItemContainer extends Containers
{
    ArrayList<Containers> list;
    public final static Color COLOR = Color.ORANGE;


    public ItemContainer(String name) {
        super(name);
        list = new ArrayList<>();
    }

    public Color getColor() {
        return COLOR;
    }

    public void addItem(Containers item) {
        list.add(item);
    }

    public void deleteItem(Containers item) {
        list.remove(item);
    }

    public ArrayList<Containers> getItems()
    {
        ArrayList<Containers> recursiveChildList = new ArrayList<>(list);
        for (Containers child : list) {
            if (child instanceof ItemContainer)
                recursiveChildList.addAll(((ItemContainer) child).getItems());
        }
        return recursiveChildList;
    }

    @Override
    public void accept(ContainerVisitor visitor) {
//        visitor.visit(this);
        ArrayList<Containers> l = getItems();
        for (int i=0; i < l.size(); i++){
            if (getItems().get(i) instanceof Item) {
                visitor.visit((Item) l.get(i));
            }
            else{
                visitor.visit((ItemContainer) l.get(i));
            }
        }
        visitor.visit(this);
    }
}
