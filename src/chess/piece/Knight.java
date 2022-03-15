package chess.piece;

import chess.Board;
import chess.Move;
import chess.Position;
import chess.player.Team;

import java.awt.Image;
import java.awt.Toolkit;
import java.util.HashSet;
import java.util.Set;

public class Knight extends Piece {

    private static Image ImgPieceWhite;
    private static Image ImgPieceBlack;

    static {
        Knight.ImgPieceWhite = Toolkit.getDefaultToolkit().getImage("res/cavalier_b.png");
        Knight.ImgPieceBlack = Toolkit.getDefaultToolkit().getImage("res/cavalier_n.png");
    }

    private static final Position[] DIRECTIONS = {
            new Position(1, 2),
            new Position(1, -2),
            new Position(2, 1),
            new Position(2, -1),
            new Position(-1, 2),
            new Position(-1, -2),
            new Position(-2, 1),
            new Position(-2, -1)
    };
    

    public Knight(Team team, Position position) {
        super(team, position);

    }

    public Set<Move> computePossibleMoves(Board board) {
        Set<Move> moves = new HashSet<>();
        
        
        for (Position direction : DIRECTIONS) {
        
           Position test = direction.add(this.position);
            if (test.isInBoard() && (board.getPiece(test) == null || (board.getPiece(test) != null && board.getPiece(test).getTeam() != this.team))) {
                moves.add(new Move(this.position, test, this, board.getPiece(test))); //renvoie null si pas de pion
            }

        }
        return moves;
    }


    public Image getImage() {
        if (this.team == Team.WHITE) {
            return ImgPieceWhite;
        } else {
            return ImgPieceBlack;
        }
    }
}
