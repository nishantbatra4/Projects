import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;


public class Dashboard extends Application implements Subject {
    private static Dashboard instance = null;

    private static ListView<String> messages; // used to display status
    private static Button redoIt;
    private static Button undoIt;
    static ItemContainer rootItem = new ItemContainer("root"); // root item container
    private Stage dashboard;
    private TreeView<Containers> tree; // used for the tree hierarchy

    private int count = 0; // for the observer pattern
    private Label vMachines;
    private Label vWorkLoad;
    private Label vCurrPrice;
    private boolean clicked;

    public Dashboard() {
        instance = this;
    }

    public static Dashboard getInstance() {
        if (instance == null) {
            instance = new Dashboard();
        }

        return instance;
    }

    static void updateUndoRedoButton() {
        undoIt.setDisable(!Invoker.getInstance().canUndo());
        redoIt.setDisable(!Invoker.getInstance().canRedo());
    }

    static void log(String message) {
        ObservableList<String> mList = messages.getItems();
        mList.add(message);
        messages.scrollTo(mList.size() - 1);
    }

    @Override
    public void start(Stage primaryStage) {

        dashboard = primaryStage;
        dashboard.setTitle("Drone Aided Agriculture");

        // set BorderPane for the general layout of the dashboard
        BorderPane dashPane = new BorderPane();
        dashPane.setStyle("-fx-background-color: #d3d3d3");


        // set the top
        HBox top = new HBox();
        top.setStyle("-fx-padding: 10;" + "-fx-border-style: solid inside;"
                + "-fx-border-width: 2;" + "-fx-border-insets: 5;"
                + "-fx-border-radius: 5;" + "-fx-border-color: black;");
        InnerShadow is = new InnerShadow();
        is.setOffsetX(2.0f);
        is.setOffsetY(2.0f);
        Text pageTitle = new Text();
        pageTitle.setEffect(is);
        pageTitle.setText("Drone Aided Agriculture");
        pageTitle.setTextAlignment(TextAlignment.CENTER);
        pageTitle.setFill(Color.GREEN);
        pageTitle.setFont(Font.font("Arial", FontWeight.BOLD, 48));
        top.getChildren().add(pageTitle);
        top.setAlignment(Pos.CENTER);

        // set the file structure and message structure
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setAlignment(Pos.CENTER);

        // file structure
        VBox fileStruct = new VBox();
        fileStruct.setStyle("-fx-padding: 10;" + "-fx-border-style: solid inside;"
                + "-fx-border-width: 2;" + "-fx-border-insets: 5;"
                + "-fx-border-radius: 5;" + "-fx-border-color: black;");
        fileStruct.setPrefSize(400, 400);
        GridPane.setConstraints(fileStruct, 0, 0);

        // add tree hierarchy
        TreeItem<Containers> root = new TreeItem<>(rootItem);
        root.setExpanded(true);
        tree = new TreeView<>();
        tree.setRoot(root);

        // add the context menu and options
        ContextMenu menu = new ContextMenu();
        MenuItem edit = new MenuItem("Edit item");
        MenuItem addItem = new MenuItem("Add item");
        MenuItem addBuilding = new MenuItem("Add building");
        MenuItem removeContainers = new MenuItem("Remove Item(s)");

        menu.getItems().addAll(addBuilding, addItem, edit, removeContainers);

        tree.setContextMenu(menu); // shows Menu item when you click on it.

        tree.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
            //TODO: add what to do when item selected
            if (newValue.getValue() instanceof Item) {
                // can't add a new ItemContainer, but give options to edit
                addBuilding.setDisable(true);
                addItem.setDisable(true);

                // TODO: add actions
            } else {
                // add a new ItemContainer or item or edit current one
                addBuilding.setDisable(false);
                addItem.setDisable(false);

                // TODO: add actions
            }
            removeContainers.setDisable(false);

            addBuilding.setOnAction(new AttributeChangeHandler(newValue,
                    AttributeChangeHandler.ACTION.NEW_BUILDING));

            addItem.setOnAction(new AttributeChangeHandler(newValue,
                    AttributeChangeHandler.ACTION.NEW_ITEM));


            removeContainers.setOnAction(e -> {
                Invoker.getInstance().execute(
                        new DelItemCommand(newValue.getValue(), newValue.getParent(), newValue));
                if (newValue.getValue() instanceof Item){
                    Item o = (Item) newValue.getValue();
                    detach(o);
                    updateCount();
                }
            });

            // Change any attribute
            edit.setOnAction(new AttributeChangeHandler(newValue,
                    AttributeChangeHandler.ACTION.EDIT));

            // code for the visitor pattern when Tree item is clicked
            if (newValue.getValue().getName()!="root" && clicked) {
                ContainerVisitor visitor = new ContainerVisitor();
                newValue.getValue().accept(visitor);
                double curr = visitor.getCurrPrice();
                System.out.println(curr);
                double market = visitor.getMarketValue();
                System.out.println(market);
                vCurrPrice.setText(curr + "");
                vWorkLoad.setText(market + "");
            }
        });

        // Event handler for clicking of the mouse
        tree.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.isPrimaryButtonDown()) {
                    clicked = true;
                }
            }
        });
        fileStruct.getChildren().add(tree);


        // message structure
        VBox messageStruct = new VBox();
        messageStruct.setStyle("-fx-padding: 10;" + "-fx-border-style: solid inside;"
                + "-fx-border-width: 2;" + "-fx-border-insets: 5;"
                + "-fx-border-radius: 5;" + "-fx-border-color: black;");
        messageStruct.setPrefSize(400, 400);
        GridPane.setConstraints(messageStruct, 1, 0);
        messages = new ListView<>();
        messageStruct.getChildren().add(messages);

        // buttons
        HBox commands = new HBox();
        redoIt = new Button("redo");
        redoIt.setDisable(true);
        undoIt = new Button("undo");
        undoIt.setDisable(true);
        Button visualize = new Button("visualize");
        visualize.setOnAction(event -> {
            Visualizer v = new Visualizer(rootItem.getItems());
            v.visualize();
        });
        undoIt.setOnAction(event -> {
            Invoker.getInstance().undo();
        });
        redoIt.setOnAction(event -> {
            Invoker.getInstance().redo();
        });
        commands.getChildren().addAll(redoIt, undoIt, visualize);
        GridPane.setConstraints(commands, 1, 1);

        Button update = new Button("Update All");
        update.setOnAction(e -> notifyObservers()); //Observer Pattern button
        GridPane.setConstraints(update, 1, 2);

        GridPane infoPane = new GridPane();
        VBox info = new VBox();
        Label workLoad = new Label("Current market value: ");
        Label currPrice = new Label("Current price: ");
        Label machines = new Label("Total no. of Items: ");
        info.getChildren().addAll(workLoad, currPrice, machines);

        // Displays results from Visitor and Observer patterns
        VBox value = new VBox();
        vWorkLoad = new Label("0");
        vCurrPrice = new Label("0");
        vMachines = new Label(count + "");
        value.getChildren().addAll(vWorkLoad, vCurrPrice, vMachines);

        infoPane.addColumn(0, info);
        infoPane.addColumn(1, value);
        GridPane.setConstraints(infoPane, 0, 1);


        grid.getChildren().addAll(fileStruct, messageStruct, commands, update, infoPane);


        // add components to the scene and display
        dashPane.setTop(top);
        dashPane.setCenter(grid);
        Scene scene = new Scene(dashPane, 1000, 700);
        dashboard.setScene(scene);
        dashboard.show();
    }

    // Observer pattern methods
    @Override
    public void attach(Item o) {
        observerList.add(o);
    }

    @Override
    public void detach(Item o) {
        observerList.remove(o);
    }

    @Override
    public void notifyObservers() {
        for (Observer o : observerList)
            o.update(count);
    }

    @Override
    public void updateCount(){
        count = observerList.size();
        vMachines.setText(count + "");
    }
}

