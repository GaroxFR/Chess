package chess.ihm.frames;

import chess.Board;
import chess.ihm.panels.BoardPanel;
import chess.ihm.panels.CapturedPiecePanel;
import chess.player.Team;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameFrame extends JFrame implements ActionListener {

    final int width = 800;
    final int height = 786;
    private JPanel mainBloc;
    private Board plateau;
    private BoardPanel game;
    private JPanel topBloc;
    private JPanel leftBloc;
    private JPanel rightBloc;
    private String playerW;
    private String playerB;
    private String timeDisplayW;
    private String timeDisplayB;
    private Timer t;
    private int minW; private int secW;
    private int minB; private int secB;
    private int timeMax;

    private ImageIcon background;
    private JLabel backgroundLabel;

    private ImageIcon sides;
    private JLabel leftLabel;
    private JLabel rightLabel;

    public GameFrame(Board plateau , int timeMax) {

        this.timeMax = timeMax;
        this.plateau = plateau;
        minW=timeMax; minB=timeMax; secW=0; secB = 0;
        //Renseigne le nom des joueurs
        playerW = plateau.getPlayers()[0].getName();
        playerB = plateau.getPlayers()[1].getName();


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
        this.background = new ImageIcon("res/images/background_war.png");
        backgroundLabel = new JLabel(this.background,JLabel.CENTER);
        backgroundLabel.setBounds(0,0,this.width,96);
        topBloc.add(backgroundLabel);

        leftBloc = new CapturedPiecePanel(Team.WHITE, plateau, this);
        leftBloc.setLayout(null);
        leftBloc.setBounds(0, 195, 128, 490);

        rightBloc = new CapturedPiecePanel(Team.BLACK, plateau, this);
        rightBloc.setLayout(null);
        rightBloc.setBounds(658, 195, 128, 490);

        Timer repaintTimer = new Timer(10, e -> {
            this.repaint();
            if (plateau.isEnded()) {
                new WelcomeFrame();
                this.setVisible(false);
                this.dispose();
                ((Timer) e.getSource()).stop();
            }
        });
        repaintTimer.start();


        mainBloc = new JPanel();
        mainBloc.setLayout(null);
        mainBloc.setBounds(0,0,this.width, this.height);
        mainBloc.setBackground(new Color(23,82,41));

        mainBloc.add(game);
        mainBloc.add(topBloc);
        mainBloc.add(leftBloc);
        mainBloc.add(rightBloc);

        this.add(mainBloc);

        t = new Timer(1000,this);
        t.start();

        this.setVisible(true);

    }

    public void actionPerformed(ActionEvent e){
        if(plateau.getTimerTurn() == Team.WHITE){  //timer : met à jour le temps
            if(secW>0){
                secW--;
            }else{
                secW=59;
                minW--;
            }
        }else {
            if(secB>0){
                secB--;
            }else{
                secB=59;
                minB--;
            }
        }
        if (secW <= 0 && minW <= 0) {
            JOptionPane.showMessageDialog(this, "Temps écoulé, " + playerB + " a gagné");
            new WelcomeFrame();
            this.setVisible(false);
            this.dispose();
            t.stop();
        } else if (secB <= 0 && minB <= 0) {
            JOptionPane.showMessageDialog(this, "Temps écoulé, " + playerW + " a gagné");
            new WelcomeFrame();
            this.setVisible(false);
            this.dispose();
            t.stop();
        }
        timeDisplayW = String.format("temps restant : %02d:%02d", minW, secW);
        timeDisplayB = String.format("temps restant : %02d:%02d", minB, secB);
    }

    public String getPlayerW() {
        return this.playerW;
    }

    public String getPlayerB() {
        return this.playerB;
    }

    public String getTimeDisplayW() {
        return this.timeDisplayW;
    }

    public String getTimeDisplayB() {
        return this.timeDisplayB;
    }
}
