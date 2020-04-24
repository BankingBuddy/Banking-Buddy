import org.jfree.chart.ChartPanel;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class UserView {
    private JFrame frame;
    private JPanel rootPanel;
    private JTabbedPane tabbedPane;
    private JButton newEntryButton;
    private JTable entriesTable;
    private JTable goalsTable;
    private JButton newGoalButton;
    private JLabel nameLabel;
    private JLabel balanceLabel;
    private JButton newCategoryButton;
    private JTable categoriesTable;
    private JButton deleteButton;
    private JComboBox sortByEntryComboBox;
    private JButton editEntryButton;
    private JButton editCategoryButton;
    private JButton editGoalButton;
    private JComboBox sortByGoalComboBox;
    private JPanel lineChartPanel;
    private JPanel pieChartPanel;
    private JComboBox pieChartTypeComboBox;
    private JComboBox lineChartTypeComboBox;
    private JComboBox rangeComboBox;
    private JLabel goalLabel;
    private JLabel amountLabel;
    private JLabel completedLabel;
    private JLabel timeLabel;
    private JLabel moneySpentLabel;
    private JLabel moneyLeftLabel;

    private final DefaultTableModel entriesTableModel;
    private final DefaultTableModel goalsTableModel;
    private final DefaultTableModel categoriesTableModel;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private final NumberFormat defaultFormat = NumberFormat.getCurrencyInstance();

    public UserView() {
        frame = new JFrame("Banking Buddy");
        frame.setContentPane(rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        changeFrameSize(600, 400);
        lineChartPanel.setLayout(new GridLayout(1,1));
        pieChartPanel.setLayout(new GridLayout(1,1));
        frame.setVisible(true);

        String[] entriesColumnNames = {"ID", "Time", "Type", "Category", "Amount", "Description", "Recurring"};
        entriesTableModel = tableModelNoEdit(entriesColumnNames);
        entriesTable.setModel(entriesTableModel);
        entriesTable.setName("Entries");
        entriesTable.getTableHeader().setReorderingAllowed(false);

        String[] goalsColumnNames = {"Name", "Amount", "Complete By", "Completed"};
        goalsTableModel = tableModelNoEdit(goalsColumnNames);
        goalsTable.setModel(goalsTableModel);
        goalsTable.setName("Goals");
        goalsTable.getTableHeader().setReorderingAllowed(false);

        categoriesTableModel = tableModelNoEdit(new String[]{"Name"});
        categoriesTable.setModel(categoriesTableModel);
        categoriesTable.setName("Categories");
    }

    public void setName(String name){
        nameLabel.setText("Name: " + name);
    }

    public void setBalance(BigDecimal balance){
        balanceLabel.setText("Balance: " + defaultFormat.format(balance));
    }

    public void setGoal(String name) {
        goalLabel.setText("Goal: " + name);
    }

    public void setAmount(BigDecimal amount) {
        amountLabel.setText("Amount: " + defaultFormat.format(amount));
    }

    public void setTime(String days) {
        timeLabel.setText("Time Left: " + days + " days");
    }

    public void setCompleted(boolean completed) {
        if (completed){
            completedLabel.setText("Completed: yes");
        }else{
            completedLabel.setText("Completed: no");
        }
    }

    public void setMoneySpent(BigDecimal amount){
        moneySpentLabel.setText("Money Spent: " + defaultFormat.format(amount));
    }

    public void setMoneyLeft(BigDecimal amount){
        moneyLeftLabel.setText("Money Left: " + defaultFormat.format(amount));
    }

    public void insertEntry(Entry entry){
        String recurring = "-";
        if (entry.isRecurring()){
            recurring = entry.getRecurringInterval() + " Days";
        }
        entriesTableModel.addRow(new Object[]{entry.getID(), dateFormat.format(entry.getTimeStamp()), entry.getType().toString(), entry.getTransactionCategory().getCategoryName(),
        defaultFormat.format(entry.getAmount()), entry.getDescription(), recurring});
    }

    public void insertGoal(Goal goal){
        goalsTableModel.addRow(new Object[]{goal.getGoalName(), defaultFormat.format(goal.getGoalAmount()), dateFormat.format(goal.getCompleteBy()), goal.isCompleted()});
    }

    public void insertCategory(Category category){
        categoriesTableModel.addRow(new Object[]{category.getCategoryName()});
    }

    public void insertLineChartPanel(ChartPanel chartPanel){
        lineChartPanel.add(chartPanel);
    }

    public void insertPieChartPanel(ChartPanel chartPanel){
        pieChartPanel.add(chartPanel);
    }

    public void updateLineChartPanel(ChartPanel chartPanel){
        lineChartPanel.remove(0);
        lineChartPanel.add(chartPanel);
        lineChartPanel.updateUI();
    }

    public void updatePieChartPanel(ChartPanel chartPanel){
        pieChartPanel.remove(0);
        pieChartPanel.add(chartPanel);
        pieChartPanel.updateUI();
    }

    public void editEntry(Entry entry, int rowIndex){
        entriesTableModel.setValueAt(entry.getID(), rowIndex, 0);
        entriesTableModel.setValueAt(dateFormat.format(entry.getTimeStamp()), rowIndex, 1);
        entriesTableModel.setValueAt(entry.getType().toString(), rowIndex, 2);
        entriesTableModel.setValueAt(entry.getTransactionCategory().getCategoryName(), rowIndex, 3);
        entriesTableModel.setValueAt(defaultFormat.format(entry.getAmount()), rowIndex, 4);
        entriesTableModel.setValueAt(entry.getDescription(), rowIndex, 5);
        String recurring = "-";
        if (entry.isRecurring()){
            recurring = entry.getRecurringInterval() + " Days";
        }
        entriesTableModel.setValueAt(recurring, rowIndex, 6);
    }

    public void editGoal(Goal goal, int rowIndex){
        goalsTableModel.setValueAt(goal.getGoalName(), rowIndex, 0);
        goalsTableModel.setValueAt(defaultFormat.format(goal.getGoalAmount()), rowIndex, 1);
        goalsTableModel.setValueAt(dateFormat.format(goal.getCompleteBy()), rowIndex, 2);
        goalsTableModel.setValueAt(goal.isCompleted(), rowIndex, 3);
    }

    public void editCategory(Category category, int rowIndex){
        categoriesTableModel.setValueAt(category.getCategoryName(), rowIndex, 0);
    }

    public void updateEntryTable(ArrayList<Entry> entries){
        clearTable(entriesTableModel);
        for (Entry entry : entries){
            insertEntry(entry);
        }
    }

    public void updateGoalTable(ArrayList<Goal> goals){
        clearTable(goalsTableModel);
        for (Goal goal : goals){
            insertGoal(goal);
        }
    }

    public void removeRow(DefaultTableModel table, int rowIndex){
        table.removeRow(rowIndex);
    }

    private void clearTable(DefaultTableModel tableModel){
        tableModel.setRowCount(0);
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

    public JButton getDeleteButton(){
        return deleteButton;
    }

    public JTable getEntriesTable(){
        return entriesTable;
    }

    public JTable getGoalsTable(){
        return goalsTable;
    }

    public JTable getCategoriesTable(){
        return categoriesTable;
    }

    public JComboBox getSortByEntryComboBox(){
        return sortByEntryComboBox;
    }

    public JComboBox getSortByGoalComboBox(){
        return sortByGoalComboBox;
    }

    public JButton getEditEntryButton(){
        return editEntryButton;
    }

    public JButton getEditCategoryButton(){
        return editCategoryButton;
    }

    public JButton getEditGoalButton(){
        return editGoalButton;
    }

    public JTabbedPane getTabbedPane(){
        return tabbedPane;
    }

    public JComboBox getPieChartTypeComboBox(){
        return pieChartTypeComboBox;
    }

    public JComboBox getLineChartTypeComboBox(){
        return lineChartTypeComboBox;
    }

    public JComboBox getRangeComboBox(){
        return rangeComboBox;
    }

    public void showMessage(String message){
        JOptionPane.showMessageDialog(null, message);
    }

    public void clear(){
        clearTable(entriesTableModel);
        clearTable(goalsTableModel);
        clearTable(categoriesTableModel);
        nameLabel.setText("Name: ");
        balanceLabel.setText("Balance: ");
        moneySpentLabel.setText("Money Spent: ");
        moneyLeftLabel.setText("Money Left");
        goalLabel.setText("Name: ");
        timeLabel.setText("Time Left: ");
        amountLabel.setText("Amount: ");
        completedLabel.setText("Completed: ");
    }

    private DefaultTableModel tableModelNoEdit(Object[] columnNames){
        return new DefaultTableModel(null, columnNames){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    public void changeFrameSize(int width, int height){
        frame.setSize(width, height);
    }
}
