import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.XYDataset;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class UserController {
    private User model;
    private UserView view;

    public UserController(User model, UserView view){
        this.model = model;
        this.view = view;
    }

    public void initialiseView(){
        view.setName(model.getName());
        view.setBalance(model.getWallet().getBalance());
        if (model.getEntries().size() > 0){
            ArrayList<Entry> recurringEntries = new ArrayList<>();
            for (Entry entry : model.getEntries()){
                if (entry.isRecurring() && entry.isPastInterval()){
                    Entry recurringEntry = cloneEntry(entry);
                    recurringEntries.add(recurringEntry);
                    entry.setRecurring(false);
                }
                view.insertEntry(entry);
            }
            for (Entry recurringEntry : recurringEntries){
                model.addEntry(recurringEntry);
                updateBalance(recurringEntry);
                view.insertEntry(recurringEntry);
                updateData();
            }
        }
        if (model.getCategories().size() > 0){
            for (Category category : model.getCategories()){
                view.insertCategory(category);
            }
        }
        if (model.getGoals().size() > 0){
            for (Goal goal : model.getGoals()){
                view.insertGoal(goal);
            }
        }
        initializeChart();
    }

    private Entry cloneEntry(Entry entry){
        Entry clone = new Entry();
        clone.setType(entry.getType());
        clone.setAmount(entry.getAmount());
        clone.setRecurringInterval(entry.getRecurringInterval());
        clone.setDescription(entry.getDescription());
        clone.setRecurring(true);
        clone.setTransactionCategory(entry.getTransactionCategory());
        clone.setTimeStamp(new Date());
        return clone;
    }

    public void initialiseController(){
        view.getNewEntryButton().addActionListener(e -> makeNewEntry());
        view.getNewGoalButton().addActionListener(e -> makeNewGoal());
        view.getNewCategoryButton().addActionListener(e -> makeNewCategory());
        view.getDeleteButton().addActionListener(e -> deleteUser());
        view.getEntriesTable().addMouseListener(mouseAdapter(view.getEntriesTable()));
        view.getGoalsTable().addMouseListener(mouseAdapter(view.getGoalsTable()));
        view.getCategoriesTable().addMouseListener(mouseAdapter(view.getCategoriesTable()));
        view.getSortByEntryComboBox().addItemListener(this::sortEntries);
        view.getSortByGoalComboBox().addItemListener(this::sortGoals);
        view.getEditEntryButton().addActionListener(e -> edit(view.getEntriesTable(), view.getEntriesTable().getSelectedRow()));
        view.getEditCategoryButton().addActionListener(e -> edit(view.getCategoriesTable(), view.getCategoriesTable().getSelectedRow()));
        view.getEditGoalButton().addActionListener(e -> edit(view.getGoalsTable(), view.getGoalsTable().getSelectedRow()));
        view.getTabbedPane().addChangeListener(this::updateTab);
    }

    public void initializeChart(){
        Analysis analyser = new Analysis();
        XYDataset lineDataset = analyser.createLineDataset();
        JFreeChart lineChart = analyser.createChart(lineDataset);
        ChartPanel lineChartPanel = new ChartPanel(lineChart);
        view.insertChartPanel(lineChartPanel);

        DefaultPieDataset pieDataset = analyser.createPieDataset();
        JFreeChart pieChart = analyser.createChart(pieDataset);
        ChartPanel pieChartPanel = new ChartPanel(pieChart);
        view.insertChartPanel(pieChartPanel);
    }

    private void makeNewEntry(){
        NewEntry newEntryDialog = new NewEntry(model.getCategories());
        newEntryDialog.setVisible(true);
        if (newEntryDialog.isMade()){
            Entry newEntry = newEntryDialog.getEntry();
            model.addEntry(newEntry);
            updateBalance(newEntry);
            view.insertEntry(newEntry);
            sortEntryBy(view.getSortByEntryComboBox().getSelectedItem().toString());
            updateData();
        }
    }

    private void updateBalance(Entry entry){
        if (entry.getType().equals(Entry.Type.Expenditure)){
            model.getWallet().withdraw(entry.getAmount());
        }else{
            model.getWallet().deposit(entry.getAmount());
        }
        view.setBalance(model.getWallet().getBalance());
    }

    private void makeNewGoal() {
        NewGoal newGoalDialog = new NewGoal();
        newGoalDialog.setVisible(true);
        if (newGoalDialog.isMade()){
            Goal newGoal = newGoalDialog.getGoal();
            model.addGoal(newGoal);
            view.insertGoal(newGoal);
            updateData();
        }
    }

    private void makeNewCategory(){
        NewCategory newCategoryDialog = new NewCategory();
        newCategoryDialog.setVisible(true);
        if (newCategoryDialog.isMade()){
            Category newCategory = newCategoryDialog.getCategory();
            model.addCategory(newCategory);
            view.insertCategory(newCategory);
            updateData();
        }
    }

    private void sortEntries(ItemEvent e){
        if (e.getStateChange() == ItemEvent.SELECTED){
            sortEntryBy(e.getItem().toString());
        }
    }

    private void sortEntryBy(String item){
        switch (item){
            case "Date":
                model.getEntries().sort(Collections.reverseOrder(Comparator.comparing(Entry::getTimeStamp)));
                break;
            case "Type":
                model.getEntries().sort(Comparator.comparing(o -> o.getType().toString().toLowerCase()));
                break;
            case "Category":
                model.getEntries().sort(Comparator.comparing(o -> o.getTransactionCategory().getCategoryName().toLowerCase()));
                break;
            case "Amount":
                model.getEntries().sort(Collections.reverseOrder(Comparator.comparing(Entry::getAmount)));
                break;
            case "Description":
                model.getEntries().sort(Comparator.comparing(o -> o.getDescription().toLowerCase()));
                break;
            case "Recurring":
                model.getEntries().sort(Collections.reverseOrder(Comparator.comparing(Entry::getRecurringInterval)));
                break;
        }
        view.updateEntryTable(model.getEntries());
    }

    private void sortGoals(ItemEvent e){
        if (e.getStateChange() == ItemEvent.SELECTED){
            sortGoalBy(e.getItem().toString());
        }
    }

    private void sortGoalBy(String item){
        switch (item){
            case "Name":
                model.getGoals().sort(Comparator.comparing(o -> o.getGoalName().toLowerCase()));
                break;
            case "Amount":
                model.getGoals().sort(Collections.reverseOrder(Comparator.comparing(Goal::getGoalAmount)));
                break;
        }
        view.updateGoalTable(model.getGoals());
    }

    private void updateData(){
        new Serializer().serialize("user.ser", model);
    }

    private void updateTab(ChangeEvent e){
        if (e.getSource() instanceof JTabbedPane){
            JTabbedPane pane = (JTabbedPane) e.getSource();
            switch (pane.getSelectedIndex()){
                case 0:
                    view.changeFrameSize(400,400);
                    break;
                case 4:
                    view.changeFrameSize(800, 800);
                    break;
                default:
                    view.changeFrameSize(600,600);
            }
        }
    }

    private void deleteUser() {
        view.clear();
        model.getEntries().clear();
        model.getCategories().clear();
        model.getGoals().clear();

        File userFile = new File("user.ser");
        if (userFile.delete()){
            view.showMessage("Data has been deleted.");
        }
    }

    private MouseAdapter mouseAdapter(JTable table){
        return new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                if (row >= 0 && row < table.getRowCount()){
                    table.setRowSelectionInterval(row, row);
                }else{
                    table.clearSelection();
                }

                int rowIndex = table.getSelectedRow();
                if (rowIndex < 0){
                    return;
                }
                if (e.isPopupTrigger() && e.getComponent() instanceof JTable){
                    JPopupMenu popup = new JPopupMenu();
                    JMenuItem edit = new JMenuItem("Edit");
                    JMenuItem delete = new JMenuItem("Delete");

                    popup.add(edit);
                    popup.add(delete);

                    edit.addActionListener(e1 -> edit(table, rowIndex));
                    delete.addActionListener(e1 -> delete(table, rowIndex));

                    popup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        };
    }

    private void edit(JTable table, int rowIndex){
        if (rowIndex == -1){
            return;
        }
        switch (table.getName()){
            case "Entries":
                Entry previousEntry = model.getEntries().get(rowIndex);
                NewEntry newEntryDialog = new NewEntry(model.getCategories());
                newEntryDialog.setTitle("Edit Entry");
                newEntryDialog.getSubmitButton().setText("Update");
                newEntryDialog.setAmountTextField(previousEntry.getAmount().toString());
                newEntryDialog.setDescriptionTextField(previousEntry.getDescription());
                newEntryDialog.setCategoryComboBox(previousEntry.getTransactionCategory());
                newEntryDialog.setTypeComboBox(previousEntry.getType());
                newEntryDialog.setRecurringCheckBox(previousEntry.isRecurring());
                if (previousEntry.isRecurring()){
                    newEntryDialog.getRecurringTextField().setForeground(new Color(51, 51, 51));
                    newEntryDialog.setRecurringTextField(String.valueOf(previousEntry.getRecurringInterval()));
                }
                newEntryDialog.setVisible(true);

                if (newEntryDialog.isMade()){
                    Entry editedEntry = newEntryDialog.getEntry();

                    BigDecimal updatedBalance = model.getWallet().getBalance();
                    if (previousEntry.getType().equals(Entry.Type.Expenditure)){
                        updatedBalance = updatedBalance.add(previousEntry.getAmount());
                    }else{
                        updatedBalance = updatedBalance.subtract(previousEntry.getAmount());
                    }
                    if (editedEntry.getType().equals(Entry.Type.Expenditure)){
                        updatedBalance = updatedBalance.subtract(editedEntry.getAmount());
                    }else{
                        updatedBalance = updatedBalance.add(editedEntry.getAmount());
                    }
                    model.getWallet().setBalance(updatedBalance);
                    view.setBalance(updatedBalance);

                    editEntry(previousEntry, editedEntry);
                    view.editEntry(editedEntry, rowIndex);
                    updateData();
                }
                break;
            case "Goals":
                Goal previousGoal = model.getGoals().get(rowIndex);
                NewGoal newGoalDialog = new NewGoal();
                newGoalDialog.setTitle("Edit Goal");
                newGoalDialog.getSubmitButton().setText("Update");
                newGoalDialog.setNameTextField(previousGoal.getGoalName());
                newGoalDialog.setAmountTextField(previousGoal.getGoalAmount().toString());
                newGoalDialog.setVisible(true);
                if (newGoalDialog.isMade()){
                    Goal editedGoal = newGoalDialog.getGoal();
                    editGoal(previousGoal, editedGoal);
                    view.editGoal(editedGoal, rowIndex);
                    updateData();
                }
                break;
            case "Categories":
                Category previousCategory = model.getCategories().get(rowIndex);
                NewCategory newCategoryDialog = new NewCategory();
                newCategoryDialog.setTitle("Edit Category");
                newCategoryDialog.getSubmitButton().setText("Update");
                newCategoryDialog.setNameTextField(previousCategory.getCategoryName());
                newCategoryDialog.setVisible(true);
                if (newCategoryDialog.isMade()){
                    Category editedCategory = newCategoryDialog.getCategory();
                    editCategory(previousCategory, editedCategory);
                    view.editCategory(editedCategory, rowIndex);
                    updateData();
                }
                break;
        }
    }

    private void delete(JTable table, int rowIndex){
        switch (table.getName()){
            case "Entries":
                model.getEntries().remove(rowIndex);
                view.removeRow((DefaultTableModel) table.getModel(), rowIndex);
                updateData();
                break;
            case "Goals":
                model.getGoals().remove(rowIndex);
                view.removeRow((DefaultTableModel) table.getModel(), rowIndex);
                updateData();
                break;
            case  "Categories":
                model.getCategories().remove(rowIndex);
                view.removeRow((DefaultTableModel) table.getModel(), rowIndex);
                updateData();
                break;
        }
    }

    private void editEntry(Entry oldEntry, Entry newEntry){
        oldEntry.setType(newEntry.getType());
        oldEntry.setTransactionCategory(newEntry.getTransactionCategory());
        oldEntry.setTimeStamp(newEntry.getTimeStamp());
        oldEntry.setRecurring(newEntry.isRecurring());
        oldEntry.setRecurringInterval(newEntry.getRecurringInterval());
        oldEntry.setDescription(newEntry.getDescription());
        oldEntry.setAmount(newEntry.getAmount());
    }

    private void editGoal(Goal oldGoal, Goal newGoal){
        oldGoal.setGoalAmount(newGoal.getGoalAmount());
        oldGoal.setGoalName(newGoal.getGoalName());
    }

    private void editCategory(Category oldCategory, Category newCategory){
        oldCategory.setCategoryName(newCategory.getCategoryName());
    }
}
