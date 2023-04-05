package ui;

import model.User;
import ui.login.LoginPage;
import ui.panels.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class MainUI extends JFrame {
    private JPanel content;
    private User user;
    private boolean admin;

    public MainUI(User user) {
        this.user = user;
        this.admin = this.user.getUsername().equals("admin");

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

    public User getUser(){
        return user;
    }

    public boolean isAdmin(){
        return admin;
    }

    private JPanel createSideBar(){
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(Color.GRAY);
        sidebar.setPreferredSize(new Dimension(150, getHeight()));

        List<JButton> regularButtons = addRegularButtons(sidebar);
        List<JButton> adminButtons = addAdminButtons(sidebar);
        addLogoutButton(sidebar);

        return sidebar;
    }

    private List<JButton> addRegularButtons(JPanel sidebar) {
        List<JButton> regularButtons = new ArrayList<>();

        regularButtons.add(new JButton("Home"));
        regularButtons.add(new JButton("Library"));
        regularButtons.add(new JButton("Search"));
        regularButtons.add(new JButton("Payments"));
        regularButtons.add(new JButton("Records"));

        regularButtons.get(0).addActionListener(e -> swapPanel(new HomePanel(this)));
        regularButtons.get(1).addActionListener(e -> swapPanel(new LibraryPanel(this)));
        regularButtons.get(2).addActionListener(e -> swapPanel(new SearchPanel(this)));
        regularButtons.get(3).addActionListener(e -> {
            if (admin){
                swapPanel(new AdminPaymentsPanel(this));
            }
            else {
                swapPanel(new UserPaymentsPanel(this));
            }
        });
        regularButtons.get(4).addActionListener(e -> swapPanel(new RecordsPanel(this)));

        for (JButton button : regularButtons) {
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            sidebar.add(button);
        }
        return regularButtons;
    }

    private List<JButton> addAdminButtons(JPanel sidebar) {
        List<JButton> adminButtons = new ArrayList<>();

        if (admin) {
            adminButtons.add(new JButton("Query"));
            adminButtons.add(new JButton("Releases"));
            adminButtons.add(new JButton("Artists"));
            adminButtons.add(new JButton("Distributors"));

            adminButtons.get(0).addActionListener(e -> swapPanel(new QueryPanel(this)));
            adminButtons.get(1).addActionListener(e -> swapPanel(new ReleasesAdminPanel(this)));
            adminButtons.get(2).addActionListener(e -> swapPanel(new ArtistsAdminPanel(this)));
            adminButtons.get(3).addActionListener(e -> swapPanel(new DistributorsAdminPanel(this)));

            sidebar.add(Box.createVerticalStrut(20));
            for (JButton button : adminButtons) {
                button.setAlignmentX(Component.CENTER_ALIGNMENT);
                sidebar.add(button);
            }
        }

        return adminButtons;
    }

    private void addLogoutButton(JPanel sidebar) {
        JButton logoutButton = new JButton("Logout");
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutButton.addActionListener(e -> {
            dispose();
            JFrame loginFrame = new LoginPage();
            loginFrame.setVisible(true);
        });
        sidebar.add(Box.createVerticalStrut(20));
        sidebar.add(logoutButton);
    }

    public void swapPanel(ContentPanel newPanel) {
        remove(this.content);
        this.content = newPanel;
        add(this.content, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
}



