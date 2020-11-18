import javafx.scene.control.TreeItem;

public class EditItemCommand extends Command {
    private Containers oldCont;
    private Containers newCont;
    private TreeItem<Containers> treeItem;

    public EditItemCommand(Containers oldItem, Containers newItem, TreeItem<Containers> treeItem) {
        this.oldCont = oldItem;
        this.newCont = newItem;
        this.treeItem = treeItem;
    }

    @Override
    public void execute() {
        treeItem.setValue(newCont);
        Dashboard.log("Attribute changed");
    }

    @Override
    public void unexecute() {
        treeItem.setValue(oldCont);
        System.out.println(treeItem.getValue().getPrice());
        Dashboard.log("Attribute change undone");
    }
}
