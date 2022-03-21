package chess.test;

import chess.Board;
import chess.BoardPanel;

import javax.swing.*;
import java.awt.*;

public class TestFrame extends JFrame {

    public TestFrame(Board board) {
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        BoardPanel boardPanel = new BoardPanel(board);
        this.add(boardPanel);
        this.pack();
        this.setVisible(true);
    }
}
