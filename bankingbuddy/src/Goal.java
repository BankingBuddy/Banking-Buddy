import java.math.BigDecimal;

public class Goal {

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
