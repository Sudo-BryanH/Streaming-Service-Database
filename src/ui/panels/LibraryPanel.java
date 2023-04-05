

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

    JPanel plTable;

    JPanel songsTable;

    JScrollPane plScroll;
    JScrollPane songsScroll;
    JButton yourLib;
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
        JButton createPlaylist = createPlButton("✚ Create a new playlist");
         yourLib = yourLibButton();
        createPlaylist.setAlignmentX(Component.LEFT_ALIGNMENT);
        nestedPanels.setLayout(layout);
        plScroll = makeScroll();
        songsScroll = makeScroll();
//        playListList = ed.getPlaylists(user.getUsername());
        makePLTable();
        makeSongTable(null);

//        JTable songsTable = makePlTable("Songs in " + currentPL, songsCol);

        songsScroll.setAlignmentX(Component.RIGHT_ALIGNMENT);
        plScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        yourLib.setAlignmentX(Component.RIGHT_ALIGNMENT);
        add(createPlaylist, BorderLayout.PAGE_START);
//        add(yourLib, BorderLayout.PAGE_END);
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

    private void makePLTable(){
//        playlistList = LibraryEndpoints.getPlaylists("A113").toArray();
//        String[] playlistList = new String[]{"NULL", "NULL"};
        getPlayLists();
        if (plTable == null) {
            plTable = new JPanel();
//            plTable.setMaximumSize(new Dimension(400, numPl*10));

            plTable.setLayout(new BoxLayout(plTable, BoxLayout.PAGE_AXIS));
            plTable.setBackground(Color.white);
            plTable.setForeground(Color.white);
        }

        plTable.setMaximumSize(new Dimension(400, numPl*20));

        plTable.removeAll();
        for (Playlist p : playlistList) {

            plTable.add(makePlResults(p));

        }


        plTable.add(Box.createVerticalGlue());
        plTable.setOpaque(true);
        plTable.setVisible(true);
        revalidate();
        repaint();

//        return temp;

    }



    private void makeSongTable(String plname) {


        getSongLists(plname);
        if (songsTable == null) {
            songsTable = new JPanel();
//            plTable.setMaximumSize(new Dimension(400, numPl*10));

            songsTable.setLayout(new BoxLayout(songsTable, BoxLayout.PAGE_AXIS));
            songsTable.setBackground(Color.white);
            songsTable.setForeground(Color.white);
        }
        numSongs = LibraryEndpoints.getSongCount(this.mainUI.getUser().getUsername(), plname);
        songsTable.setMaximumSize(new Dimension(400, numSongs*20));

        JLabel currPlayList = new JLabel(((plname == null) ? "Your Library": plname));
        currPlayList.setMaximumSize(new Dimension(400, 20));
        currPlayList.setBackground(Color.gray);

        currPlayList.setOpaque(true);

        JPanel grey = new JPanel();
        grey.setMaximumSize(new Dimension(400, 20));
        grey.setBackground(Color.gray);
//        grey.setForeground(Color.gray);
        grey.setOpaque(true);
        grey.add(currPlayList);
        if (plname != null) {
            grey.add(yourLib);
        }
//        songsTable.add(grey);
        songsScroll.setColumnHeaderView(grey);

        songsTable.removeAll();
        for (Song s : playlistSongList) {

            songsTable.add(makeSongsPanel(s, plname));

        }



        songsTable.add(Box.createVerticalGlue());
        songsTable.setOpaque(true);
        songsTable.setVisible(true);
        revalidate();
        repaint();

    }

    private JPanel makeSongsPanel(Song s, String plName) {
        JPanel result = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        result.setMaximumSize(new Dimension(400, 20));
        result.setBackground(Color.lightGray);
        JLabel song = new JLabel(s.getName() + "\n");   //TODO include the artist in the panel
        song.setMaximumSize(new Dimension(180,20));
        song.setMinimumSize(new Dimension(180,20));
        song.setPreferredSize(new Dimension(180,20));

        JComboBox<String> playlists = makePlaylistDropdown(s);


//        JButton delete = new JButton("⨂");
//        delete.setForeground(Color.red);
//        delete.setPreferredSize(new Dimension(20,20));
//        delete.setAlignmentX(Component.RIGHT_ALIGNMENT);
//        delete.setEnabled(true);
//        delete.setOpaque(true);
//        delete.addActionListener(new ActionListener(){
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                if (plName == null) {
//                    boolean b = deleteSongLib(s);
//                } else {
//                    boolean b = deleteSongPl(s, plName);
//                }
//
////                deletePL(pl);
//
//            }
//
//        });

        JButton likeButton = new JButton((s.liked)? "♥" : "♡");
        likeButton.setForeground((s.liked)? Color.green : Color.black);
        likeButton.setPreferredSize(new Dimension(20,20));
        likeButton.addActionListener(e -> {
            if (s.liked) {
                likeButton.setText("♡");
                likeButton.setForeground(Color.black);
                LibraryEndpoints.unlikeSong(s, this.mainUI.getUser().getUsername());
            } else {
                likeButton.setText("♥");
                likeButton.setForeground(Color.green);
                LibraryEndpoints.likeSong(s, this.mainUI.getUser().getUsername());
            }
            s.liked = !s.liked;
        });

        result.add(song);
        result.add(playlists);
        result.add(likeButton);
        return result;

    }

    private JComboBox<String> makePlaylistDropdown(Song s) {
        makePLTable();
        String[] plNames = new String[numPl + 1];
        plNames[0] = "Add to Playlist";
        for (int i = 0; i < numPl; i++) {
            plNames[i + 1] = playlistList.get(i).getPlName();
        }
        JComboBox<String> temp = new JComboBox<>(plNames);
        temp.setMaximumSize(new Dimension(60,20));
        temp.setBackground(Color.lightGray);

        temp.setOpaque(true);
        temp.setAlignmentX(Component.RIGHT_ALIGNMENT);
        temp.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {

                JComboBox comboBox1 = (JComboBox) e.getSource();


                Object selected = comboBox1.getSelectedItem();

                addSongPl(s, (String) selected);

            }


        });

        return temp;
    }

    private void addSongPl(Song s, String plName) {

        LibraryEndpoints.addSongToPl(s, this.mainUI.getUser().getUsername(), plName);
        makeSongTable(plName);
    }

    private boolean deleteSongPl(Song s, String plName) {

        LibraryEndpoints.deletePLSong(s, plName, this.mainUI.getUser().getUsername());
        makeSongTable(plName);

        return true;
    }

    private boolean deleteSongLib(Song s) {

        LibraryEndpoints.deleteLibSong(s, this.mainUI.getUser().getUsername());
        makeSongTable(null);
        return true;

    }

    private JPanel makePlResults(Playlist p) {
        String pl = p.getPlName();
        int num = p.getSize();
        JPanel result = new JPanel(new GridLayout(1, 3, 10, 10));
        result.setMaximumSize(new Dimension(400, 20));
        result.setBackground(Color.lightGray);
        result.setForeground(Color.lightGray);

        JLabel playlist = new JLabel(pl);
        playlist.setMaximumSize(new Dimension(100,20));
//        playlist.setMinimumSize(new Dimension(100,20));
//        playlist.setPreferredSize(new Dimension(100,20));
        result.add(playlist);

        JLabel count = new JLabel(num + " songs");
        count.setAlignmentX(Component.CENTER_ALIGNMENT);
        count.setBackground(Color.lightGray);
        count.setMaximumSize(new Dimension(60,20));
        count.setOpaque(true);
        result.add(count);

        JButton select = new JButton("Select");
        select.setMaximumSize(new Dimension(60,20));
        select.setBackground(Color.lightGray);
        select.setOpaque(true);
        select.setAlignmentX(Component.RIGHT_ALIGNMENT);
        select.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                makeSongTable(pl);

            }


        });

        JButton delete = new JButton("⨂");
        delete.setForeground(Color.red);
        delete.setMaximumSize(new Dimension(20,20));
        delete.setBackground(Color.lightGray);
        delete.setOpaque(true);
        delete.setAlignmentX(Component.RIGHT_ALIGNMENT);
        delete.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                makeSongTable(null);

                deletePL(pl);

            }
        });

        result.setOpaque(true);
        result.add(select);
        result.add(delete);


        return result;
    }


    private JButton createPlButton(String text) {
        JButton temp = new JButton(text);
        temp.setMaximumSize(new Dimension(200,50));
        temp.setPreferredSize(new Dimension(200,50));
        temp.setAlignmentX(1);
        temp.setAlignmentY(1);
        temp.setOpaque(true);
        temp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame makeFrame= new JFrame("Create Playlist");
                makeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                makeFrame.setContentPane(makePlaylistWindow());
                makeFrame.setSize(new Dimension(400,160));
                makeFrame.setVisible(true);
            }
        });
        return temp;

    }

    private JButton yourLibButton() {
        JButton temp = new JButton("Go To Library");
        temp.setMaximumSize(new Dimension(120,20));
        temp.setPreferredSize(new Dimension(120,20));
        temp.setAlignmentX(1);
        temp.setAlignmentY(1);
        temp.setOpaque(true);
        temp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                makeSongTable(null);
            }
        });
        return temp;

    }

    private JPanel makePlaylistWindow() {
        JPanel makePlaylist = new JPanel();
        JTextField name = new JTextField("UNTITLED");

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

    private boolean deletePL(String plname) {
        Boolean b = LibraryEndpoints.deletePL(this.mainUI.getUser().getUsername(), plname);
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

