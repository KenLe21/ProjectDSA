import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class Register {
    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton registerButton;
    private UserManager userManager;
    public Register() {
        userManager = new UserManager();
        frame = new JFrame("Register");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 250);
        frame.setLayout(new GridLayout(3, 2));

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        registerButton = new JButton("Register");

        frame.add(usernameLabel);
        frame.add(usernameField);
        frame.add(passwordLabel);
        frame.add(passwordField);
        frame.add(new JLabel());
        frame.add(registerButton);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                if (!username.isEmpty() && !password.isEmpty()) {
                    userManager.addUser(new User(username, password));
                    JOptionPane.showMessageDialog(frame, "User registered successfully!");
                    usernameField.setText("");
                    passwordField.setText("");
                    openLoginForm();
                } else {
                    JOptionPane.showMessageDialog(frame, "Please enter both username and password.");
                }
            }
        });
        frame.setVisible(true);
    }
    private void openLoginForm() {
        frame.dispose();
        new LoginForm();
    }
}
