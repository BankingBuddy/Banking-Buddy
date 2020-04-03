public class BankingBuddy {
    public static void main(String[] args) {
        NewUser newUser = new NewUser();
        newUser.setVisible(true);
        if (newUser.isMade()){
            String name = newUser.getName();
            Wallet wallet = new Wallet(newUser.getBalance());

            User user = new User(name, wallet);
            UserView view = new UserView();
            UserController controller = new UserController(user, view);
            controller.initialiseView();
            controller.initialiseController();
        }
    }
}
