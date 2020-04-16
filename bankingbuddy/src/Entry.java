import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Entry implements Serializable {

    private BigDecimal amount;
    private String description;
    private boolean recurring;
    private int recurringInterval;
    private Date timeStamp;
    private Category transactionCategory;
    private Type type;

    enum Type {
        Income,
        Expenditure
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public boolean isRecurring() {
        return recurring;
    }

    public void setRecurring(boolean recurring){
        this.recurring = recurring;
    }

    public int getRecurringInterval(){
        return recurringInterval;
    }

    public void setRecurringInterval(int recurringInterval){
        this.recurringInterval = recurringInterval;
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
    public boolean isPastInterval(){
        Date currentDate = new Date();
        Date entryDate = getTimeStamp();
        long dayInterval = (long) getRecurringInterval() * 24 * 60 * 60 * 1000;
        return currentDate.after(new Date(entryDate.getTime() + dayInterval));
    }
}