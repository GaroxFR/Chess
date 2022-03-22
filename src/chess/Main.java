package chess;

import chess.test.TestFrame;

public class Main {
    public static void main(String[] args) {
        Board board = new Board();
        board.loadFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w");
        //board.loadFEN("8/8/8/p7/1P/8/8/8 w");
        //new TestFrame(board);
        //board.countPossibleMoves(3);
        System.out.println(board.countPossibleMoves(7));
        /*for (int i = 1; i < 8; i++) {
            long time = System.currentTimeMillis();
            int count = board.countPossibleMoves(i);
            time = System.currentTimeMillis() - time;
            System.out.printf("%s --> %s coups (%s ms)\n", i, count, time);
        }*/
    }
}
