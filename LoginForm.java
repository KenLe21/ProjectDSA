import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;

public class LoginForm extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel messageLabel;
    private Map<String, User> users;

    public LoginForm() {
        users = loadUsersFromFile("E:\\lab scale\\ProjectDSA\\userdata.csv");

        setTitle("Login Form");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create form panel
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1));

        panel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        JButton togglePasswordButton = new JButton("Show Password");
        togglePasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (passwordField.getEchoChar() != '\u0000') {
                    passwordField.setEchoChar('\u0000');
                    togglePasswordButton.setText("Hide Password");
                } else {
                    passwordField.setEchoChar('•');
                    togglePasswordButton.setText("Show Password");
                }
            }
        });
        panel.add(togglePasswordButton);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkLogin();
            }
        });
        panel.add(loginButton);
        messageLabel = new JLabel("", SwingConstants.CENTER);
        panel.add(messageLabel);
        add(panel);
        setVisible(true);
    }

    private Map<String, User> loadUsersFromFile(String filename) {
        Map<String, User> users = new HashMap<>();
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                User user = User.fromCSV(line);
                users.put(user.getUsername(), user);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error: File not found: " + filename);
        }
        return users;
    }

    private void checkLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Data is null");
            messageLabel.setForeground(Color.RED);
        } else if (users.containsKey(username) && users.get(username).getPassword().equals(password)) {
            dispose(); // Close the login frame
            new DiceGame(users.get(username)); // Pass the logged-in user to the DiceGame class
        } else {
            messageLabel.setText("Information is incorrect");
            messageLabel.setForeground(Color.RED);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginForm();
            }
        });
    }
}
