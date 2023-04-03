package ui.panels;

import backend.LibraryEndpoints;
import model.Playlist;
import model.Song;
import ui.MainUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


public class LibraryPanel extends ContentPanel{

//    User user = this.mainUI.getUser();
    ArrayList<Playlist> playlistList;
    ArrayList<Song> playlistSongList;

    int numPl;
    int numSongs;

    JScrollPane plScroll;
    JScrollPane songsScroll;
    public LibraryPanel(MainUI mainUI) {
        super(mainUI);
    }





    @Override
    protected void generate() {
        getPlayLists();
        getSongLists(null);
        setLayout(new BorderLayout());
        JPanel nestedPanels = new JPanel();
        BoxLayout layout = new BoxLayout(nestedPanels, BoxLayout.X_AXIS);
        JButton createPlaylist = createPlButtons("Create a new playlist!");
        createPlaylist.setAlignmentX(Component.LEFT_ALIGNMENT);
        nestedPanels.setLayout(layout);
        plScroll = makeScroll();
        songsScroll = makeScroll();
//        playListList = ed.getPlaylists(user.getUsername());
        JPanel plTable = makePLTable();
        JPanel songsTable = makeSongTable(null);

//        JTable songsTable = makePlTable("Songs in " + currentPL, songsCol);

        songsScroll.setAlignmentX(Component.RIGHT_ALIGNMENT);
        plScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(createPlaylist, BorderLayout.PAGE_START);
        nestedPanels.add(plScroll);
        nestedPanels.add(songsScroll);
        plScroll.setViewportView(plTable);
        songsScroll.setViewportView(songsTable);
        add(nestedPanels, BorderLayout.CENTER);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }

    void getPlayLists() {
        playlistList = LibraryEndpoints.getPlaylists(this.mainUI.getUser().getUsername());
        numPl = LibraryEndpoints.getPlaylistCount(this.mainUI.getUser().getUsername());
    }

    void getSongLists(String plName) {
        if (plName == null) {
            playlistSongList = LibraryEndpoints.getLibrarySongs(this.mainUI.getUser().getUsername());
            numSongs = playlistSongList.size();
        } else {
            playlistSongList = LibraryEndpoints.getPlaylistSongs(this.mainUI.getUser().getUsername(), plName);
            numSongs = LibraryEndpoints.getSongCount(this.mainUI.getUser().getUsername(), plName);
        }
    }

    private JPanel makePLTable(){
//        playlistList = LibraryEndpoints.getPlaylists("A113").toArray();
//        String[] playlistList = new String[]{"NULL", "NULL"};
        getPlayLists();
        JPanel temp = new JPanel();
        temp.setSize(new Dimension(400, numPl*20));
        temp.setLayout(new BoxLayout(temp, BoxLayout.Y_AXIS));


        for (Playlist p : playlistList) {
            temp.add(makePlResults(p.getPlName(), p.getSize()));
        }



        temp.setOpaque(true);
        temp.setVisible(true);
        revalidate();
        repaint();

        return temp;

    }



    private JPanel makeSongTable(String plname) {
        getSongLists(plname);
        JPanel temp = new JPanel();
        temp.setMaximumSize(new Dimension(400, numSongs*20));
        temp.setLayout(new BoxLayout(temp, BoxLayout.Y_AXIS));

        temp.setOpaque(true);
        temp.setVisible(true);

        for (Song s : playlistSongList) {
            temp.add(makeSongsPanel(s));
        }

        revalidate();
        repaint();

        return temp;

    }

    private JPanel makeSongsPanel(Song s) {
        JPanel result = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        result.setSize(400, 20);
        result.setBackground(Color.lightGray);
        JLabel song = new JLabel(s.getName() + "\n");   //TODO include the artist in the panel
        song.setMaximumSize(new Dimension(300,20));
        result.add(song);

        return result;

    }

    private JPanel makePlResults(String pl, int num) {
        JPanel result = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        result.setSize(400, 18);
        result.setBackground(Color.white);
        JLabel playlist = new JLabel(pl);
        playlist.setMaximumSize(new Dimension(100,20));
        result.add(playlist);

        JLabel count = new JLabel("# of songs: " +  Integer.toString(num));
        count.setPreferredSize(new Dimension(100,20));
        count.setOpaque(true);
        result.add(count);

        JButton select = new JButton("Select");
        select.setPreferredSize(new Dimension(100,20));
        select.setOpaque(true);
        select.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                songsScroll.setViewportView(makeSongTable(pl));

            }
        });

        result.setOpaque(true);
        result.add(select);


        return result;
    }


    private JButton createPlButtons(String text) {
        JButton temp = new JButton(text);
        temp.setMaximumSize(new Dimension(400,50));
        temp.setPreferredSize(new Dimension(400,50));
        temp.setAlignmentX(1);
        temp.setAlignmentY(1);
        temp.setOpaque(true);
        temp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame paymentFrame = new JFrame("Create Playlist");
                paymentFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                paymentFrame.setContentPane(makePlaylistWindow());
                paymentFrame.setSize(new Dimension(400,160));
                paymentFrame.setVisible(true);
            }
        });
        return temp;

    }

    private JPanel makePlaylistWindow() {
        JPanel makePlaylist = new JPanel();
        JTextField name = new JTextField("Playlist Name:");

        name.setPreferredSize(new Dimension(300, 50));
        name.setAlignmentX(Component.CENTER_ALIGNMENT);
        name.setAlignmentY(Component.CENTER_ALIGNMENT);
        name.setVisible(true);

        JButton doneButton = new JButton("Done");
        doneButton.setPreferredSize(new Dimension(150, 40));
        doneButton.setVisible(true);
        doneButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        doneButton.setAlignmentY(Component.CENTER_ALIGNMENT);
        doneButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                String newName = name.getText();
                Boolean b = newPL(newName);
                makePlaylist.hide();


            }
        });
        makePlaylist.add(name);
        makePlaylist.add(doneButton);
        return makePlaylist;
    }

    private boolean newPL(String plname) {
       Boolean b = LibraryEndpoints.makePL(this.mainUI.getUser().getUsername(), plname);
        makePLTable();
        return b;
    }

    private JScrollPane makeScroll() {

        JScrollPane temp = new JScrollPane();
        JScrollBar sb = new JScrollBar();
        sb.setOpaque(true);
        temp.setVerticalScrollBar(sb);
        temp.setBackground(Color.lightGray);
        temp.setMaximumSize(new Dimension(400,450));
        temp.setPreferredSize(new Dimension(400,450));
        temp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        temp.setOpaque(true);
        return temp;
    }
}