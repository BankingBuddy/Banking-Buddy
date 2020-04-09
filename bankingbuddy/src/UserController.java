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
    }

    public void initialiseController(){
        view.getNewEntryButton().addActionListener(e -> makeNewEntry());
        view.getNewGoalButton().addActionListener(e -> makeNewGoal());
        view.getNewCategoryButton().addActionListener(e -> makeNewCategory());
    }

    private void makeNewEntry(){
        NewEntry newEntryDialog = new NewEntry(model.getCategories());
        newEntryDialog.setVisible(true);
        if (newEntryDialog.isMade()){
            Entry newEntry = newEntryDialog.getEntry();
            model.addEntry(newEntry);
            view.insertEntry(newEntry);
        }
    }

    private void makeNewGoal() {
        NewGoal newGoalDialog = new NewGoal();
        newGoalDialog.setVisible(true);
        if (newGoalDialog.isMade()){
            Goal newGoal = newGoalDialog.getGoal();
            model.addGoal(newGoal);
            view.insertGoal(newGoal);
        }
    }

    private void makeNewCategory(){
        NewCategory newCategoryDialog = new NewCategory();
        newCategoryDialog.setVisible(true);
        if (newCategoryDialog.isMade()){
            Category newCategory = newCategoryDialog.getCategory();
            model.addCategory(newCategory);
            view.insertCategory(newCategory);
        }
    }
}
