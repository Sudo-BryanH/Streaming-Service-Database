package ui.panels;

import backend.ReleaseEndpoints;
import model.Release;
import ui.MainUI;
import util.Misc;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchPanel extends ContentPanel{
    private JPanel resultsPanel;
    private JScrollPane resultsScrollPane;
    public SearchPanel(MainUI mainUI) {
        super(mainUI);
    }

    @Override
    protected void generate() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(createTitle("Search"), BorderLayout.NORTH);
        headerPanel.add(getSearchBarPanel(), BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        createResultsScrollPane();
        add(resultsScrollPane, BorderLayout.WEST);
    }

    private void performSearch(String query) {
        String[] words = query.split("\\s+");

        List<String> combinations = new ArrayList<>();
        for (int i = 0; i < words.length; i++) {
            for (int j = i + 1; j <= words.length; j++) {
                combinations.add(String.join(" ", Arrays.copyOfRange(words, i, j)));
            }
        }

        List<Release> results = new ArrayList<>();
        for (String c : combinations) {
            List<Release> newResults = ReleaseEndpoints.searchReleases(c);
            Misc.mergeReleaseLists(results, newResults);
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

        JLabel nameLabel = new JLabel(release.name);
        nameLabel.setPreferredSize(new Dimension(325,20));
        result.add(nameLabel);

        JLabel artistLabel = new JLabel(release.getArtistNames());
        artistLabel.setPreferredSize(new Dimension(325,20));
        result.add(artistLabel);

        JButton viewButton = new JButton("View");
        viewButton.addActionListener(e -> mainUI.swapPanel(new ReleasePanel(mainUI, release)));
        result.add(viewButton);

        return result;
    }

    private JPanel getSearchBarPanel() {
        JPanel searchBarPanel = new JPanel(new FlowLayout());

        JTextField searchField = new JTextField("");
        searchField.setPreferredSize(new Dimension(680, 25));
        searchField.setForeground(Color.BLACK);
        searchBarPanel.add(searchField);

        JButton enterButton = new JButton("Enter");
        enterButton.addActionListener(e -> performSearch(searchField.getText()));
        searchBarPanel.add(enterButton);

        return searchBarPanel;
    }

    private void createResultsScrollPane() {
        resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));

        resultsScrollPane = new JScrollPane(resultsPanel);
        resultsScrollPane.setVisible(false);
    }
}
