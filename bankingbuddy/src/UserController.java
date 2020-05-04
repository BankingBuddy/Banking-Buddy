import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateTickUnitType;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.math.BigDecimal;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class UserController {
    private final User model;
    private final UserView view;

    public UserController(User model, UserView view){
        this.model = model;
        this.view = view;
    }

    public void initialiseView(){
        view.setName(model.getName());
        view.setBalance(model.getWallet().getBalance());
        if (model.getEntries().size() > 0){
            checkRecurringEntries();
        }
        if (model.getCategories().size() > 0){
            for (Category category : model.getCategories()){
                view.insertCategory(category);
            }
        }
        if (model.getGoals().size() > 0){
            for (Goal goal : model.getGoals()){
                checkCompletedGoal(goal);
                view.insertGoal(goal);
            }
            updateGoalSection();
        }
        initializeChart();
    }

    private void checkRecurringEntries(){
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

    private void checkCompletedGoal(Goal goal){
        Date currentDate = new Date();
        if (goal.getCompleteBy().before(currentDate)){
            if (model.getWallet().getBalance().compareTo(goal.getStartingAmount().subtract(goal.getGoalAmount())) > 0){
                goal.setCompleted(true);
            }
        }
    }

    private void updateGoalSection(){
        Goal currentGoal = model.getGoals().get(0);
        for (Goal goal : model.getGoals()){
            if (!goal.isCompleted() && goal.getCompleteBy().before(currentGoal.getCompleteBy()) && goal.getCompleteBy().after(new Date(System.currentTimeMillis() - 24*3600*1000))){
                currentGoal = goal;
            }
        }
        view.setAmount(currentGoal.getGoalAmount());
        view.setGoal(currentGoal.getGoalName());
        view.setMoneySpent(currentGoal.getStartingAmount().subtract(model.getWallet().getBalance()));
        view.setMoneyLeft(currentGoal.getGoalAmount().subtract(currentGoal.getStartingAmount().subtract(model.getWallet().getBalance())));
        view.setCompleted(currentGoal.isCompleted());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentGoal.getCompleteBy());
        LocalDate date1 = LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
        LocalDate date2 = LocalDate.now();
        long daysBetween = ChronoUnit.DAYS.between(date2, date1);
        view.setTime(Long.toString(daysBetween));
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
        view.getPieChartTypeComboBox().addItemListener(this::changeChart);
        view.getLineChartTypeComboBox().addItemListener(this::changeChart);
        view.getRangeComboBox().addItemListener(this::changeChart);
    }

    public void initializeChart(){
        Analysis analyser = new Analysis();

        JFreeChart lineChart = analyser.createTypeLineChart(model.getEntries(), DateTickUnitType.MINUTE);
        ChartPanel lineChartPanel = new ChartPanel(lineChart);
        view.insertLineChartPanel(lineChartPanel);

        JFreeChart pieChart = analyser.createPieChart(model.getEntries(), Entry.Type.Expenditure);
        ChartPanel pieChartPanel = new ChartPanel(pieChart);
        view.insertPieChartPanel(pieChartPanel);
    }

    public void updateCharts(){
        Analysis analyser = new Analysis();
        JFreeChart lineChart;
        if (view.getLineChartTypeComboBox().getSelectedIndex() == 0){
            lineChart = analyser.createTypeLineChart(model.getEntries(), getRange());
        }else{
            lineChart = analyser.createCategoryLineChart(model.getEntries(), getRange());
        }
        ChartPanel lineChartPanel = new ChartPanel(lineChart);
        view.updateLineChartPanel(lineChartPanel);

        JFreeChart pieChart;
        if (view.getPieChartTypeComboBox().getSelectedIndex() == 0){
            pieChart = analyser.createPieChart(model.getEntries(), Entry.Type.Expenditure);
        }else{
            pieChart = analyser.createPieChart(model.getEntries(), Entry.Type.Income);
        }
        ChartPanel pieChartPanel = new ChartPanel(pieChart);
        view.updatePieChartPanel(pieChartPanel);
    }

    private DateTickUnitType getRange(){
       switch (view.getRangeComboBox().getSelectedIndex()){
           case 0:
               return DateTickUnitType.MINUTE;
           case 1:
               return DateTickUnitType.HOUR;
           case 2:
               return DateTickUnitType.DAY;
           case 3:
               return DateTickUnitType.MONTH;
           default:
               return DateTickUnitType.YEAR;
       }
    }

    private void makeNewEntry(){
        NewEntry newEntryDialog = new NewEntry(model.getCategories());
        newEntryDialog.setVisible(true);
        if (newEntryDialog.isMade()){
            Entry newEntry = newEntryDialog.getEntry();
            newEntry.setID(model.getEntries().size() + 1);
            model.addEntry(newEntry);
            updateBalance(newEntry);
            view.insertEntry(newEntry);
            sortEntryBy(Objects.requireNonNull(view.getSortByEntryComboBox().getSelectedItem()).toString());
            updateData();
            updateCharts();
            if (!model.getGoals().isEmpty()){
                updateGoalSection();
            }
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
            newGoal.setStartingAmount(model.getWallet().getBalance());
            model.addGoal(newGoal);
            view.insertGoal(newGoal);
            sortGoalBy(Objects.requireNonNull(view.getSortByGoalComboBox().getSelectedItem()).toString());
            updateGoalSection();
            updateData();
            updateCharts();
        }
    }

    private void makeNewCategory(){
        NewCategory newCategoryDialog = new NewCategory();
        newCategoryDialog.setVisible(true);
        if (newCategoryDialog.isMade()){
            boolean existing = false;
            Category newCategory = newCategoryDialog.getCategory();
            for (Category category : model.getCategories()){
                if (category.getCategoryName().toLowerCase().equals(newCategory.getCategoryName().toLowerCase())){
                    existing = true;
                    break;
                }
            }
            if (!existing){
                model.addCategory(newCategory);
                view.insertCategory(newCategory);
                updateData();
                updateCharts();
            }else{
                view.showMessage("Category already exists.");
            }
        }
    }

    private void sortEntries(ItemEvent e){
        if (e.getStateChange() == ItemEvent.SELECTED){
            sortEntryBy(e.getItem().toString());
        }
    }

    private void sortEntryBy(String item){
        switch (item){
            case "ID":
                model.getEntries().sort(Comparator.comparingInt(Entry::getID));
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
            case "Completed By":
                model.getGoals().sort(Collections.reverseOrder(Comparator.comparing(Goal::getCompleteBy)));
                break;
            case "Completed":
                model.getGoals().sort(Collections.reverseOrder(Comparator.comparing(Goal::isCompleted)));
                break;
        }
        view.updateGoalTable(model.getGoals());
    }

    private void changeChart(ItemEvent e){
        if(e.getStateChange() == ItemEvent.SELECTED){
            updateCharts();
        }
    }

    private void updateData(){
        new Serializer().serialize("user.ser", model);
    }

    private void updateTab(ChangeEvent e){
        if (e.getSource() instanceof JTabbedPane){
            JTabbedPane pane = (JTabbedPane) e.getSource();
            switch (pane.getSelectedIndex()){
                case 0:
                    view.changeFrameSize(600, 400);
                    break;
                case 4:
                    view.changeFrameSize(1000, 1000);
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
        updateCharts();

        File userFile = new File("user.ser");
        if (userFile.delete()){
            view.showMessage("Data has been deleted.");
        }
        System.exit(0);
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
                    updateCharts();
                    if (!model.getGoals().isEmpty()){
                        updateGoalSection();
                    }
                }
                break;
            case "Goals":
                Goal previousGoal = model.getGoals().get(rowIndex);
                NewGoal newGoalDialog = new NewGoal();
                newGoalDialog.setTitle("Edit Goal");
                newGoalDialog.getSubmitButton().setText("Update");
                newGoalDialog.setNameTextField(previousGoal.getGoalName());
                newGoalDialog.setAmountTextField(previousGoal.getGoalAmount().toString());
                newGoalDialog.setDatePicker(previousGoal.getCompleteBy());
                newGoalDialog.setVisible(true);
                if (newGoalDialog.isMade()){
                    Goal editedGoal = newGoalDialog.getGoal();
                    editGoal(previousGoal, editedGoal);
                    view.editGoal(editedGoal, rowIndex);
                    updateData();
                    updateCharts();
                    updateGoalSection();
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
                    boolean existing = false;
                    Category editedCategory = newCategoryDialog.getCategory();
                    for (Category category : model.getCategories()){
                        if (category.getCategoryName().toLowerCase().equals(editedCategory.getCategoryName().toLowerCase())) {
                            existing = true;
                            break;
                        }
                    }
                    if (!existing){
                        editCategory(previousCategory, editedCategory);
                        view.editCategory(editedCategory, rowIndex);
                        updateData();
                        updateCharts();
                    }else{
                        view.showMessage("Category already exists.");
                    }
                }
                break;
        }
        updateCharts();
    }

    private void delete(JTable table, int rowIndex){
        switch (table.getName()){
            case "Entries":
                Entry entryToDelete = model.getEntries().get(rowIndex);
                if (entryToDelete.getType().equals(Entry.Type.Expenditure)){
                    model.getWallet().setBalance(model.getWallet().getBalance().add(entryToDelete.getAmount()));
                }else{
                    model.getWallet().setBalance(model.getWallet().getBalance().subtract(entryToDelete.getAmount()));
                }
                view.setBalance(model.getWallet().getBalance());
                model.getEntries().remove(rowIndex);
                view.removeRow((DefaultTableModel) table.getModel(), rowIndex);
                updateData();
                updateCharts();
                if (!model.getGoals().isEmpty()){
                    updateGoalSection();
                }
                break;
            case "Goals":
                model.getGoals().remove(rowIndex);
                view.removeRow((DefaultTableModel) table.getModel(), rowIndex);
                updateData();
                updateCharts();
                updateGoalSection();
                break;
            case  "Categories":
                model.getCategories().remove(rowIndex);
                view.removeRow((DefaultTableModel) table.getModel(), rowIndex);
                updateData();
                updateCharts();
                break;
        }
        updateCharts();
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
        oldGoal.setCompleteBy(newGoal.getCompleteBy());
        oldGoal.setCompleted(newGoal.isCompleted());
    }

    private void editCategory(Category oldCategory, Category newCategory){
        oldCategory.setCategoryName(newCategory.getCategoryName());
    }
}
