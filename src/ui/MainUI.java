package ui;

import model.User;
import ui.login.LoginPage;
import ui.panels.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


public class MainUI extends JFrame {
    private JPanel content;
    private User user;

    public MainUI() {
        setLayout(new BorderLayout());
        JPanel sidebar = createSideBar();

        // Create a JPanel to hold the main content
        this.content = new HomePanel(this);

        // Add the sidebar to the left side of the JFrame
        add(sidebar, BorderLayout.WEST);

        // Add the main content to the center of the JFrame
        add(content, BorderLayout.CENTER);
        // Set JFrame properties
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void setUser(User user){
        this.user = user;
    }

    public User getUser(){
        return this.user;
    }

    private JPanel createSideBar(){
        JPanel sidebar = new JPanel();
        sidebar.setBackground(Color.GRAY);
        sidebar.setPreferredSize(new Dimension(150, getHeight()));

        // Create the buttons and add them to the sidebar
        ArrayList<JButton> buttons = new ArrayList<>();
        buttons.add(new JButton("Home"));
        buttons.add(new JButton("Library"));
        buttons.add(new JButton("Search"));
        buttons.add(new JButton("Payments"));
        buttons.add(new JButton("Query"));
        buttons.add(new JButton("Logout"));

        for (JButton button : buttons) {
            sidebar.add(button);
        }

        addActionListeners(buttons);
        return sidebar;
    }

    public void swapPanel(ContentPanel newPanel) {
        remove(this.content);
        this.content = newPanel;
        add(this.content, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    // should refactor later, this just exists as is right now for convenience
    private void addActionListeners(ArrayList<JButton> buttons) {
        MainUI thisReference = this;
        buttons.get(0).addActionListener(e -> swapPanel(new HomePanel(thisReference)));
        buttons.get(1).addActionListener(e -> swapPanel(new LibraryPanel(thisReference)));
        buttons.get(2).addActionListener(e -> swapPanel(new SearchPanel(thisReference)));
        buttons.get(3).addActionListener(e -> {
            if(this.user.getUsername().equals("dhk")){
                swapPanel(new AdminPaymentsPanel(thisReference));
            }
            else {
                swapPanel(new UserPaymentsPanel(thisReference));
            }
        });
        buttons.get(4).addActionListener(e -> swapPanel(new QueryPanel(thisReference)));
        buttons.get(5).addActionListener(e -> {
            dispose();
            JFrame loginFrame = new LoginPage();
            loginFrame.setVisible(true);
        });
    }

}



