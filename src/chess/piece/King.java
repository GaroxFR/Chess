package chess.piece;

import chess.Board;
import chess.Move;
import chess.Position;
import chess.player.Team;

import java.util.HashSet;
import java.util.Set;

public class King extends Piece{

    private int move;
    private boolean played; //pour le roque, si pièce déjà jouée
    private boolean roque; //si le roque a été joué

    public King(Team team, Position position, int move,boolean played, boolean roque) {
        super(team, position);
        this.move = 1;
        this.played = false;
        this.roque = false;
    }

    @Override
    public Set<Move> computePossibleMoves(Board board) {
        Set<Move> moves = new HashSet<>();

        Position nextPosition = this.position.add(0, this.move);
        if (nextPosition.isInBoard() && board.getPiece(nextPosition) == null) {
            moves.add(new Move(this.position, nextPosition, this));
        }

        nextPosition = this.position.add(0, -this.move);
        if (nextPosition.isInBoard() && board.getPiece(nextPosition) == null) {
            moves.add(new Move(this.position, nextPosition, this));
        }

        nextPosition = this.position.add(this.move,0);
        if (nextPosition.isInBoard() && board.getPiece(nextPosition) == null) {
            moves.add(new Move(this.position, nextPosition, this));
        }

        nextPosition = this.position.add(-this.move,0);
        if (nextPosition.isInBoard() && board.getPiece(nextPosition) == null) {
            moves.add(new Move(this.position, nextPosition, this));
        }

        //détecter l'échec et mat : si la position finale de la piece fait partie des coups possibles d'une piece de l'autre team
        /*le roque :
        nextPosition = ;
        test de non occupation des cases entre la tour et le roi
        test de non control des cases entre la tour et le roi
        if(!played && la tour1 n'a pas encore été jouée && rook1.position)
        */

        return moves;
    }
}
