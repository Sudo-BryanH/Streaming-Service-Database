package ui.panels;

import backend.ReleaseEndpoints;
import model.Release;
import ui.MainUI;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.List;

public class HomePanel extends ContentPanel{
    private String username;

    public HomePanel(MainUI mainUI) {
        super(mainUI);
    }

    @Override
    protected void generate() {
        this.username = this.mainUI.getUser().getUsername();

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(createTitle("Home"), BorderLayout.NORTH);

        headerPanel.add(createSubtitle("Welcome back, " + username), BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        JPanel suggestionsPanel = new JPanel();
        suggestionsPanel.setLayout(new BoxLayout(suggestionsPanel, BoxLayout.Y_AXIS));
        suggestionsPanel.add(new JSeparator()); // spacer
        suggestionsPanel.add(getLibrarySuggestions());
        suggestionsPanel.add(new JSeparator()); // spacer
        suggestionsPanel.add(getRandomSuggestions());
        add(suggestionsPanel, BorderLayout.WEST);
    }

    protected JLabel createSuggestionsTitle(String title) {
        JLabel label = new JLabel(title);
        Font font = label.getFont();

        label.setFont( new Font(font.getFontName(), font.getStyle(), 20));
        label.setPreferredSize(new Dimension(5,40));

        return label;
    }

    private JPanel getLibrarySuggestions() {
        JPanel librarySuggestions = new JPanel(new BorderLayout());
        librarySuggestions.add(createSuggestionsTitle("Start off with picks from your library:"), BorderLayout.NORTH);

        JPanel suggestionContents = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        suggestionContents.setPreferredSize(new Dimension(750, 50));
        populateSuggestions(suggestionContents, ReleaseEndpoints.getReleasesByUser(username));
        librarySuggestions.add(new JScrollPane(suggestionContents));

        return librarySuggestions;
    }

    private JPanel getRandomSuggestions() {
        JPanel randomSuggestions = new JPanel(new BorderLayout());
        randomSuggestions.add(createSuggestionsTitle("SoundHive's suggestions:"), BorderLayout.NORTH);

        JPanel suggestionContents = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        suggestionContents.setPreferredSize(new Dimension(750, 50));

        populateSuggestions(suggestionContents, ReleaseEndpoints.getReleases());
        randomSuggestions.add(new JScrollPane(suggestionContents));

        return randomSuggestions;
    }

    private void populateSuggestions(JPanel outerPanel, List<Release> releases) {
        Collections.shuffle(releases);
        int length = Math.min(releases.size(), 12);

        for (Release release : releases.subList(0, length)) {
            outerPanel.add(getSuggestionPanel(release));
        }
    }

    private JPanel getSuggestionPanel(Release release) {
        JPanel result = new JPanel();
        result.setLayout(new BoxLayout(result, BoxLayout.Y_AXIS));
        result.setPreferredSize(new Dimension(100,100));

        JLabel nameLabel = new JLabel(release.name);
        nameLabel.setPreferredSize(new Dimension(20, 35));
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel artistLabel = new JLabel(release.getArtistNames());
        artistLabel.setPreferredSize(new Dimension(20, 35));
        artistLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JButton viewButton = new JButton("View");
        viewButton.addActionListener(e -> mainUI.swapPanel(new ReleasePanel(mainUI, release)));

        result.add(nameLabel);
        result.add(artistLabel);
        result.add(viewButton);

        return result;
    }
}
