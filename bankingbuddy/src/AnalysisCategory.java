import java.math.BigDecimal;

public class AnalysisCategory extends Category {
    private BigDecimal amount;
    public AnalysisCategory(String categoryName) {
        super(categoryName);
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
