package ui.panels;

import backend.ArtistEndpoints;
import backend.SongEndpoints;
import model.Release;
import model.Song;
import ui.MainUI;
import util.Misc;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ReleaseSongsAdminPanel extends ContentPanel{
    private JPanel infoPanel;
    private JScrollPane infoScrollPane;
    private Release release;
    public ReleaseSongsAdminPanel(MainUI mainUI, Release release) {
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

        fillSongs();
    }

    private JPanel getButtonsPanel() {
        JPanel buttonsPanel = new JPanel(new BorderLayout());

        JButton addButton = new JButton("Add Songs");
        buttonsPanel.add(addButton, BorderLayout.WEST);
        // TODO ADDDDDDDDDDDDDD

        JButton manageButton = new JButton("Manage Artists");
        buttonsPanel.add(manageButton, BorderLayout.EAST);
        manageButton.addActionListener(e -> mainUI.swapPanel(new ReleaseArtistsAdminPanel(mainUI, release)));

        return buttonsPanel;
    }

    private void fillSongs() {
        List<Song> songs = SongEndpoints.getSongsByRelease(release, mainUI.getUser().getUsername());

        infoPanel.removeAll();
        for (Song song : songs) {
            infoPanel.add(getSongPanel(song));
        }

        revalidate();
        repaint();
    }

    private JPanel getSongPanel(Song song) {
        String username = mainUI.getUser().getUsername();
        JPanel result = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        Dimension miniButtonSize = new Dimension(25,20);

        JLabel numLabel = new JLabel(Integer.toString(song.trackNum));
        numLabel.setPreferredSize(new Dimension(25,20));
        result.add(numLabel);

        JLabel nameLabel = new JLabel(song.name);
        nameLabel.setPreferredSize(new Dimension(240,20));
        result.add(nameLabel);

        JLabel artistLabel = new JLabel(Misc.artistsToString(ArtistEndpoints.getArtistsBySong(song)));
        artistLabel.setPreferredSize(new Dimension(200,20));
        result.add(artistLabel);

        JLabel genreLabel = new JLabel(song.genre);
        genreLabel.setPreferredSize(new Dimension(90,20));
        result.add(genreLabel);

        JLabel durationLabel = new JLabel(Misc.secondsToFormatted(song.duration));
        durationLabel.setPreferredSize(new Dimension(40,20));
        result.add(durationLabel);

        JButton editButton = new JButton("✎");
        editButton.setPreferredSize(miniButtonSize);
        // TODO add listener
        result.add(editButton);

        JButton deleteButton = new JButton("✕");
        deleteButton.setPreferredSize(miniButtonSize);
        deleteButton.addActionListener(e -> {
            SongEndpoints.removeSong(song);
            fillSongs();
        });
        result.add(deleteButton);

        return result;
    }
}
