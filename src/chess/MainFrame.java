package chess;

import chess.test.TestFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MainFrame extends JFrame /*implements ActionListener*/{

    final int width = 786;
    final int height = 786;
    private JPanel mainBloc;
    private BoardPanel game;
    private JPanel topBloc;
    private JPanel leftBloc;
    private JPanel rightBloc;
    private JLabel player1;
    private JLabel player2;
    private JLabel timeDisplay1;
    private JLabel timeDisplay2;
    private Timer timer1; private int min1; private int sec1;
    private Timer timer2; private int min2; private int sec2;

    private ImageIcon background;
    private JLabel backgroundLabel;

    private ImageIcon sides;
    private JLabel leftLabel;
    private JLabel rightLabel;

    public MainFrame (Board plateau) {
        setLocation(290, 10);
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //init min et sec (selon init de la fenetre de depart)

        game = new BoardPanel(plateau);
        this.addKeyListener(game);
        game.setBounds(128,176,530,530);

        topBloc = new JPanel();
        topBloc.setLayout(null);
        topBloc.setBounds(0, 0,this.width,96);
        this.background = new ImageIcon("res/images/background2.png");
        backgroundLabel = new JLabel(this.background,JLabel.CENTER);
        backgroundLabel.setBounds(0,0,this.width,96);
        topBloc.add(backgroundLabel);

        leftBloc = new JPanel();
        leftBloc.setLayout(null);
        leftBloc.setBounds(0, 216, 128, 490);

        rightBloc = new JPanel();
        rightBloc.setLayout(null);
        rightBloc.setBounds(658, 216, 128, 490);

        player1 = new JLabel("Player1" /*get name de la premiere fenetre*/);
        player1.setBounds(235, 96, 60,20);
        player2 = new JLabel("Player2"/*get name de la premiere fenetre*/);
        player2.setBounds(235, 706, 60,20);

        timeDisplay1 = new JLabel("Temps : "/*a completer*/);
        timeDisplay1.setBounds(235, 116, 60,20);
        timeDisplay2 = new JLabel("Temps : "/*a completer*/);
        timeDisplay2.setBounds(235, 726, 60,20);

        mainBloc = new JPanel();
        mainBloc.setLayout(null);
        mainBloc.setBounds(0,0,this.width, this.height);
        mainBloc.setBackground(Color.CYAN);

        mainBloc.add(game);
        mainBloc.add(topBloc);
        mainBloc.add(leftBloc);
        mainBloc.add(rightBloc);
        mainBloc.add(player1);
        mainBloc.add(player2);
        mainBloc.add(timeDisplay1);
        mainBloc.add(timeDisplay2);

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
