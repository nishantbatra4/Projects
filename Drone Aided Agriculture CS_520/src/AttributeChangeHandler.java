import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.Optional;

public class AttributeChangeHandler implements EventHandler<ActionEvent> {

    private TreeItem<Containers> newValue;
    private ACTION a;
    private Containers old;

    AttributeChangeHandler(TreeItem<Containers> newValue, ACTION a) {
        this.newValue = newValue;
        this.a = a;
        old = newValue.getValue().clone();
    }

    @Override
    public void handle(ActionEvent event) {
        TreeItem.TreeModificationEvent<Containers> tEvent = new TreeItem.TreeModificationEvent<>(TreeItem.valueChangedEvent(), newValue);

        Dialog<Containers> dialog = new Dialog<>();
        GridPane pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        pane.setHgap(5);
        pane.setVgap(5);
        pane.setPadding(new Insets(25, 25, 25, 25));

        switch (a) {
            case EDIT:
                dialog.setHeaderText("Change Attribute...");
                dialog.setContentText("Please enter a new set of attribute:");
                break;
            case NEW_ITEM:
                dialog.setHeaderText("Create Item...");
                dialog.setContentText("Please enter a set of attribute for the item:");
                break;
            case NEW_BUILDING:
                dialog.setHeaderText("Create Building...");
                dialog.setContentText("Please enter a set of attribute for the building:");
        }

        Label lName = new Label("Name:");
        Label lPrice = new Label("Price:");
        Label lX = new Label("X-location:");
        Label lY = new Label("Y-location:");
        Label lW = new Label("Width:");
        Label lL = new Label("Length:");
        TextField tfName = new TextField(newValue.getValue().getName());
        TextField tfPrice = new TextField(newValue.getValue().getPrice() + "");
        TextField tfX = new TextField(newValue.getValue().getLocationX() + "");
        TextField tfY = new TextField(newValue.getValue().getLocationY() + "");
        TextField tfW = new TextField(newValue.getValue().getWidth() + "");
        TextField tfL = new TextField(newValue.getValue().getLength() + "");
        Label lM = new Label("Market Value:");
        TextField tfM = new TextField("0");


        pane.add(lName, 0, 0);
        pane.add(tfName, 1, 0);
        pane.add(lPrice, 0, 1);
        pane.add(tfPrice, 1, 1);
        pane.add(lX, 0, 2);
        pane.add(tfX, 1, 2);
        pane.add(lY, 0, 3);
        pane.add(tfY, 1, 3);
        pane.add(lW, 0, 4);
        pane.add(tfW, 1, 4);
        pane.add(lL, 0, 5);
        pane.add(tfL, 1, 5);


        if (a != ACTION.NEW_BUILDING){
            if (newValue.getValue() instanceof Item){
                tfM.setText(((Item) newValue.getValue()).getMarketVal() + "");
            }
            pane.add(lM, 0, 6);
            pane.add(tfM, 1, 6);
        }

        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialogPane.setContent(pane);

        dialog.setResultConverter((ButtonType button) -> {
            if (button == ButtonType.OK) {
                try {
                    Containers item = null;
                    switch (a) {
                        case EDIT:
                            item = newValue.getValue();
                            if (item instanceof Item){
                                ((Item) item).setMarketVal(Double.parseDouble(tfM.getText()));
                            }
                            break;
                        case NEW_ITEM:
                            item = new Item("item");
                            ((Item) item).setMarketVal(Double.parseDouble(tfM.getText()));
                            break;
                        case NEW_BUILDING:
                            item = new ItemContainer("building");
                    }
                    item.setName(tfName.getText());
                    item.setPrice(Double.parseDouble(tfPrice.getText()));
                    item.setLocationX(Double.parseDouble(tfX.getText()));
                    item.setLocationY(Double.parseDouble(tfY.getText()));
                    item.setWidth(Double.parseDouble(tfW.getText()));
                    item.setLength(Double.parseDouble(tfL.getText()));
                    return item;
                } catch (NumberFormatException e1) {
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setTitle(e1.toString());
                    a.setHeaderText("An error occurred...");
                    a.setContentText("Please input the correct data! Unsaved changes will be lost.");
                    a.showAndWait();
                }
            }
            return null;
        });
        Optional<Containers> result = dialog.showAndWait();
        result.ifPresent(i -> {

            switch (a) {
                case EDIT:
                    EditItemCommand edit = new EditItemCommand(old, i, newValue);
                    Invoker.getInstance().execute(edit);
                    break;

                case NEW_ITEM:
                case NEW_BUILDING:
                    TreeItem<Containers> newNode = new TreeItem<>(i);
                    newNode.setExpanded(true);
                    AddItemCommand add = new AddItemCommand(i, newValue, newNode);
                    Invoker.getInstance().execute(add);

            }
            Event.fireEvent(newValue, tEvent);


        });
    }

    public enum ACTION {EDIT, NEW_ITEM, NEW_BUILDING}
}
