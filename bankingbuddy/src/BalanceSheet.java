import java.util.ArrayList;
import java.util.List;

public class BalanceSheet {

    private User user;

    private List<Entry> entries;
    private List<Goals> goals;

    public BalanceSheet(){
        goals = new ArrayList<>();
        entries = new ArrayList<>();
    }
    private void addEntry(Entry newEntry){
        entries.add(newEntry);
    }
    private void addGoal(Goals newGoal){
        goals.add(newGoal);
    }

    private void updateBalance(Double amount){
        user.getWallet().deposit(amount);
    }
}
