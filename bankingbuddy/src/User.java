import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    private String name;
    private Wallet wallet;
    private ArrayList<Entry> entries = new ArrayList<>();
    private ArrayList<Category> categories = new ArrayList<>();
    private ArrayList<Goal> goals = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet){
        this.wallet = wallet;
    }

    public ArrayList<Entry> getEntries(){
        return entries;
    }

    public void setEntries(ArrayList<Entry> entries){
        this.entries = entries;
    }

    public void addEntry(Entry entry){
        entries.add(entry);
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }

    public void addCategory(Category category){
        categories.add(category);
    }

    public ArrayList<Goal> getGoals(){
        return goals;
    }

    public void setGoals(ArrayList<Goal> goals){
        this.goals = goals;
    }

    public void addGoal(Goal goal){
        goals.add(goal);
    }
}