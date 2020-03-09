import java.util.Scanner;

public class Wallet {
    private Double balance = 0.00;

    public Wallet(){
        setupWallet();
    }

    public Wallet(double balance){
        this.balance = balance;
    }

    public Double getBalance() {
        return balance;
    }

    private void withdraw(double amount){

    }

    public void deposit(double amount){
        balance += amount;
    }

    private void setupWallet(){
        Scanner input = new Scanner(System.in);
        System.out.println("Enter the amount of money you have: ");
        balance = input.nextDouble();
    }
}
