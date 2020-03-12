import java.math.BigDecimal;
import java.util.Date;

public class Entry {

    private Type type;
    private BigDecimal amount;
    private String item;
    private Date timeStamp;
    private Category transactionCategory;

    enum Type {
        INCOME,
        EXPENDITURE
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Date getTimeStamp(){
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Category getTransactionCategory() {
        return transactionCategory;
    }

    public void setTransactionCategory(Category transactionCategory) {
        this.transactionCategory = transactionCategory;
    }

    //Checks if the date is before the specified number of days.
    public boolean youngerThanDate(int days){
        long currentTime = new Date().getTime();
        long timeInDays = days * 24 * 60 * 60 * 1000;
        return timeStamp.getTime() < (currentTime - timeInDays);
    }
}

