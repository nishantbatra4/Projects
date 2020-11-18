import java.util.ArrayList;

public interface Subject {
    ArrayList<Item> observerList = new ArrayList<>();

//    int getState();
    void updateCount();

    void attach(Item o);

    void detach(Item o);

    void notifyObservers();
}
