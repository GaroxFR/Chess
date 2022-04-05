package chess;

public class FrameTest {
    public static void main (String[]args){
        Board board = new Board();
        board.loadFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq");
        //board.loadFEN("8/8/8/p7/1P/8/8/8 w");
        MainFrame test = new MainFrame(board);
    }
}
