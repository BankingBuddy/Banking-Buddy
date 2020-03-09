import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class BalanceSheet {

    private User user;

    private List<Entry> entries;
    private List<Goal> goals;
    private List<Category> categories;

    public BalanceSheet(){
        user = new User();
        goals = new ArrayList<>();
        entries = new ArrayList<>();
        categories = new ArrayList<>();
        start();
    }

    private void addEntry(Entry newEntry){
        entries.add(newEntry);
    }

    private void addGoal(Goal newGoal){
        goals.add(newGoal);
    }

    private void addCategory(Category newCategory){
        categories.add(newCategory);
    }

    private void updateBalance(Double amount){
        user.getWallet().deposit(amount);
    }

    private void start(){
        while (true){
            int option = chooseOptions();
            switch (option){
                case 1:
                    makeEntry();
                    break;
                case 2:
                    makeGoal();
                    break;
                case 3:
                    for (Entry e : entries){
                        System.out.println(e.getAmount() + " - " + e.getType() + " - " + e.getTransactionCategory().getCategoryName() + " - " + e.getTimeStamp().toString());
                    }
                    break;
                default:
                    System.out.println("No option chosen.");
            }
        }
    }

    private int chooseOptions(){
        Scanner input = new Scanner(System.in);
        System.out.println("Select an option: ");
        System.out.println("1) Add an entry.");
        System.out.println("2) Add a goal.");
        System.out.println("3) View entry");
        return input.nextInt();
    }

    private void makeEntry(){
        Scanner input = new Scanner(System.in);
        Entry newEntry = new Entry();
        if (categories.isEmpty()){
            System.out.println("Enter a new Category name: ");
            newEntry.setTransactionCategory(createCategory());
        }else{
            System.out.println("Select a Category");
            for (Category c : categories){
                int index = categories.indexOf(c);
                System.out.println((index + 1) + ") " + c.getCategoryName());
            }
            System.out.println((categories.size() + 1) + ") Make new Category.");
            int option = input.nextInt();
            if (option == (categories.size() + 1)){
                System.out.println("Enter a new Category name: ");
                newEntry.setTransactionCategory(createCategory());
            } else {
                newEntry.setTransactionCategory(categories.get(option - 1));
            }
        }
        System.out.println("Enter amount: ");
        Double amount = input.nextDouble();
        newEntry.setAmount(amount);
        System.out.println("Type of transaction");
        newEntry.setType(input.next());
        Date date = new Date();
        newEntry.setTimeStamp(date);
        addEntry(newEntry);
    }

    private Category createCategory(){
        Scanner input = new Scanner(System.in);
        String categoryName = input.next();
        Category newCategory = new Category(categoryName);
        addCategory(newCategory);
        return newCategory;
    }

    private void makeGoal(){
        Scanner input = new Scanner(System.in);
        System.out.println("Enter the name of your goal: ");
        String goalName = input.next();
        System.out.println("Enter the amount of your goal: ");
        double amount = input.nextDouble();
        Goal newGoal = new Goal(goalName, amount);
        addGoal(newGoal);
    }
}
