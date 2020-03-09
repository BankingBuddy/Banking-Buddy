import java.util.Date;

public class Entry {

    private String type;    //Expenditure/Income
    private Double amount;
    private String item;
    private Date timeStamp;
    private Category transactionCategory;

    public String getItem() {
        return item;
    }

    public Double getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public Date getTimeStamp(){
        return timeStamp;
    }

    public Category getTransactionCategory() {
        return transactionCategory;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setTransactionCategory(Category transactionCategory) {
        this.transactionCategory = transactionCategory;
    }
}

