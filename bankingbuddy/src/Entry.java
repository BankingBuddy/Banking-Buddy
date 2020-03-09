import jdk.jfr.Category;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

public class Entry {
    private String type;

    private Double amount;

    private String item;

    private Date timeStamp;

    private Category transCat;

    Entry(){
    }

    public String getItem() {
        return item;
    }

    public Double getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public Category getTransCat(){return Category;}

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

    public void setTransCat(Category transCat) {
        this.transCat = transCat;
    }
}

