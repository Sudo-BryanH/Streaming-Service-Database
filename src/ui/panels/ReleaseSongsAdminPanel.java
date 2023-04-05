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

    private interface FunctionCallback {
        void apply(Song song);
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
        addButton.addActionListener(e -> infoPrompt(null, this::add));

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
        editButton.addActionListener(e -> infoPrompt(song, this::edit));
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

    private void add(Song song) {
        try {
            SongEndpoints.addSong(song);

            Song check = SongEndpoints.getSongsByReleaseTrackNum(release, song.trackNum, mainUI.getUser().getUsername());
            if (check != null && check.releaseID == song.releaseID && check.trackNum == song.trackNum &&
                    check.name.equals(song.name) && check.genre.equals(song.genre) && check.duration == song.duration &&
                    check.plays == song.plays) {
                JOptionPane.showMessageDialog(this, "Song '" + song.name + "' added!");
                fillSongs();
            } else {
                JOptionPane.showMessageDialog(this, "Song add failed :(");
            }
        }
        catch (Exception exception){
            JOptionPane.showMessageDialog(this, "Song add failed :(");
        }
    }

    private void edit(Song song) {
        try {
            SongEndpoints.editSong(song);

            Song check = SongEndpoints.getSongsByReleaseTrackNum(release, song.trackNum, mainUI.getUser().getUsername());
            if (check != null && check.releaseID == song.releaseID && check.trackNum == song.trackNum) {
                fillSongs();
            } else {
                JOptionPane.showMessageDialog(this,  "Edit failed :(");
            }
        }
        catch (Exception exception){
            JOptionPane.showMessageDialog(this, "Edit failed :(");
        }
    }

    private void infoPrompt(Song song, FunctionCallback callback) {
        JFrame infoFrame = new JFrame("Song Info");
        infoFrame.setSize(new Dimension(450,300));
        infoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        infoFrame.setVisible(true);

        JPanel infoContent = new JPanel(new GridLayout(7,2,10,10));

        JLabel idLabel = new JLabel("Release ID:");
        JLabel trackNumLabel = new JLabel("Track #:");
        JLabel nameLabel = new JLabel("Name:");
        JLabel durationLabel = new JLabel("Duration (s):");
        JLabel genreLabel = new JLabel("Genre:");
        JLabel playsLabel = new JLabel("# of Plays:");

        JTextField idField = new JTextField(20);
        JTextField trackNumField = new JTextField(20);
        JTextField nameField = new JTextField(20);
        JTextField durationField = new JTextField(20);
        JTextField genreField = new JTextField(20);
        JTextField playsField = new JTextField(20);

        idField.setText(String.valueOf(release.id));
        idField.setEnabled(false);
        if (song != null) {
            trackNumField.setText(String.valueOf(song.trackNum));
            trackNumField.setEnabled(false);
            nameField.setText(song.name);
            durationField.setText(String.valueOf(song.duration));
            genreField.setText(song.genre);
            playsField.setText(String.valueOf(song.plays));
        }

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            try {
                int releaseID = Integer.parseInt(idField.getText());
                int trackNum = Integer.parseInt(trackNumField.getText());
                String name = nameField.getText();
                int duration = Integer.parseInt(durationField.getText());
                String genre = genreField.getText();
                int plays = Integer.parseInt(playsField.getText());

                callback.apply(new Song(releaseID, trackNum, name, duration, genre, plays));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Info parsing failed :(");
            }

            infoFrame.dispose();
        });

        infoContent.add(idLabel);
        infoContent.add(idField);
        infoContent.add(trackNumLabel);
        infoContent.add(trackNumField);
        infoContent.add(nameLabel);
        infoContent.add(nameField);
        infoContent.add(durationLabel);
        infoContent.add(durationField);
        infoContent.add(genreLabel);
        infoContent.add(genreField);
        infoContent.add(playsLabel);
        infoContent.add(playsField);
        infoContent.add(new JLabel());
        infoContent.add(submitButton);

        infoContent.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        infoFrame.add(infoContent);
    }
}
