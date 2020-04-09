import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.math.BigDecimal;
import java.text.NumberFormat;

public class UserView {
    private JPanel rootPanel;
    private JTabbedPane tabbedPane1;
    private JButton newEntryButton;
    private JTable entriesTable;
    private JTable goalsTable;
    private JButton newGoalButton;
    private JLabel nameLabel;
    private JLabel balanceLabel;
    private JButton newCategoryButton;
    private JTable categoriesTable;

    private DefaultTableModel entriesTableModel;
    private DefaultTableModel goalsTableModel;
    private DefaultTableModel categoriesTableModel;

    private NumberFormat defaultFormat = NumberFormat.getCurrencyInstance();

    public UserView() {
        JFrame frame = new JFrame("Banking Buddy");
        frame.setContentPane(rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        String[] entriesColumnNames = {"Type", "Category", "Amount", "Description", "Recurring", "Time"};
        entriesTableModel = new DefaultTableModel(null, entriesColumnNames);
        entriesTable.setModel(entriesTableModel);

        String[] goalsColumnNames = {"Name", "Amount"};
        goalsTableModel = new DefaultTableModel(null, goalsColumnNames);
        goalsTable.setModel(goalsTableModel);

        categoriesTableModel = new DefaultTableModel(null, new String[]{"Name"});
        categoriesTable.setModel(categoriesTableModel);
    }

    public void setName(String name){
        nameLabel.setText("Name: " + name);
    }

    public void setBalance(BigDecimal balance){
        balanceLabel.setText("Balance: " + defaultFormat.format(balance));
    }

    public void insertEntry(Entry entry){
        entriesTableModel.addRow(new Object[]{entry.getType().toString(), entry.getTransactionCategory().getCategoryName(),
        defaultFormat.format(entry.getAmount()), entry.getDescription(), entry.isRecurring(), entry.getTimeStamp().toString()});
    }

    public void insertCategory(Category category){
        categoriesTableModel.addRow(new Object[]{category.getCategoryName()});
    }

    public void insertGoal(Goal goal){
        goalsTableModel.addRow(new Object[]{goal.getGoalName(), goal.getGoalAmount()});
    }

    public JButton getNewEntryButton(){
        return newEntryButton;
    }

    public JButton getNewGoalButton(){
        return newGoalButton;
    }

    public JButton getNewCategoryButton(){
        return newCategoryButton;
    }
}
