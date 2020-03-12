import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class UserView {
    Scanner input;
    NumberFormat defaultFormat = NumberFormat.getCurrencyInstance();

    public void displayUserDetails(String name, BigDecimal balance){
        System.out.println("Name: " + name);
        System.out.println("Balance: " + defaultFormat.format(balance));
    }

    public void displayEntryDetails(Entry.Type type, String categoryName, BigDecimal amount, Date timeStamp){
        System.out.println("Type: " + type.toString().substring(0, 1) + type.toString().substring(1).toLowerCase());
        System.out.println("Category: " + categoryName);
        System.out.println("Amount: " + defaultFormat.format(amount));
        System.out.println("TimeStamp: " + timeStamp.toString());
    }

    public void displayGoalDetails(String goalName, BigDecimal amount){
        System.out.println("Name: " + goalName);
        System.out.println("Amount: " + defaultFormat.format(amount));
    }

    public void displayMessage(String message){
        System.out.println(message);
    }

    public String promptStringInput(String message){
        System.out.println(message);
        input = new Scanner(System.in);
        return input.next();
    }

    public BigDecimal promptBalanceInput(String message){
        System.out.println(message);
        input = new Scanner(System.in);
        while (!input.hasNextBigDecimal()){
            System.out.println("Please enter a numerical value.");
            input.next();
        }
        return input.nextBigDecimal();
    }

    public int promptOptionInput(String[] options){
        System.out.println("Please select an option.");
        for(int i = 0; i < options.length; i++){
            System.out.println(options[i]);
        }
        input = new Scanner(System.in);
        return input.nextInt();
    }

    public int promptSelectCategory(ArrayList<Category> userCategories, boolean remove) {
        System.out.println("Please select an Category.");
        int count = 1;
        for(Category category : userCategories){
            System.out.println(count + ") " + category.getCategoryName());
            count++;
        }
        if (!remove){
            System.out.println("Or select " + count + " to add a new category.");
        }
        input = new Scanner(System.in);
        return input.nextInt();
    }

    public int promptSelectEntry(ArrayList<Entry> userEntries) {
        System.out.println("Please select an Entry.");
        int count = 1;
        for(Entry entry : userEntries){
            System.out.println(count + ") ");
            displayEntryDetails(entry.getType(), entry.getTransactionCategory().getCategoryName(), entry.getAmount(), entry.getTimeStamp());
            count++;
        }
        input = new Scanner(System.in);
        return input.nextInt();
    }

    public int promptSelectGoal(ArrayList<Goal> userGoals){
        System.out.println("Please select a Goal.");
        int count = 1;
        for (Goal goal : userGoals){
            System.out.println(count + ") " + goal.getGoalName());
            count++;
        }
        input = new Scanner(System.in);
        return input.nextInt();
    }
}
