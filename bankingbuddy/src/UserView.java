import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
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
    private JPanel allChartPanel;
    private ChartPanel lineChartPanel;
    private ChartPanel pieChartPanel;

    private final DefaultTableModel entriesTableModel;
    private final DefaultTableModel goalsTableModel;
    private final DefaultTableModel categoriesTableModel;

    private final NumberFormat defaultFormat = NumberFormat.getCurrencyInstance();

    public UserView() {
        frame = new JFrame("Banking Buddy");
        frame.setContentPane(rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        allChartPanel.setLayout(new GridLayout(2, 1));
        changeFrameSize(400, 400);
        frame.setVisible(true);

        String[] entriesColumnNames = {"Time", "Type", "Category", "Amount", "Description", "Recurring"};
        entriesTableModel = tableModelNoEdit(entriesColumnNames);
        entriesTable.setModel(entriesTableModel);
        entriesTable.setName("Entries");
        entriesTable.getTableHeader().setReorderingAllowed(false);

        String[] goalsColumnNames = {"Name", "Amount"};
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

    public void insertEntry(Entry entry){
        String recurring = "-";
        if (entry.isRecurring()){
            recurring = entry.getRecurringInterval() + " Days";
        }
        entriesTableModel.addRow(new Object[]{entry.getTimeStamp().toString(), entry.getType().toString(), entry.getTransactionCategory().getCategoryName(),
        defaultFormat.format(entry.getAmount()), entry.getDescription(), recurring});
    }

    public void insertGoal(Goal goal){
        goalsTableModel.addRow(new Object[]{goal.getGoalName(), defaultFormat.format(goal.getGoalAmount())});
    }

    public void insertCategory(Category category){
        categoriesTableModel.addRow(new Object[]{category.getCategoryName()});
    }

    public void insertChartPanel(ChartPanel chartPanel){
        allChartPanel.add(chartPanel);
    }

    public void editEntry(Entry entry, int rowIndex){
        entriesTableModel.setValueAt(entry.getTimeStamp(), rowIndex, 0);
        entriesTableModel.setValueAt(entry.getType().toString(), rowIndex, 1);
        entriesTableModel.setValueAt(entry.getTransactionCategory().getCategoryName(), rowIndex, 2);
        entriesTableModel.setValueAt(defaultFormat.format(entry.getAmount()), rowIndex, 3);
        entriesTableModel.setValueAt(entry.getDescription(), rowIndex, 4);
        String recurring = "-";
        if (entry.isRecurring()){
            recurring = entry.getRecurringInterval() + " Days";
        }
        entriesTableModel.setValueAt(recurring, rowIndex, 5);
    }

    public void editGoal(Goal goal, int rowIndex){
        goalsTableModel.setValueAt(goal.getGoalName(), rowIndex, 0);
        goalsTableModel.setValueAt(defaultFormat.format(goal.getGoalAmount()), rowIndex, 1);
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

    public void showMessage(String message){
        JOptionPane.showMessageDialog(null, message);
    }

    public void clear(){
        clearTable(entriesTableModel);
        clearTable(goalsTableModel);
        clearTable(categoriesTableModel);
        nameLabel.setText("Name: ");
        balanceLabel.setText("Balance: ");
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
