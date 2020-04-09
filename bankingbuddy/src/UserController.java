import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

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
            for (Entry entry : model.getEntries()){
                view.insertEntry(entry);
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
    }

    public void initialiseController(){
        view.getNewEntryButton().addActionListener(e -> makeNewEntry());
        view.getNewGoalButton().addActionListener(e -> makeNewGoal());
        view.getNewCategoryButton().addActionListener(e -> makeNewCategory());
        view.getDeleteButton().addActionListener(e -> deleteUser());
        view.getEntriesTable().addMouseListener(mouseAdapter(view.getEntriesTable()));
        view.getGoalsTable().addMouseListener(mouseAdapter(view.getGoalsTable()));
        view.getCategoriesTable().addMouseListener(mouseAdapter(view.getCategoriesTable()));
    }

    private void makeNewEntry(){
        NewEntry newEntryDialog = new NewEntry(model.getCategories());
        newEntryDialog.setVisible(true);
        if (newEntryDialog.isMade()){
            Entry newEntry = newEntryDialog.getEntry();
            model.addEntry(newEntry);
            if (newEntry.getType().equals(Entry.Type.Expenditure)){
                model.getWallet().withdraw(newEntry.getAmount());
            }else{
                model.getWallet().deposit(newEntry.getAmount());
            }
            view.setBalance(model.getWallet().getBalance());
            view.insertEntry(newEntry);
            updateData();
        }
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

    private void updateData(){
        new Serializer().serialize("user.ser", model);
    }

    private void deleteUser() {
        view.clear();
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
        switch (table.getName()){
            case "Entries":
                Entry previousEntry = model.getEntries().get(rowIndex);
                NewEntry newEntryDialog = new NewEntry(model.getCategories());
                newEntryDialog.setTitle("Edit Entry");
                newEntryDialog.setAmountTextField(previousEntry.getAmount().toString());
                newEntryDialog.setDescriptionTextField(previousEntry.getDescription());
                newEntryDialog.setCategoryComboBox(previousEntry.getTransactionCategory());
                newEntryDialog.setTypeComboBox(previousEntry.getType());
                newEntryDialog.setRecurringCheckBox(previousEntry.isRecurring());
                newEntryDialog.setVisible(true);
                if (newEntryDialog.isMade()){
                    Entry editedEntry = newEntryDialog.getEntry();
                    //UPDATE THE BALANCE AFTER THE EDIT
                    editEntry(previousEntry, editedEntry);
                    view.editEntry(editedEntry, rowIndex);
                    updateData();
                }
                break;
            case "Goals":
                Goal previousGoal = model.getGoals().get(rowIndex);
                NewGoal newGoalDialog = new NewGoal();
                newGoalDialog.setTitle("Edit Goal");
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
