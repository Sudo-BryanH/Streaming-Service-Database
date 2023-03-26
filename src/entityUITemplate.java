import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;


public class entityUITemplate implements ActionListener {

    ArrayList<String> comp;

    String SQLQuery;
    String[] defaultComp = {"Compare"};
    String[] defaultCond = {"Type"};
    String[] songsComp = {"Compare", "is", "isn't", ""};
    String[] songsCond = {"Type", "Artist", "Genre", "Publisher"};
    String[] plComp = {"Compare", "is"};
    String[] plCond = {"Type", "Creator"};
    String[] artistsComp = {"Compare", "follows"};
    String[] artistsCond = {"Type", "Artist"};
    String[] billsComp = {"Compare", "is", "on", "before", "after"};
    String[] billsCond = {"Type", "User", "Date"};

    int windowWidth = 600;
    int windowHeight = 800;
    JFrame frame;
    JButton playlists;
    JButton bills;
    JButton songs;
    JButton artists;
    JComboBox<String> comparator;
    JComboBox<String> condition;

    JTextField checkFor;

    JTextArea results;

    JTextArea query;

    public entityUITemplate() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        designLayout();

    }

    private void designLayout() {
        frame.setSize(windowWidth, windowHeight);
        frame.setBackground(Color.lightGray);
        frame.setForeground(Color.lightGray);


        // Buttons
        playlists = makeSideButtons("Playlists", 0);
        playlists.addActionListener(this);
        frame.add(playlists);
        songs = makeSideButtons("Songs", 1);
        songs.addActionListener(this);
        frame.add(songs);
        artists = makeSideButtons("Artists", 2);
        artists.addActionListener(this);
        frame.add(artists);
        bills = makeSideButtons("Payments", 3);
        bills.addActionListener(this);
        frame.add(bills);

        // conditional dropdowns and search field
        condition = new JComboBox<String>(defaultCond);
        condition.setBounds(120, 60, 80, 40);
        condition.setOpaque(true);
        condition.setBackground(frame.getBackground());
        condition.setForeground(Color.black);
        frame.add(condition);
//
        comparator = new JComboBox<>(defaultComp);
        comparator.setBounds(200, 60, 80, 40);
        comparator.setOpaque(true);
        comparator.setBackground(frame.getBackground());
        comparator.setForeground(Color.black);
        frame.add(comparator);

        checkFor = new JTextField("Check for: ");
        checkFor.setBounds(300, 60, 280, 40);
        checkFor.setOpaque(true);
        checkFor.setBackground(Color.white);
        checkFor.setForeground(Color.black);
        frame.add(checkFor);

        // side and top bars
        JPanel sidebar = new JPanel();
        sidebar.setBounds(0, 0, 100, windowHeight);
        sidebar.setBackground(Color.darkGray);
        sidebar.setForeground(Color.darkGray);
        frame.add(sidebar);

        JPanel topbar = new JPanel();
        topbar.setBounds(0, 0, windowWidth, 40);
        topbar.setBackground(Color.gray);
        topbar.setForeground(Color.gray);
        frame.add(topbar);

        // results and query fields
        results = new JTextArea("Results here: ");

        results.setBounds(120, 120, 460, 460);
        results.setBackground(Color.white);
        results.setForeground(Color.black);
        frame.add(results);

        query = new JTextArea("Query used: ");
        query.setBounds(120, 600, 460, 160);
        query.setBackground(Color.white);
        query.setForeground(Color.black);
        frame.add(query);



        /* keep rand at the bottom */
        JPanel rand = new JPanel();
        frame.add(rand);

        frame.setVisible(true);
    }

    private JButton makeSideButtons(String label, int yOff) {
        JButton temp = new JButton(label);

        temp.setBounds(10, yOff * 50 + 10, 80, 40);
        temp.setOpaque(true);
        temp.setBackground(Color.darkGray);

        return temp;

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == playlists) {

            frame.remove(condition);
            frame.remove(comparator);
            condition = new JComboBox<>(plCond);
            condition.setBounds(120, 60, 80, 40);
            condition.setOpaque(true);
            condition.setBackground(frame.getBackground());
            condition.setForeground(Color.black);
            frame.add(condition);

            comparator = new JComboBox<>(plComp);
            comparator.setBounds(200, 60, 80, 40);
            comparator.setOpaque(true);
            comparator.setBackground(frame.getBackground());
            comparator.setForeground(Color.black);
            frame.add(comparator);


        } else if (e.getSource() == bills) {

            frame.remove(condition);
            frame.remove(comparator);
            condition = new JComboBox<>(billsCond);
            condition.setBounds(120, 60, 80, 40);
            condition.setOpaque(true);
            condition.setBackground(frame.getBackground());
            condition.setForeground(Color.black);
            frame.add(condition);

            comparator = new JComboBox<>(billsComp);
            comparator.setBounds(200, 60, 80, 40);
            comparator.setOpaque(true);
            comparator.setBackground(frame.getBackground());
            comparator.setForeground(Color.black);
            frame.add(comparator);
        } else if (e.getSource() == songs) {

            frame.remove(condition);
            frame.remove(comparator);
            condition = new JComboBox<>(songsCond);
            condition.setBounds(120, 60, 80, 40);
            condition.setOpaque(true);
            condition.setBackground(frame.getBackground());
            condition.setForeground(Color.black);
            frame.add(condition);

            comparator = new JComboBox<>(songsComp);
            comparator.setBounds(200, 60, 80, 40);
            comparator.setOpaque(true);
            comparator.setBackground(frame.getBackground());
            comparator.setForeground(Color.black);
            frame.add(comparator);
        } else if (e.getSource() == artists) {

            frame.remove(condition);
            frame.remove(comparator);
            condition = new JComboBox<>(artistsCond);
            condition.setBounds(120, 60, 80, 40);
            condition.setOpaque(true);
            condition.setBackground(frame.getBackground());
            condition.setForeground(Color.black);
            frame.add(condition);

            comparator = new JComboBox<>(artistsComp);
            comparator.setBounds(200, 60, 80, 40);
            comparator.setOpaque(true);
            comparator.setBackground(frame.getBackground());
            comparator.setForeground(Color.black);
            frame.add(comparator);
        }
    }


}

