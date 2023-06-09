package ui.login;

import backend.LoginRegistrationEndpoints;
import model.User;
import ui.MainUI;
import ui.panels.UserPaymentsPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegistrationPage extends JFrame {
    private JButton loginButton;
    private JButton registerButton;
    private JTextField usernameField;
    private JTextField emailTextField;
    private JPasswordField passwordField;

    public RegistrationPage(){
        JPanel loginPanel = createRegistrationPanel();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(loginPanel);
        setSize(400, 400);
        setLocationRelativeTo(null);
        setVisible(true);
        attachActionListeners();
    }

    private JPanel createRegistrationPanel() {
        JPanel registrationPanel = new JPanel();
        BoxLayout layout = new BoxLayout(registrationPanel, BoxLayout.Y_AXIS);
        registrationPanel.setLayout(layout);
        JLabel welcomeLabel = UserPaymentsPanel.createBoldedTitle("SoundHive");
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel registrationForm = new JPanel(new GridLayout(3,4,8,10));
        registrationForm.setPreferredSize(new Dimension(200,100));
        registrationForm.setMaximumSize(new Dimension(200,100));
        JLabel usernameLabel = new JLabel("username:");
        usernameField = new JTextField(20);
        usernameField.setMaximumSize(new Dimension(200,10));
        usernameField.setPreferredSize(new Dimension(200,10));
        JLabel emailLabel = new JLabel("email:");
        emailTextField = new JTextField(20);
        emailTextField.setMaximumSize(new Dimension(200,10));
        emailTextField.setPreferredSize(new Dimension(200,10));
        JLabel passwordLabel = new JLabel("password:");
        passwordField = new JPasswordField(20);
        passwordField.setMaximumSize(new Dimension(200,10));
        passwordField.setPreferredSize(new Dimension(200,10));
        registrationForm.add(usernameLabel);
        registrationForm.add(usernameField);
        registrationForm.add(emailLabel);
        registrationForm.add(emailTextField);
        registrationForm.add(passwordLabel);
        registrationForm.add(passwordField);
        JPanel buttonPanel = new JPanel(new FlowLayout());
        this.loginButton = new JButton("Login?");
        this.registerButton = new JButton("Register");
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        registrationPanel.add(Box.createVerticalStrut(30));
        registrationPanel.add(welcomeLabel);
        registrationPanel.add(Box.createVerticalStrut(10));
        registrationPanel.add(registrationForm);
        registrationPanel.add(buttonPanel);
        return registrationPanel;
    }

    private void attachActionListeners(){
        this.loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                JFrame loginFrame = new LoginPage();
                loginFrame.setVisible(true);
            }
        });
        this.registerButton.addActionListener(e-> {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String email = emailTextField.getText();
                if(LoginRegistrationEndpoints.register(username,password,email)){
                    dispose();
                    MainUI homePage = new MainUI(new User(username));
                    homePage.setVisible(true);
                }
                else {
                    JOptionPane.showMessageDialog(this, "Registration Failed");
                }
        });
    }
}
