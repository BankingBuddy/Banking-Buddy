import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class BalanceSheet {

    private List<Entry> entries;

    private int ce;

    private BalanceSheet(){
        ce = 0;
        entries = new ArrayList<>();
    }
    private void modifySheet(){
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Press + to add entry, - to delete entry");
        try{
            String in;
            while (!(in = br.readLine()).equals("quit")){
                switch (in){
                    case "+":
                        Entry en = new Entry();
                        en.run();
                        entries.add(en);
                        break;
                    case "-":
                        entries.remove(ce);
                        break;
                }
                System.out.println("Press + to add entry, - to delete entry");
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println("Welcome to your personal balance sheet");
        BalanceSheet bs = new BalanceSheet();
        bs.modifySheet();
        System.out.println(bs.entries.toString());
    }

}
