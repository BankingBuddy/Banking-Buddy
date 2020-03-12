import java.math.BigDecimal;

public class Wallet {
    private final BigDecimal budget;
    private BigDecimal balance;

    public Wallet(BigDecimal budget){
        this.budget = budget;
        this.balance = budget;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance){
        this.balance = balance;
    }

    public void withdraw(BigDecimal amount){
        balance = balance.subtract(amount);
    }

    public void deposit(BigDecimal amount){
        balance = balance.add(amount);
    }
}
