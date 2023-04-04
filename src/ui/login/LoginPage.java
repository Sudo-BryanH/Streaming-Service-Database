package ui.login;

import backend.LoginRegistrationEndpoints;
import backend.PaymentEndpoints;
import model.User;
import ui.MainUI;
import ui.panels.UserPaymentsPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPage extends JFrame {
    private JButton loginButton;
    private JButton registerButton;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginPage() {
        JPanel loginPanel = createLoginPanel();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(loginPanel);
        setSize(400, 400);
        setLocationRelativeTo(null);
        setVisible(true);
        attachActionListeners();
    }

    private JPanel createLoginPanel() {
        JPanel loginPanel = new JPanel();
        BoxLayout layout = new BoxLayout(loginPanel, BoxLayout.Y_AXIS);
        loginPanel.setLayout(layout);
        JLabel welcomeLabel = UserPaymentsPanel.createBoldedTitle("SoundHive");
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel loginForm = new JPanel(new GridLayout(2, 4, 8, 10));
        loginForm.setPreferredSize(new Dimension(200, 80));
        loginForm.setMaximumSize(new Dimension(200, 80));
        JLabel usernameLabel = new JLabel("username:");
        usernameField = new JTextField(20);
        usernameField.setMaximumSize(new Dimension(200, 10));
        usernameField.setPreferredSize(new Dimension(200, 10));
        JLabel passwordLabel = new JLabel("password:");
        passwordField = new JPasswordField(20);
        passwordField.setMaximumSize(new Dimension(200, 10));
        passwordField.setPreferredSize(new Dimension(200, 10));
        loginForm.add(usernameLabel);
        loginForm.add(usernameField);
        loginForm.add(passwordLabel);
        loginForm.add(passwordField);
        JPanel buttonPanel = new JPanel(new FlowLayout());
        this.loginButton = new JButton("Login");
        this.registerButton = new JButton("Register?");
        buttonPanel.add(registerButton);
        buttonPanel.add(loginButton);
        loginPanel.add(Box.createVerticalStrut(30));
        loginPanel.add(welcomeLabel);
        loginPanel.add(Box.createVerticalStrut(10));
        loginPanel.add(loginForm);
        loginPanel.add(buttonPanel);
        return loginPanel;
    }

    private void attachActionListeners() {
        this.loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (LoginRegistrationEndpoints.login(username, password)) {
                dispose();
                User user = new User(username);
                MainUI homepage = new MainUI(user);
                user.setPremium(PaymentEndpoints.getPremiumStatus(user));
                homepage.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Login Failed");
            }
        });
        this.registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                JFrame registerFrame = new RegistrationPage();
                registerFrame.setVisible(true);
            }
        });
    }
}
