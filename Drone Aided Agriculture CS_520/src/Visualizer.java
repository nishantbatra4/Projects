import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Visualizer {

    private List<Containers> containersList;

    private BorderPane borderPane = new BorderPane();

    // loads an array
    public Visualizer(List<Containers> containersList) {
        this.containersList = containersList;
    }

    // add dummies
    public Visualizer() {
        this.containersList = new ArrayList<>();
        getDummyItems();
    }

    public static void main(String[] args) {

        Visualizer v = new Visualizer();
        v.visualize();

    }

    private void getDummyItems() {
        Item item = new Item("Dummy 1");
        item.setLocationX(100f);
        item.setLocationY(100f);
        item.setWidth(100f);
        item.setLength(100f);
        containersList.add(item);
        ItemContainer ic = new ItemContainer("Dummy 2");
        ic.setLocationX(200f);
        ic.setLocationY(250f);
        ic.setWidth(100f);
        ic.setLength(200f);
        containersList.add(ic);
    }

    public void visualize() {
        Stage stage = new Stage();
        stage.setTitle("Visualizer");
        Group group = new Group();
        group.prefHeight(500);
        group.prefWidth(500);
        Rectangle bg = new Rectangle(500, 500, Color.WHITE);
        bg.setStroke(Color.BLACK);
        group.getChildren().add(bg);

        for (Containers item : containersList) {
            Rectangle rect = new Rectangle(item.locationX, item.locationY,
                    item.width, item.length);
            rect.setFill(item.getColor());
            Text name = new Text(item.locationX, item.locationY, item.name);
            group.getChildren().add(rect);
            group.getChildren().add(name);
        }

        Group legendG = addLegend();
        borderPane.setTop(legendG);
        borderPane.setCenter(group);
        Scene scene = new Scene(borderPane, 550, 600);
        stage.setScene(scene);
        stage.show();
    }

    public Group addLegend() {
        Group group = new Group();
        Rectangle legendWarp = new Rectangle(2, 2, 125, 52);
        legendWarp.setStroke(Color.BLACK);
        legendWarp.setFill(Color.WHITE);
        group.getChildren().add(legendWarp);
        Rectangle itemR = new Rectangle(5, 5, 20, 20);
        itemR.setFill(Item.COLOR);
        group.getChildren().add(itemR);
        Text tItem = new Text(30, 20, Item.class.getName());
        group.getChildren().add(tItem);
        Text tItemContainer = new Text(30, 45, ItemContainer.class.getName());
        group.getChildren().add(tItemContainer);

        Rectangle icR = new Rectangle(5, 30, 20, 20);
        icR.setFill(ItemContainer.COLOR);
        group.getChildren().add(icR);
        return group;
    }
}
