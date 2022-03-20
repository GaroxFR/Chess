package chess.piece;

import chess.Board;
import chess.Move;
import chess.Position;
import chess.player.Team;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class King extends Piece{

    private int move;
    private boolean played; //pour le roque, si pièce déjà jouée
    private boolean roque; //si le roque a été joué
    private static Image ImgPieceWhite;
    private static Image ImgPieceBlack;

    static {
        King.ImgPieceWhite = Toolkit.getDefaultToolkit().getImage("res/roi_b.png");
        King.ImgPieceBlack = Toolkit.getDefaultToolkit().getImage("res/roi_n.png");
    }

    private static final Position[] DIRECTIONS = {
            new Position(0, 1),
            new Position(1, 0),
            new Position(0, -1),
            new Position(-1, 0),
            new Position(1, 1),
            new Position(1, -1),
            new Position(-1, -1),
            new Position(-1, 1)
    };

    public King(Team team, Position position) {
        super(team, position);
        this.move = 1;
        this.played = false;
        this.roque = false;
    }

    @Override
    public Set<Move> computePossibleMoves(Board board) {
        Set<Move> moves = new HashSet<>();

        //détecter l'échec et mat : si la position finale de la piece fait partie des coups possibles d'une piece de l'autre team
        /*le roque :
        nextPosition = ;
        test de non occupation des cases entre la tour et le roi
        test de non control des cases entre la tour et le roi
        if(!played && la tour1 n'a pas encore été jouée && rook1.position)
        */

        for (Position direction : King.DIRECTIONS) {
            if ((direction.isInBoard() && board.getPiece(direction) == null) || (board.getPiece(direction) != null && board.getPiece(direction).getTeam() != this.team)) {
                moves.add(new Move(this.position, direction, this, board.getPiece(direction)));
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
