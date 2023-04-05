package ui.panels;

import backend.ArtistEndpoints;
import model.Artist;
import ui.MainUI;
import util.Misc;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ArtistsAdminPanel extends ContentPanel {
    private String queryString;
    private JPanel resultsPanel;
    private JScrollPane resultsScrollPane;
    public ArtistsAdminPanel(MainUI mainUI) {
        super(mainUI);
    }


    private interface FunctionCallback {
        void apply(Artist artist);
    }

    @Override
    protected void generate() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(createTitle("Manage Artists"), BorderLayout.NORTH);
        headerPanel.add(getSearchBarPanel(), BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        createResultsScrollPane();
        add(resultsScrollPane, BorderLayout.WEST);

        performSearch("");
    }

    private void performSearch(String query) {
        queryString = query;
        List<String> combinations = Misc.stringCombinations(query);

        List<Artist> results = new ArrayList<>();
        for (String c : combinations) {
            List<Artist> newResults = ArtistEndpoints.searchArtists(c);
            mergeLists(results, newResults);
        }

        if (results.size() > 0) {
            resultsPanel.removeAll();
            for (Artist d : results) {
                resultsPanel.add(getResultPanel(d));
            }

            resultsScrollPane.setVisible(true);
        } else {
            resultsScrollPane.setVisible(false);
        }

        revalidate();
        repaint();
    }

    private JPanel getResultPanel(Artist artist) {
        JPanel result = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        Dimension miniButtonSize = new Dimension(30,20);

        JLabel idLabel = new JLabel(Integer.toString(artist.id));
        idLabel.setPreferredSize(new Dimension(40,20));
        result.add(idLabel);

        JLabel nameLabel = new JLabel(artist.name);
        nameLabel.setPreferredSize(new Dimension(650,20));
        result.add(nameLabel);

        JButton deleteButton = new JButton("✕");
        deleteButton.setPreferredSize(miniButtonSize);
        deleteButton.addActionListener(e -> {
            ArtistEndpoints.removeArtist(artist);
            performSearch(queryString);
        });
        result.add(deleteButton);

        return result;
    }

    private JPanel getSearchBarPanel() {
        JPanel searchBarPanel = new JPanel(new FlowLayout());

        JTextField searchField = new JTextField("");
        searchField.setPreferredSize(new Dimension(575, 25));
        searchField.setForeground(Color.BLACK);
        searchBarPanel.add(searchField);

        JButton enterButton = new JButton("Search");
        enterButton.addActionListener(e -> performSearch(searchField.getText()));
        searchBarPanel.add(enterButton);

        JButton addButton = new JButton("Add Artist");
        addButton.addActionListener(e -> infoPrompt(null, this::add));
        searchBarPanel.add(addButton);

        return searchBarPanel;
    }

    private void createResultsScrollPane() {
        resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));

        resultsScrollPane = new JScrollPane(resultsPanel);
        resultsScrollPane.setVisible(false);
    }

    private void add(Artist artist) {
        try {
            ArtistEndpoints.addArtist(artist);

            Artist check = ArtistEndpoints.getArtistByID(artist.id);
            if (check != null && check.id == artist.id && check.name.equals(artist.name)){
                JOptionPane.showMessageDialog(this, "Artist '" + artist.name + "' added!");
                performSearch(queryString);
            } else {
                JOptionPane.showMessageDialog(this, "Artist add failed :(");
            }
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(this, "Artist add failed :(");
        }
    }

    private void infoPrompt(Artist artist, FunctionCallback callback) {
        JFrame infoFrame = new JFrame("Artist Info");
        infoFrame.setSize(new Dimension(450,150));
        infoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        infoFrame.setVisible(true);

        JPanel infoContent = new JPanel(new GridLayout(3,2,10,10));

        JLabel idLabel = new JLabel("ID:");
        JLabel nameLabel = new JLabel("Name:");

        JTextField idField = new JTextField(20);
        JTextField nameField = new JTextField(20);

        if (artist != null) {
            idField.setText(Integer.toString(artist.id));
            idField.setEnabled(false);
            nameField.setText(artist.name);
        }

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                String name = nameField.getText();

                callback.apply(new Artist(id, name));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Info parsing failed :(\n" + ex.getMessage());
            }
            infoFrame.dispose();
        });

        infoContent.add(idLabel);
        infoContent.add(idField);
        infoContent.add(nameLabel);
        infoContent.add(nameField);
        infoContent.add(new JLabel());
        infoContent.add(submitButton);

        infoContent.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        infoFrame.add(infoContent);
    }

    public static void mergeLists(List<Artist> target, List<Artist> addition) {
        for (Artist a : addition) {
            if (!target.contains(a)) {
                target.add(a);
            }
        }
    }
}
