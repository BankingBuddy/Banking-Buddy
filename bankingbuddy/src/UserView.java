import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class UserView {
    Scanner input;

    public void displayUserDetails(String name, BigDecimal balance){
        System.out.println("Name: " + name);
        System.out.println("Balance: " + balance);
    }

    public void displayEntryDetails(String type, String categoryName, BigDecimal amount, Date timeStamp){
        System.out.println("Type: " + type);
        System.out.println("Category " + categoryName);
        System.out.println("Amount: " + amount);
        System.out.println("TimeStamp: " + timeStamp.toString());
    }

    public String promptStringInput(String message){
        System.out.println(message);
        input = new Scanner(System.in);
        return input.next();
    }

    public BigDecimal promptBalanceInput(String message){
        System.out.println(message);
        input = new Scanner(System.in);
        return input.nextBigDecimal();
    }

    public void displayMessage(String message){
        System.out.println(message);
    }

    public int promptOptionInput(String[] options){
        System.out.println("Please select an option.");
        for(int i = 0; i < options.length; i++){
            System.out.println(options[i]);
        }
        input = new Scanner(System.in);
        return input.nextInt();
    }

    public int promptSelectCategory(ArrayList<Category> userCategories) {
        System.out.println("Please select an Category.");
        int count = 1;
        for(Category category : userCategories){
            System.out.println(count + ") " + category.getCategoryName());
            count++;
        }
        System.out.println("Or select " + count + " to add a new category.");
        input = new Scanner(System.in);
        return input.nextInt();
    }
}
