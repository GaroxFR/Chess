package chess.test;

import chess.Board;
import chess.ihm.panels.BoardPanel;

import javax.swing.*;

public class TestFrame extends JFrame {

    public TestFrame(Board board) {
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setResizable(false);
        BoardPanel boardPanel = new BoardPanel(board);
        this.addKeyListener(boardPanel);
        this.add(boardPanel);
        this.pack();
        this.setVisible(true);
    }
}
