package chess;

import chess.player.Computer;
import chess.player.Human;
import chess.player.Player;
import chess.player.Team;

public class FrameTest {
    public static void main (String[]args){
        Player[] players = {
                new Human(Team.BLACK, "Joueur"),
                new Human(Team.WHITE, "Joueur")
        };
        Board board = new Board(players);
        //board.loadFEN("4R3/1k6/1p2P1p1/p7/4r3/1P1r4/1K6/2R5 w - - 0 0");
        board.loadFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq");
        //board.loadFEN("8/8/8/p7/1P/8/8/8 w");
        MainFrame test = new MainFrame(board);
    }
}
