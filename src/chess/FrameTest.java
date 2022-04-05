package chess;

import chess.player.Computer;
import chess.player.Human;
import chess.player.Player;
import chess.player.Team;

public class FrameTest {
    public static void main (String[]args){
        Player[] players = {
                new Computer(Team.BLACK),
                new Human(Team.WHITE, "Joueur")
        };
        Board board = new Board(players);
        board.loadFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq");
        //board.loadFEN("8/8/8/p7/1P/8/8/8 w");
        MainFrame test = new MainFrame(board);
    }
}
