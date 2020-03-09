public class Wallet {
    private Double balance = 0.00;

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
}
