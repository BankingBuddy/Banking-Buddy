import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Goal implements Serializable {
    private boolean completed;
    private Date completeBy;
    private String goalName;
    private BigDecimal goalAmount;
    private BigDecimal startingAmount;

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Date getCompleteBy() {
        return completeBy;
    }

    public void setCompleteBy(Date completeBy) {
        this.completeBy = completeBy;
    }

    public String getGoalName() {
        return goalName;
    }

    public void setGoalName(String goalName){
        this.goalName = goalName;
    }

    public BigDecimal getGoalAmount(){
        return goalAmount;
    }

    public void setGoalAmount(BigDecimal goalAmount) {
        this.goalAmount = goalAmount;
    }

    public BigDecimal getStartingAmount(){
        return startingAmount;
    }

    public void setStartingAmount(BigDecimal startingAmount) {
        this.startingAmount = startingAmount;
    }
}