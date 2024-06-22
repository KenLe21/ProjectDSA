import java.io.*;
import java.util.ArrayList;
import java.util.List;
public class UserManager {
    private List<User> users;
    private static final String FILE_NAME = "userdata.csv";
    public UserManager() {
        users = loadUsers();
    }
    public void addUser(User user) {
        users.add(user);
        saveUsers();
    }
    private List<User> loadUsers() {
        List<User> userList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                userList.add(User.fromCSV(line));
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userList;
    }
    private void saveUsers() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (User user : users) {
                writer.write(user.toCSV());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public List<User> getUsers() {
        return users;
    }
    public User getUserByUsername(String username) {
        List<User> userList = getUsers();
        for (User userData : userList) {
            if (userData.getUsername().equals(username)) {
                return userData;
            }
        }
        return null;
    }
}
