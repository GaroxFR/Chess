package chess;

public class FrameTest {
    public static void main (String[]args){
        Board board = new Board();
        board.loadFEN("rnbqkbnr/ppppPppp/8/8/8/8/PPpPPPPP/RNBQKBNR w");
        //board.loadFEN("8/8/8/p7/1P/8/8/8 w");
        MainFrame test = new MainFrame(board);
    }
}
