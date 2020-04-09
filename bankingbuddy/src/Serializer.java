import java.io.*;

public class Serializer {

    public void serialize(String filename, User user){
        File userFile = new File(filename);
        userFile.delete();
        try {
            userFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (FileOutputStream file = new FileOutputStream(filename); ObjectOutputStream out = new ObjectOutputStream(file)){
            out.writeObject(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public User deserialize(String filename){
        User user = null;
        try (FileInputStream file = new FileInputStream(filename); ObjectInputStream in = new ObjectInputStream(file)){
            user = (User)in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return user;
    }
}
