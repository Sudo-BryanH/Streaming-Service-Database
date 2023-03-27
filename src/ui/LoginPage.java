package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPage extends JFrame {
    private JButton loginButton;
    private JButton registerButton;

    public LoginPage(){
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
        JLabel welcomeLabel = userPaymentsPage.createBoldedTitle("SoundHive");
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel loginForm = new JPanel(new GridLayout(2,4,8,10));
        loginForm.setPreferredSize(new Dimension(200,80));
        loginForm.setMaximumSize(new Dimension(200,80));
        JLabel usernameLabel = new JLabel("username:");
        JTextField usernameField = new JTextField(20);
        usernameField.setMaximumSize(new Dimension(200,10));
        usernameField.setPreferredSize(new Dimension(200,10));
        JLabel passwordLabel = new JLabel("password:");
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setMaximumSize(new Dimension(200,10));
        passwordField.setPreferredSize(new Dimension(200,10));
        loginForm.add(usernameLabel);
        loginForm.add(usernameField);
        loginForm.add(passwordLabel);
        loginForm.add(passwordField);
        JPanel buttonPanel = new JPanel(new FlowLayout());
        this.loginButton = new JButton("Login");
        this.registerButton = new JButton("Register?");
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        loginPanel.add(Box.createVerticalStrut(30));
        loginPanel.add(welcomeLabel);
        loginPanel.add(Box.createVerticalStrut(10));
        loginPanel.add(loginForm);
        loginPanel.add(buttonPanel);
        return loginPanel;
    }

    private void attachActionListeners(){
        this.loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                JFrame homepage = new userUIHome();
                homepage.setVisible(true);
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
