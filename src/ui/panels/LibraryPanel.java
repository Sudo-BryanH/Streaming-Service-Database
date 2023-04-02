package ui.panels;

import backend.LibraryEndpoints;
import model.Playlist;
import model.User;
import ui.MainUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


public class LibraryPanel extends ContentPanel{

    User user = this.mainUI.getUser();
    ArrayList<Playlist> playlistList;
    public LibraryPanel(MainUI mainUI) {
        super(mainUI);
    }


    String[] songList = new String[]{"NULL", "NULL", "NULL"};
    String currentPL = "null playlist";


    @Override
    protected void generate() {
        getPlayLists();
        setLayout(new BorderLayout());
        JPanel nestedPanels = new JPanel();
        BoxLayout layout = new BoxLayout(nestedPanels, BoxLayout.X_AXIS);
        JButton createPlaylist = createPlButtons("Create a new playlist!");
        createPlaylist.setAlignmentX(Component.LEFT_ALIGNMENT);
        nestedPanels.setLayout(layout);
        String[] plCol = new String[]{"Playlist Name", "Number of songs"};
        String[] songsCol = new String[]{"Song Name", "Artist", "Genre"};
//        playListList = ed.getPlaylists(user.getUsername());
        JPanel plTable = makePLTable();

//        JTable songsTable = makePlTable("Songs in " + currentPL, songsCol);
        JScrollPane plScroll = makeScroll();
        JScrollPane songsScroll = makeScroll();
        songsScroll.setAlignmentX(Component.RIGHT_ALIGNMENT);
        plScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(createPlaylist, BorderLayout.PAGE_START);
        nestedPanels.add(plScroll);
        nestedPanels.add(songsScroll);
        plScroll.setViewportView(plTable);
//        songsScroll.setViewportView(songsTable);
        add(nestedPanels, BorderLayout.CENTER);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }

    void getPlayLists() {
        playlistList = LibraryEndpoints.getPlaylists(this.mainUI.getUser().getUsername());

    }

    private JPanel makePLTable(){
//        playlistList = LibraryEndpoints.getPlaylists("A113").toArray();
//        String[] playlistList = new String[]{"NULL", "NULL"};
        getPlayLists();
        JPanel temp = new JPanel();
        temp.setSize(new Dimension(400, (20*3)));
        temp.setLayout(new BoxLayout(temp, BoxLayout.Y_AXIS));
        temp.add(makePlResults("pl1", 2));
        // when querying, make sure to get the number of songs
        temp.add(makePlResults("pl2" , 2));
        temp.add(makePlResults("pl3" , 2));
//        temp.add(makePlResults("pl4" , 2));
//        temp.add(makePlResults("pl5" , 2));
//        temp.add(makePlResults("pl6" , 2));
//        temp.add(makePlResults("pl7" , 2));
//        temp.add(makePlResults("pl2" , 2));
//        temp.add(makePlResults("pl3" , 2));
//        temp.add(makePlResults("pl4" , 2));
//        temp.add(makePlResults("pl5" , 2));
//        temp.add(makePlResults("pl6" , 2));
//        temp.add(makePlResults("pl7" , 2));
        for (Playlist p : playlistList) {
            System.out.println(p.getPlName());
        }


        temp.setOpaque(true);
        return temp;

    }

    private JPanel makePlResults(String pl, int num) {
        JPanel result = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        result.setSize(396, 18);
        result.setBackground(Color.white);
        JLabel playlist = new JLabel(pl);
        playlist.setPreferredSize(new Dimension(100,20));
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

            }
        });

        result.setOpaque(true);
        result.add(select);
        JPanel backing = new JPanel();
        backing.setOpaque(true);
        backing.setPreferredSize(new Dimension(400, 20));
        backing.add(result);

        return backing;
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

            }
        });
        makePlaylist.add(name);
        makePlaylist.add(doneButton);
        return makePlaylist;
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