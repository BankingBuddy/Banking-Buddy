public class Main {
    public static void main(String[] args) {
        UserView theView = new UserView();
        User theUser = new User();
        UserController theController = new UserController(theUser, theView);
        theController.registerUser();
        theController.selectOptions();
    }
}
