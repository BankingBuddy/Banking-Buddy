import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Entry {
    private String type;

    private String amount;

    private String item;

    Entry(){
    }

    public String getItem() {
        return item;
    }

    public String getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    void run(){
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try{
            System.out.println("Are you entering an income or an expenditure?(in/ex)");
            this.type = br.readLine();
            System.out.println("What is your item?");
            this.item = br.readLine();
            System.out.println("How much was it?");
            this.amount = br.readLine();

        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
