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
}
