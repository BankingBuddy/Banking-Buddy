import java.io.Serializable;
import java.math.BigDecimal;

public class Goal implements Serializable {

    private String goalName;
    private BigDecimal goalAmount;


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
}