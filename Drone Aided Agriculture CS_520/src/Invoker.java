import java.util.Stack;

public class Invoker {
    private static Invoker instance;
    private Stack<Command> undoList = new Stack<>();
    private Stack<Command> redoList = new Stack<>();

    private Invoker() {

    }

    public static Invoker getInstance() {
        if (instance == null)
            instance = new Invoker();
        return instance;
    }

    public boolean canUndo() {
        return !undoList.empty();
    }

    public boolean canRedo() {
        return !redoList.empty();
    }

    public void execute(Command c) {
        c.execute();
        undoList.push(c);
        Dashboard.updateUndoRedoButton();
        System.out.println("exec: undosize" + undoList.size() + "redosize" + redoList.size());
    }

    public void record(Command c) {
        undoList.push(c);
        Dashboard.updateUndoRedoButton();
        System.out.println("record: undosize" + undoList.size() + "redosize" + redoList.size());
    }

    public void undo() {
        if (!undoList.empty()) {
            Command c = undoList.pop();
            c.unexecute();
            redoList.push(c);
            Dashboard.updateUndoRedoButton();
            System.out.println("undo: undosize" + undoList.size() + "redosize" + redoList.size());
        } else {
            Dashboard.log("Undo List is Empty!");
        }

    }

    public void redo() {
        if (!redoList.empty()) {
            Command c = redoList.pop();
            c.execute();
            undoList.push(c);
            Dashboard.getInstance().updateUndoRedoButton();
            System.out.println("redo: undosize" + undoList.size() + "redosize" + redoList.size());
        } else {
            Dashboard.getInstance().log("Redo List is Empty!");
        }
    }


}
