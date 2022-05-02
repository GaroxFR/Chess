package chess;

import chess.player.Computer;
import chess.player.Human;
import chess.player.Player;
import chess.player.Team;

public class FrameTest {
    public static void main (String[]args){
        //WelcomeFrame test1 = new WelcomeFrame();

        //Inutile - on garde pour l'instant pour des tests futurs
        Player[] players = {
                new Computer(Team.BLACK, 1.5f),
                new Human(Team.WHITE, "Enzo")
        };
        Board board = new Board(players);
        //board.loadFEN("4R3/1k6/1p2P1p1/p7/4r3/1P1r4/1K6/2R5 w - - 0 0");
        //board.loadFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq");
        board.loadFEN("8/1p2p2p/8/3k4/8/8/2P2P1P/6K1 b - - 0 1");
        //board.loadFEN("8/8/8/p7/1P/8/8/8 w");
        GameFrame test = new GameFrame(board, 10);
    }
}
