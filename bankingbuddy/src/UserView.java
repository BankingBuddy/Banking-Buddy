import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
    private JButton deleteButton;

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
        entriesTableModel = tableModelNoEdit(null, entriesColumnNames);
        entriesTable.setModel(entriesTableModel);
        entriesTable.setName("Entries");

        String[] goalsColumnNames = {"Name", "Amount"};
        goalsTableModel = tableModelNoEdit(null, goalsColumnNames);
        goalsTable.setModel(goalsTableModel);
        goalsTable.setName("Goals");

        categoriesTableModel = tableModelNoEdit(null, new String[]{"Name"});
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
        entriesTableModel.addRow(new Object[]{entry.getType().toString(), entry.getTransactionCategory().getCategoryName(),
        defaultFormat.format(entry.getAmount()), entry.getDescription(), entry.isRecurring(), entry.getTimeStamp().toString()});
    }

    public void insertGoal(Goal goal){
        goalsTableModel.addRow(new Object[]{goal.getGoalName(), defaultFormat.format(goal.getGoalAmount())});
    }

    public void insertCategory(Category category){
        categoriesTableModel.addRow(new Object[]{category.getCategoryName()});
    }

    public void editEntry(Entry entry, int rowIndex){
        entriesTableModel.setValueAt(entry.getType().toString(), rowIndex, 0);
        entriesTableModel.setValueAt(entry.getTransactionCategory().getCategoryName(), rowIndex, 1);
        entriesTableModel.setValueAt(defaultFormat.format(entry.getAmount()), rowIndex, 2);
        entriesTableModel.setValueAt(entry.getDescription(), rowIndex, 3);
        entriesTableModel.setValueAt(entry.isRecurring(), rowIndex, 4);
        entriesTableModel.setValueAt(entry.getTimeStamp(), rowIndex, 5);
    }

    public void editGoal(Goal goal, int rowIndex){
        goalsTableModel.setValueAt(goal.getGoalName(), rowIndex, 0);
        goalsTableModel.setValueAt(defaultFormat.format(goal.getGoalAmount()), rowIndex, 1);
    }

    public void editCategory(Category category, int rowIndex){
        categoriesTableModel.setValueAt(category.getCategoryName(), rowIndex, 0);
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

    private DefaultTableModel tableModelNoEdit(Object[][] data, Object[] columnNames){
        return new DefaultTableModel(data, columnNames){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }
}
