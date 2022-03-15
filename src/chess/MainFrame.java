package chess;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class MainFrame extends JFrame /*implements ActionListener*/{

    final int width = 700;
    final int height = 700;
    private JPanel mainBloc;
    private JPanel topBloc;
    private JPanel leftBloc;
    private JPanel rightBloc;
    private JLabel player1;
    private JLabel player2;
    private JLabel timeDisplay1;
    private JLabel timeDisplay2;
    private Timer timer1; private int min1; private int sec1;
    private Timer timer2; private int min2; private int sec2;

    public MainFrame () {
        setLocation(290, 10);
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //init min et sec (selon init de la fenetre de depart)

        topBloc = new JPanel();
        topBloc.setLayout(null);
        topBloc.setBounds(25, 0, 650, 100);
        //image de fond ?
        this.add(topBloc);

        leftBloc = new JPanel();
        leftBloc.setLayout(null);
        leftBloc.setBounds(0, 144, 94, 512);
        this.add(leftBloc);

        rightBloc = new JPanel();
        rightBloc.setLayout(null);
        rightBloc.setBounds(606, 144, 94, 512);
        this.add(rightBloc);

        player1 = new JLabel(/*get name de la premiere fenetre*/);
        player2 = new JLabel(/*get name de la premiere fenetre*/);

        timeDisplay1 = new JLabel("Temps : "/*a completer*/);
        timeDisplay2 = new JLabel("Temps : "/*a completer*/);

        mainBloc.add(topBloc);
        mainBloc.add(leftBloc);
        mainBloc.add(rightBloc);
        this.add(mainBloc);

        this.setVisible(true);

        //timer1 = new Timer(1000, timer1Listener); //start quand le tour du joueur 1 commence & stop quand le tour du joueur 2 commence


    }
    /*
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == timer1){

        }
    }
     */

    //ImageIcon(Icon nomImage);
    //sky = Toolkit.getDefaultToolkit().getImage("sky.jpg");
}
