package ui.panels;

import backend.ReleaseEndpoints;
import model.Release;
import ui.MainUI;
import util.Misc;

import javax.swing.*;
import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ReleasesAdminPanel extends ContentPanel{
    private static final DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private String queryString;
    private JPanel resultsPanel;
    private JScrollPane resultsScrollPane;
    public ReleasesAdminPanel(MainUI mainUI) {
        super(mainUI);
    }


    private interface FunctionCallback {
        void apply(Release release);
    }

    @Override
    protected void generate() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(createTitle("Manage Releases"), BorderLayout.NORTH);
        headerPanel.add(getSearchBarPanel(), BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        createResultsScrollPane();
        add(resultsScrollPane, BorderLayout.WEST);

        performSearch("");
    }

    private void performSearch(String query) {
        queryString = query;
        List<String> combinations = Misc.stringCombinations(query);

        List<Release> results = new ArrayList<>();
        for (String c : combinations) {
            List<Release> newResults = ReleaseEndpoints.searchReleases(c);
            mergeLists(results, newResults);
        }

        if (results.size() > 0) {
            resultsPanel.removeAll();
            for (Release release : results) {
                resultsPanel.add(getResultPanel(release));
            }

            resultsScrollPane.setVisible(true);
        } else {
            resultsScrollPane.setVisible(false);
        }

        revalidate();
        repaint();
    }

    private JPanel getResultPanel(Release release) {
        JPanel result = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        Dimension miniButtonSize = new Dimension(30,20);

        JLabel idLabel = new JLabel(String.valueOf(release.id));
        idLabel.setPreferredSize(new Dimension(30,20));
        result.add(idLabel);

        JLabel nameLabel = new JLabel(release.name);
        nameLabel.setPreferredSize(new Dimension(325,20));
        result.add(nameLabel);

        JLabel artistLabel = new JLabel(release.getArtistNames());
        artistLabel.setPreferredSize(new Dimension(250,20));
        result.add(artistLabel);

        JButton songsButton = new JButton("Manage");
        songsButton.setPreferredSize(new Dimension(60,20));
        songsButton.addActionListener(e -> mainUI.swapPanel(new ReleaseSongsAdminPanel(mainUI, release)));
        result.add(songsButton);

        JButton editButton = new JButton("✎");
        editButton.setPreferredSize(miniButtonSize);
        editButton.addActionListener(e -> infoPrompt(release, this::edit));
        result.add(editButton);

        JButton deleteButton = new JButton("✕");
        deleteButton.setPreferredSize(miniButtonSize);
        deleteButton.addActionListener(e -> {
            ReleaseEndpoints.removeRelease(release);
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

        JButton addButton = new JButton("Add Release");
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

    private void add(Release release) {
        try {
            ReleaseEndpoints.addRelease(release);

            Release check = ReleaseEndpoints.getReleaseByID(release.id);
            if (check != null && check.id == release.id){
                JOptionPane.showMessageDialog(this, "Release '" + release.name + "' added!");
                performSearch(queryString);
            } else {
                JOptionPane.showMessageDialog(this, "Release add failed :(");
            }
        }
        catch (Exception exception){
            JOptionPane.showMessageDialog(this, "Release add failed :(");
        }
    }

    private void edit(Release release) {
        try {
            ReleaseEndpoints.editRelease(release);

            Release check = ReleaseEndpoints.getReleaseByID(release.id);
            if (check != null && check.id == release.id){
                performSearch(queryString);
            } else {
                JOptionPane.showMessageDialog(this,  "Edit failed :(");
            }
        }
        catch (Exception exception){
            JOptionPane.showMessageDialog(this, "Edit failed :(");
        }
    }

    private void infoPrompt(Release release, FunctionCallback callback) {
        JFrame infoFrame = new JFrame("Release Info");
        infoFrame.setSize(new Dimension(450,300));
        infoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        infoFrame.setVisible(true);

        JPanel infoContent = new JPanel(new GridLayout(7,2,10,10));

        JLabel idLabel = new JLabel("ID:");
        JLabel nameLabel = new JLabel("Name:");
        JLabel typeLabel = new JLabel("Type:");
        JLabel dateLabel = new JLabel("Release Date (yyyy-MM-dd):");
        JLabel urlLabel = new JLabel("Artwork Link:");
        JLabel distLabel = new JLabel("Distributor Name:");

        JTextField idField = new JTextField(20);
        JTextField nameField = new JTextField(20);
        JTextField typeField = new JTextField(20);
        JTextField dateField = new JTextField(20);
        JTextField urlField = new JTextField(20);
        JTextField distField = new JTextField(20);

        if (release != null) {
            idField.setText(String.valueOf(release.id));
            idField.setEnabled(false);
            nameField.setText(release.name);
            typeField.setText(release.type);
            dateField.setText(Misc.slashDateToDash(release.releaseDate));
            urlField.setText(release.artUrl);
            distField.setText(release.distributor);
        }

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                String name = nameField.getText();
                String type = typeField.getText();
                String date = dateField.getText();
                String url = urlField.getText();
                String dist = distField.getText();

                callback.apply(new Release(id, name, type, date, url, dist));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Info parsing failed :(\n" + ex.getMessage());
            }

            infoFrame.dispose();
        });

        infoContent.add(idLabel);
        infoContent.add(idField);
        infoContent.add(nameLabel);
        infoContent.add(nameField);
        infoContent.add(typeLabel);
        infoContent.add(typeField);
        infoContent.add(dateLabel);
        infoContent.add(dateField);
        infoContent.add(urlLabel);
        infoContent.add(urlField);
        infoContent.add(distLabel);
        infoContent.add(distField);
        infoContent.add(new JLabel());
        infoContent.add(submitButton);

        infoContent.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        infoFrame.add(infoContent);
    }

    public static void mergeLists(List<Release> target, List<Release> addition) {
        for (Release a : addition) {
            if (!target.contains(a)) {
                target.add(a);
            }
        }
    }
}
