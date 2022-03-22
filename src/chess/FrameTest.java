package chess;

public class FrameTest {
    public static void main (String[]args){
        Board board = new Board();
        board.loadFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w");
        MainFrame test = new MainFrame(board);
    }
}
