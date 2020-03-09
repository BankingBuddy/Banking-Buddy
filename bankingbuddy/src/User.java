import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class User {
    private String name;
    private Wallet wallet;

    public User(){
        registerUser();
    }

    public User(String name, Wallet wallet){
        this.name = name;
        this.wallet = wallet;
    }

    public String getName() {
        return name;
    }

    public Wallet getWallet() {
        return wallet;
    }

    private void registerUser() {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter your name: ");
        try {
            name = input.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        wallet = new Wallet();
    }
}
