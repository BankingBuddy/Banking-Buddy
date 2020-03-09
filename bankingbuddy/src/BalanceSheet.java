import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class BalanceSheet {

    private List<Entry> entries;

    private List<Goals> goals;

    private BalanceSheet(){
        goals = new ArrayList<>();
        entries = new ArrayList<>();
    }
    private void addEntry(Entry newEntry){
        entries.add(newEntry);
    }
    private void addGoal(Goals newGoal){
        goals.add(newGoal);
    }

    private void updateBalanceSheet(Double amount){

    }
}
