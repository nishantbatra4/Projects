import javafx.scene.control.TreeItem;

public class AddItemCommand extends Command {
    private TreeItem<Containers> parent;
    private TreeItem<Containers> node;
    private Containers container;

    public AddItemCommand(Containers container, TreeItem<Containers> parent, TreeItem<Containers> node) {
        this.container = container;
        this.parent = parent;
        this.node = node;
    }

    @Override
    public void execute() {
        //child = Dashboard.makeBranch(container, parent);
        parent.getChildren().add(node);
        Dashboard.rootItem.addItem(container);
        if (parent.getValue() != Dashboard.rootItem)
            parent.getValue().addItem(container);

        if (container instanceof Item) {
            Dashboard.log("Item added");
            Dashboard.getInstance().attach((Item)container);
            Dashboard.getInstance().updateCount();
        }
        if (container instanceof ItemContainer) {
            Dashboard.log("Building added");
        }

    }

    @Override
    public void unexecute() {
        new DelItemCommand(container, parent, node).execute();
    }
}
