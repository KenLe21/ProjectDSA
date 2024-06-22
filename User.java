import java.io.Serializable;
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String username;
    private String password;
    public User(String username, String password) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Username and password cannot be null or empty");
        }
        this.username = username;
        this.password = password;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
    public String toCSV() {
        return username + "," + password;
    }
    public static User fromCSV(String csv) {
        if (csv == null || !csv.contains(",")) {
            throw new IllegalArgumentException("Invalid CSV format");
        }
        String[] parts = csv.split(",");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid CSV format");
        }
        return new User(parts[0], parts[1]);
    }
}
