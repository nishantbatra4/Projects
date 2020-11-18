import javafx.event.Event;
import javafx.scene.control.TreeItem;

public class DelItemCommand extends Command {
    private TreeItem<Containers> node;
    private TreeItem<Containers> parent;
    private Containers container;

    public DelItemCommand(Containers container, TreeItem<Containers> parent, TreeItem<Containers> node) {
        this.container = container;
        this.parent = parent;
        this.node = node;
    }

    @Override
    public void execute() {
        if (!node.getChildren().isEmpty()) {
            Dashboard.log("Please empty the Container first!");
            return;
        }
        parent = node.getParent();

        TreeItem.TreeModificationEvent<Containers> event =
                new TreeItem.TreeModificationEvent<>(
                        TreeItem.childrenModificationEvent(), parent);
        parent.getValue().deleteItem(container);
        parent.getChildren().remove(node);
        Dashboard.rootItem.deleteItem(container);
        Event.fireEvent(parent, event);
        Dashboard.log("Item removed");
        if (container instanceof Item){
            Dashboard.getInstance().detach((Item)container);
            Dashboard.getInstance().updateCount();
        }
    }


    @Override
    public void unexecute() {
        new AddItemCommand(container, parent, node).execute();
    }
}
