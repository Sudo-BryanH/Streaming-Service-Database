package ui.panels;

import backend.ArtistEndpoints;
import backend.ReleaseEndpoints;
import model.Artist;
import model.Release;
import ui.MainUI;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class ReleaseArtistsAdminPanel extends ContentPanel{
    private JPanel infoPanel;
    private JScrollPane infoScrollPane;
    private Release release;
    public ReleaseArtistsAdminPanel(MainUI mainUI, Release release) {
        super(mainUI);
        this.release = release;
        generateCustom(); // man sometimes i would like to do things before the super ;-;
    }

    @Override
    protected void generate() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }

    private void generateCustom() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(createTitle("Manage: " + release.name), BorderLayout.NORTH);
        headerPanel.add(getButtonsPanel(), BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoScrollPane = new JScrollPane(infoPanel);
        add(infoScrollPane, BorderLayout.WEST);

        fillArtists();
    }

    private JPanel getButtonsPanel() {
        JPanel buttonsPanel = new JPanel(new BorderLayout());

        JButton addButton = new JButton("Add Artists");
        buttonsPanel.add(addButton, BorderLayout.WEST);
        addButton.addActionListener(e -> artistInfoPrompt());


        JButton manageButton = new JButton("Manage Songs");
        buttonsPanel.add(manageButton, BorderLayout.EAST);
        manageButton.addActionListener(e -> mainUI.swapPanel(new ReleaseSongsAdminPanel(mainUI, release)));

        return buttonsPanel;
    }

    private void fillArtists() {
        List<Artist> artists = ArtistEndpoints.getArtistsByRelease(release.id);

        infoPanel.removeAll();
        for (Artist artist : artists) {
            infoPanel.add(getArtistPanel(artist));
        }

        revalidate();
        repaint();
    }

    private JPanel getArtistPanel(Artist artist) {
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
            ReleaseEndpoints.removeCreates(release, artist);
            fillArtists();
        });
        result.add(deleteButton);

        return result;
    }

    private void artistInfoPrompt() {
        Artist[] artists = ArtistEndpoints.getArtistsNotInRelease(release).toArray(new Artist[0]);
        if (artists.length == 0) {
            JOptionPane.showMessageDialog(this, "No artists to add!");
            return;
        }

        JFrame infoFrame = new JFrame("Select Artist");
        infoFrame.setSize(new Dimension(225,110));
        infoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        infoFrame.setVisible(true);

        JPanel infoContent = new JPanel(new GridLayout(2,1,10,10));
        infoContent.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        infoFrame.add(infoContent);

        String[] artistNames = Arrays.stream(artists).map(n -> n.id + " " + n.name).toArray(String[]::new);
        JComboBox selector = new JComboBox<>(artistNames);
        infoContent.add(selector);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            int artistID = Integer.parseInt(selector.getSelectedItem().toString().split(" ")[0]);
            addArtist(ArtistEndpoints.getArtistByID(artistID));
            infoFrame.dispose();
        });
        infoContent.add(submitButton);
    }

    private void addArtist(Artist artist) {
        try {
            ReleaseEndpoints.addCreates(release, artist);
            JOptionPane.showMessageDialog(this, "Artist '" + artist.name + "' added!");
            fillArtists();
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(this, "Artist add failed :(");
        }
    }
}
