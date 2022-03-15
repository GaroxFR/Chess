package chess;

public class Main {
    public static void main(String[] args) {
        Board board = new Board();
        board.loadFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w");

        board.test();
        System.out.println(board.countPossibleMoves(2));

        for (int i = 1; i < 9; i++) {
            long time = System.currentTimeMillis();
            int count = board.countPossibleMoves(i);
            time = System.currentTimeMillis() - time;
            System.out.printf("%s --> %s coups (%s ms)\n", i, count, time);
        }
    }
}
