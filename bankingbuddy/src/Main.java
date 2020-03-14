public class Main {
    public static void main(String[] args) {
        UserView theView = new UserView();
        User theUser = new User();
        UserController theController = new UserController(theUser, theView);
        //If account exists then set objects and check recurring.
        //Else register user.
        theController.registerUser();
        theController.selectOptions();
    }
}
