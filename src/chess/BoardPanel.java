package chess;

import javax.swing.*;
import java.awt.*;

public class BoardPanel extends JPanel {

    private Image fond;

    public BoardPanel(){
        this.fond = Toolkit.getDefaultToolkit().getImage("res/boards.png");
    }

    public BoardPanel(Board plateau){
        this.fond = Toolkit.getDefaultToolkit().getImage("res/boards.png");
    }

    public void paint(Graphics g){
        super.paintComponent(g);//méthode paint qui herite de la classe mere
        g.drawImage(this.fond,0,0,this.getWidth(),this.getHeight(),this);
    }

    //redessine l'IHM pour le mettre à jour
    public void update() {
        this.repaint();
    }
}
