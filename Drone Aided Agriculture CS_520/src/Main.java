import javafx.application.Application;

public class Main
{
    public static void main(String[] args)
    {
        Dashboard test = Dashboard.getInstance();

        Dashboard test2 = Dashboard.getInstance();

        if(test == test2)//test if both instances have reference to the same object
        {
            System.out.println("Singleton correct: Same object");
        }
        else
        {
            System.out.println("Singleton incorrect: different object");
        }

        Application.launch(test.getClass(), args);
    }
}
