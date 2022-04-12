package chess;

import javax.swing.*;
import java.awt.*;

public class WelcomeFrame extends JFrame {

    final int width = 786;
    final int height = 786;
    private JPanel mainBloc;
    private JPanel topBloc;
    private JLabel player1;
    private JLabel player2;
    private JLabel namePlayer;
    private JLabel ia;
    private JLabel namePlayer1;
    private JLabel namePlayer2;
    private JTextField aNamePlayer;
    private JPanel aIa;
    private JTextField aNamePlayer1;
    private JTextField aNamePlayer2;
    private JButton buttonPlayer;
    private JButton buttonPlayer2;
    private JLabel chrono;
    private JPanel selectChrono;


    private ImageIcon topImage;
    private JLabel imageLabel;
    private ImageIcon background;
    private JLabel backLabel;

    public WelcomeFrame() {
        setLocation(1000, 300);
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainBloc = new JPanel();
        mainBloc.setLayout(null);
        mainBloc.setBounds(0,0,this.width, this.height);

        topBloc = new JPanel();
        topBloc.setLayout(null);
        topBloc.setBounds(250, 0,286, 286);

        /*
        this.background = new ImageIcon("res/images/welcome_background.jpeg");
        backLabel = new JLabel();
        backLabel.setBounds(0,0,786,786);
        backLabel.setIcon(background);
        mainBloc.add(backLabel);

         */

        this.topImage = new ImageIcon("res/images/presentation_icon3.png");
        imageLabel = new JLabel(this.topImage,JLabel.CENTER);
        imageLabel.setBounds(0,0,286,286);
        topBloc.add(imageLabel);
        mainBloc.add(topBloc);

        player1 = new JLabel("1 JOUEUR");
        player1.setBounds(130, 300, 300, 50);
        player1.setFont(new Font("Serif", Font.BOLD, 35));
        mainBloc.add(player1);

        player2 = new JLabel("2 JOUEURS");
        player2.setBounds(500, 300, 300, 50);
        player2.setFont(new Font("Serif", Font.BOLD, 35));
        mainBloc.add(player2);

        namePlayer = new JLabel("nom joueur : ");
        namePlayer.setBounds(93, 546, 100, 20);
        mainBloc.add(namePlayer);

        ia = new JLabel("niveau de l'IA : ");
        ia.setBounds(93, 566, 150, 20);
        mainBloc.add(ia);

        namePlayer1 = new JLabel("nom joueur 1 : ");
        namePlayer1.setBounds(445, 546, 150, 20);
        mainBloc.add(namePlayer1);

        namePlayer2 = new JLabel("nom joueur 2 : ");
        namePlayer2.setBounds(445, 566, 150, 20);
        mainBloc.add(namePlayer2);

        aNamePlayer = new JTextField("nom joueur");
        aNamePlayer.setBounds(293, 546, 150, 20);
        mainBloc.add(aNamePlayer);

        aIa = new JPanel();
        String[] level = {"débutant","moyen","expert"};
        JComboBox<String> l = new JComboBox<String>(level);
        l.setVisible(true);
        aIa.setBounds(293, 566, 150, 30);
        aIa.add(l);
        mainBloc.add(aIa);

        aNamePlayer1 = new JTextField("nom premier joueur");
        aNamePlayer1.setBounds(593, 546, 150, 20);
        mainBloc.add(aNamePlayer1);

        aNamePlayer2 = new JTextField("nom deuxième joueur");
        aNamePlayer2.setBounds(593, 566, 150, 20);
        mainBloc.add(aNamePlayer2);

        buttonPlayer = new JButton("JOUER SEUL");
        buttonPlayer.setBounds(97, 626, 200, 90);
        mainBloc.add(buttonPlayer);

        buttonPlayer2 = new JButton("JOUER A DEUX");
        buttonPlayer2.setBounds(489, 626, 200, 90);
        mainBloc.add(buttonPlayer2);

        chrono = new JLabel("chronomètre : ");
        chrono.setBounds(270, 756, 150, 20);
        mainBloc.add(chrono);

        selectChrono = new JPanel();
        String[] choices = { "30 min","20 min","10 min"};
        JComboBox<String> c = new JComboBox<String>(choices);
        c.setVisible(true);
        selectChrono.setBounds(393, 756, 100, 30);
        selectChrono.add(c);
        mainBloc.add(selectChrono);

        /*
        selectChrono = new JTextField("temps");
        selectChrono.setBounds(393, 756, 200, 20);
        mainBloc.add(selectChrono);
         */

        this.add(mainBloc);

        this.setVisible(true);
    }

    public static void main(String[] args) {
        WelcomeFrame f = new WelcomeFrame();
    }

}

