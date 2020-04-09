import java.io.Serializable;
import java.math.BigDecimal;

public class Wallet implements Serializable {
    private BigDecimal balance;

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