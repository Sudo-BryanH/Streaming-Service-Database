package ui.panels;

import ui.MainUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;

public class QueryPanel extends ContentPanel {

    private JComboBox<String> entity;
    private JComboBox<String> condition;
    private JComboBox<String> comparator;


    // not sure what these are used for, but i will put them here for now :)
    private ArrayList<String> comp;
    private String SQLQuery;
    private String[] defaultComp = {"Compare"};
    private String[] defaultCond = {"Type"};
    private String[] songsComp = {"Compare", "is", "isn't", ""};
    private String[] songsCond = {"Type", "Artist", "Genre", "Publisher"};
    private String[] plComp = {"Compare", "is"};
    private String[] plCond = {"Type", "Creator"};
    private String[] artistsComp = {"Compare", "follows"};
    private String[] artistsCond = {"Type", "Artist"};
    private String[] billsComp = {"Compare", "is", "on", "before", "after"};
    private String[] billsCond = {"Type", "User", "Date"};
    private JPanel queryPanel;
    private JTextField searchField;



    public QueryPanel(MainUI mainUI) {
        super(mainUI);
    }

    @Override
    protected void generate(){
        setLayout(new BorderLayout());
        addSearchFiled();
        JPanel dropdownPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addDropDowns(dropdownPanel);
        JPanel bottomPanel = createDefaultBottomPanel();
        JPanel nestedPanels = new JPanel(new BorderLayout());
        nestedPanels.add(dropdownPanel, BorderLayout.NORTH);
        nestedPanels.add(bottomPanel, BorderLayout.CENTER);
        add(nestedPanels, BorderLayout.CENTER);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }

    private JPanel createDefaultBottomPanel() {
        JPanel bottomPanel = new JPanel();
        JPanel resultPanel = new JPanel();
        resultPanel.setBackground(Color.WHITE);
        resultPanel.setPreferredSize(new Dimension(375, 200));
        JPanel queryPanel = new JPanel();
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

    private void addDropDowns(JPanel dropdownPanel) {
        String[] entityOptions = {"Playlists", "Songs", "Artists"};
        entity = new JComboBox<>();
        entity.setModel(new DefaultComboBoxModel<>(entityOptions));
        entity.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedValue = (String) entity.getSelectedItem();
                supposedlySacrilegiousSwitchOnOption(selectedValue);
            }
        });
        String[] playlistOptions = {"Playlist 1", "Playlist 2", "Playlist 3"};
        condition = new JComboBox<>();
        condition.setModel(new DefaultComboBoxModel<>(playlistOptions));
        String[] playlistComparators = {"Playlist 1", "Playlist 2", "Playlist 3"};
        comparator = new JComboBox<>();
        comparator.setModel(new DefaultComboBoxModel<>(playlistComparators));

        dropdownPanel.add(entity);
        dropdownPanel.add(condition);
        dropdownPanel.add(comparator);
        dropdownPanel.add(searchField);
    }

    private void addSearchFiled() {
        searchField = new JTextField("Check for");
        searchField.setPreferredSize(new Dimension(150, 25)); // Set the size of the text field
        searchField.setForeground(Color.GRAY);
        addPlaceHolderValueToSearchField();
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

    // this method exists due to hasty refactoring, change later as needed
    private void supposedlySacrilegiousSwitchOnOption(String option) {
        switch (option) {
            case "Playlists":
                String[] playlistOptions = {"Playlist 1", "Playlist 2", "Playlist 3"};
                condition.setModel(new DefaultComboBoxModel<>(playlistOptions));
                String[] playlistComparators = {"Playlist 1", "Playlist 2", "Playlist 3"};
                comparator.setModel(new DefaultComboBoxModel<>(playlistComparators));
                break;
            case "Songs":
                String[] songOptions = {"Song A", "Song B", "Song C"};
                condition.setModel(new DefaultComboBoxModel<>(songOptions));
                String[] songComparators = {"Playlist 1", "Playlist 2", "Playlist 3"};
                comparator.setModel(new DefaultComboBoxModel<>(songComparators));
                break;
            case "Artists":
                String[] artistOptions = {"Artist X", "Artist Y", "Artist Z"};
                condition.setModel(new DefaultComboBoxModel<>(artistOptions));
                String[] artistComparators = {"Playlist 1", "Playlist 2", "Playlist 3"};
                comparator.setModel(new DefaultComboBoxModel<>(artistComparators));
        }
    }
}
