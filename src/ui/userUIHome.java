package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;


public class userUIHome extends JFrame {

    ArrayList<String> comp;

    String SQLQuery;
    String[] defaultComp = {"Compare"};
    String[] defaultCond = {"Type"};
    String[] songsComp = {"Compare", "is", "isn't", ""};
    String[] songsCond = {"Type", "Artist", "Genre", "Publisher"};
    String[] plComp = {"Compare", "is"};
    String[] plCond = {"Type", "Creator"};
    String[] artistsComp = {"Compare", "follows"};
    String[] artistsCond = {"Type", "Artist"};
    String[] billsComp = {"Compare", "is", "on", "before", "after"};
    String[] billsCond = {"Type", "User", "Date"};
    private JComboBox<String> condition;
    private JComboBox<String> comparator;
    private JPanel resultPanel;
    private JPanel queryPanel;
    private JTextField searchField;
    private JPanel content;



    public userUIHome() {
        setLayout(new BorderLayout());
        JPanel sidebar = createSideBar();

        // Create a JPanel to hold the main content
        this.content = createDefaultContent();

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

    private void addPlaceHolderValueToSearchField() {
        searchField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals("Check for")) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK); // Change the text color to black
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Check for");
                    searchField.setForeground(Color.GRAY); // Change the text color back to gray
                }
            }
        });
    }

    private void addActionListeners(JButton playlistsButton, JButton songsButton, JButton artistsButton, JButton paymentsButton) {
        playlistsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] playlistOptions = {"Playlist 1", "Playlist 2", "Playlist 3"};
                condition.setModel(new DefaultComboBoxModel<>(playlistOptions));
                String[] playlistComparators = {"Playlist 1", "Playlist 2", "Playlist 3"};
                comparator.setModel(new DefaultComboBoxModel<>(playlistComparators));
            }
        });

        songsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] songOptions = {"Song A", "Song B", "Song C"};
                condition.setModel(new DefaultComboBoxModel<>(songOptions));
                String[] songComparators = {"Playlist 1", "Playlist 2", "Playlist 3"};
                comparator.setModel(new DefaultComboBoxModel<>(songComparators));
            }
        });

        artistsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] artistOptions = {"Artist X", "Artist Y", "Artist Z"};
                condition.setModel(new DefaultComboBoxModel<>(artistOptions));
                String[] artistComparators = {"Playlist 1", "Playlist 2", "Playlist 3"};
                comparator.setModel(new DefaultComboBoxModel<>(artistComparators));
            }
        });

        paymentsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handlePaymentScreen();
            }
        });
    }

    private void handlePaymentScreen() {
        remove(this.content);
        this.content = new userPaymentsPage().createPaymentContent();
        add(this.content, BorderLayout.CENTER);
        revalidate();
        repaint();

    }

    private JPanel createSideBar(){
        JPanel sidebar = new JPanel();
        sidebar.setBackground(Color.GRAY);
        sidebar.setPreferredSize(new Dimension(150, getHeight()));

        // Create the buttons and add them to the sidebar
        JButton playlistsButton = new JButton("Playlists");
        JButton songsButton = new JButton("Songs");
        JButton artistsButton = new JButton("Artists");
        JButton paymentsButton = new JButton("Payments");
        sidebar.add(playlistsButton);
        sidebar.add(songsButton);
        sidebar.add(artistsButton);
        sidebar.add(paymentsButton);
        addActionListeners(playlistsButton, songsButton, artistsButton, paymentsButton);
        return sidebar;
    }

   private JPanel createDefaultContent(){
        JPanel content = new JPanel();
        content.setLayout(new BorderLayout());
        addDropDowns();
        addSearchFiled();
        JPanel dropdownPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        dropdownPanel.add(condition);
        dropdownPanel.add(comparator);
        dropdownPanel.add(searchField);
        JPanel bottomPanel = createDefaultBottomPanel();
        JPanel nestedPanels = new JPanel(new BorderLayout());
        nestedPanels.add(dropdownPanel, BorderLayout.NORTH);
        nestedPanels.add(bottomPanel, BorderLayout.CENTER);
        content.add(nestedPanels, BorderLayout.CENTER);
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        return content;
    }

    private JPanel createDefaultBottomPanel() {
        JPanel bottomPanel = new JPanel();
        resultPanel = new JPanel();
        resultPanel.setBackground(Color.WHITE);
        resultPanel.setPreferredSize(new Dimension(375, 200));
        queryPanel = new JPanel();
        queryPanel.setBackground(Color.WHITE);
        queryPanel.setPreferredSize(new Dimension(375, 50));
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setPreferredSize(new Dimension(800, 400));
        bottomPanel.add(Box.createVerticalStrut(20));
        bottomPanel.add(resultPanel);
        bottomPanel.add(Box.createVerticalStrut(20));
        bottomPanel.add(queryPanel);
        return bottomPanel;
    }

    private void addDropDowns() {
        this.condition = new JComboBox<>();
        this.comparator = new JComboBox<>();
        String[] playlistOptions = {"Playlist 1", "Playlist 2", "Playlist 3"};
        condition.setModel(new DefaultComboBoxModel<>(playlistOptions));
        String[] playlistComparators = {"Playlist 1", "Playlist 2", "Playlist 3"};
        comparator.setModel(new DefaultComboBoxModel<>(playlistComparators));
    }

    private void addSearchFiled() {
        searchField = new JTextField("Check for");
        searchField.setPreferredSize(new Dimension(150, 25)); // Set the size of the text field
        searchField.setForeground(Color.GRAY);
        addPlaceHolderValueToSearchField();
    }
}



