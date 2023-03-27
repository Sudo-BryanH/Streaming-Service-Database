package ui.panels;

import ui.MainUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LibraryPanel extends ContentPanel{
    public LibraryPanel(MainUI mainUI) {
        super(mainUI);
    }

    String currentPL = "null playlist";

    int numPL = 10;
    @Override
    protected void generate() {
        setLayout(new BorderLayout());
        JPanel nestedPanels = new JPanel();
        BoxLayout layout = new BoxLayout(nestedPanels, BoxLayout.X_AXIS);
        JButton createPlaylist = createPlButtons("Create a new playlist!");
        createPlaylist.setAlignmentX(Component.LEFT_ALIGNMENT);
        nestedPanels.setLayout(layout);
        String[] plCol = {"Playlist Name", "Number of songs"};
        String[] songsCol = {"Song Name", "Artist", "Genre"};
        JTable plTable = makePlTable("Your Playlists", plCol);
        JTable songsTable = makePlTable("Songs in " + currentPL, songsCol);
        JScrollPane plScroll = makeScroll();
        JScrollPane songsScroll = makeScroll();
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

    private JTable makePlTable(String text, String[] columnList){
        DefaultTableModel model = new DefaultTableModel(columnList, 0);
        JTable pan = new JTable(model);
        pan.setSize(1400, 50*numPL);
        pan.setBackground(Color.gray);
        pan.setOpaque(true);
        BoxLayout layout = new BoxLayout(pan, BoxLayout.Y_AXIS);

        return pan;
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
