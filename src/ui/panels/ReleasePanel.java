package ui.panels;

import backend.ArtistEndpoints;
import backend.LibraryEndpoints;
import backend.ReleaseEndpoints;
import model.Release;
import model.Song;
import ui.MainUI;
import util.Misc;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ReleasePanel extends ContentPanel{
    private JPanel songsPanel;
    private JScrollPane songsScrollPane;
    private Release release;
    public ReleasePanel(MainUI mainUI, Release release) {
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
        add(createHeader(), BorderLayout.NORTH);

        createSongsScrollPane();
        add(songsScrollPane, BorderLayout.WEST);

        add(new JLabel("Published by " + release.distributor), BorderLayout.SOUTH);
    }

    private JPanel createHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(createTitle(release.name), BorderLayout.NORTH);
        String releaseInfo = release.type + " by " + release.getArtistNames() + " - " + release.releaseDate;
        headerPanel.add(createSubtitle(releaseInfo), BorderLayout.CENTER);
        headerPanel.add(new JSeparator(), BorderLayout.SOUTH);

        return headerPanel;
    }

    private void fillSongs() {
        List<Song> songs = ReleaseEndpoints.getSongsByRelease(release, mainUI.getUser().getUsername());
        for (Song song : songs) {
            songsPanel.add(getSongPanel(song));
        }
        revalidate();
        repaint();
    }

    private JPanel getSongPanel(Song song) {
        String username = mainUI.getUser().getUsername();
        JPanel result = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        Dimension miniButtonSize = new Dimension(30,20);

        JButton playButton = new JButton("▶");
        playButton.setPreferredSize(miniButtonSize);
        result.add(playButton);

        JLabel numLabel = new JLabel(Integer.toString(song.trackNum));
        numLabel.setPreferredSize(new Dimension(15,20));
        result.add(numLabel);

        JLabel nameLabel = new JLabel(song.name);
        nameLabel.setPreferredSize(new Dimension(200,20));
        result.add(nameLabel);

        JLabel artistLabel = new JLabel(Misc.artistsToString(ArtistEndpoints.getArtistsBySong(song)));
        artistLabel.setPreferredSize(new Dimension(175,20));
        result.add(artistLabel);

        JLabel genreLabel = new JLabel(song.genre);
        genreLabel.setPreferredSize(new Dimension(90,20));
        result.add(genreLabel);

        JLabel durationLabel = new JLabel(Misc.secondsToFormatted(song.duration));
        durationLabel.setPreferredSize(new Dimension(40,20));
        result.add(durationLabel);

        String addButtonLabel = song.added ? "-" : "+";
        JButton addButton = new JButton(addButtonLabel);
        addButton.setPreferredSize(miniButtonSize);
        result.add(addButton);

        JButton downloadButton = new JButton();
        downloadButton.setPreferredSize(miniButtonSize);
        downloadButton.addActionListener(e -> {
            if (song.downloaded) {
                downloadButton.setText("↓");
                LibraryEndpoints.undownloadSong(song, username);
            } else {
                downloadButton.setText("✕");
                LibraryEndpoints.downloadSong(song, username);
            }
            song.downloaded = !song.downloaded;
        });
        result.add(downloadButton);

        JButton likeButton = new JButton();
        likeButton.setPreferredSize(miniButtonSize);
        likeButton.addActionListener(e -> {
            if (song.liked) {
                likeButton.setText("♡");
                LibraryEndpoints.unlikeSong(song, username);
            } else {
                likeButton.setText("♥");
                LibraryEndpoints.likeSong(song, username);
            }
            song.liked = !song.liked;
        });
        result.add(likeButton);

        addButton.addActionListener(e -> {
            if (song.added) {
                addButton.setText("+");
                LibraryEndpoints.deleteLibSong(song, username);
                song.downloaded = false;
                song.liked = false;
            } else {
                addButton.setText("-");
                LibraryEndpoints.addToLibrary(song, username);
            }
            song.added = !song.added;
            updateButtons(song, downloadButton, likeButton);
        });

        updateButtons(song, downloadButton, likeButton);
        return result;
    }

    private void updateButtons(Song song, JButton downloadButton, JButton likeButton) {
        String downloadButtonLabel = song.downloaded ? "✕" : "↓";
        downloadButton.setEnabled(song.added);
        downloadButton.setText(downloadButtonLabel);

        String likeButtonLabel = song.liked ? "♥" : "♡";
        likeButton.setEnabled(song.added);
        likeButton.setText(likeButtonLabel);
    }

    private void createSongsScrollPane() {
        songsPanel = new JPanel();
        songsPanel.setLayout(new BoxLayout(songsPanel, BoxLayout.Y_AXIS));

        fillSongs();
        songsScrollPane = new JScrollPane(songsPanel);
    }
}
