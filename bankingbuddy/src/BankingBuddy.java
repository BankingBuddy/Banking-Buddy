import java.io.File;

public class BankingBuddy {
    public static void main(String[] args) {
        String filePath = "user.ser";
        File userFile = new File(filePath);
        User user = null;
        if (userFile.exists()){
            user = new Serializer().deserialize(filePath);
        }else{
            NewUser newUser = new NewUser();
            newUser.setVisible(true);
            if (newUser.isMade()){
                user = newUser.getUser();
                new Serializer().serialize(filePath, user);
            }else{
                System.exit(0);
            }
        }

        UserView view = new UserView();
        UserController controller = new UserController(user, view);
        controller.initialiseView();
        controller.initialiseController();

    }
}
