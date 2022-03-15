package chess.piece;

import chess.Board;
import chess.Move;
import chess.Position;
import chess.player.Team;

import java.util.HashSet;
import java.util.Set;

public abstract class SlidingPiece extends Piece {

    public SlidingPiece(Team team, Position position) {
        super(team, position);
    }

    @Override
    public Set<Move> computePossibleMoves(Board board) {
        Set<Move> moves = new HashSet<>();

        for (Position direction : this.getSlidingDirections()) {
            for (int i = 1; i < 8; i++) {
                Position nextPosition = this.position.add(direction.multiply(i));

                if (!nextPosition.isInBoard()) {
                    break;
                }

                if (board.getPiece(nextPosition) != null) {
                    Piece piece = board.getPiece(nextPosition);
                    if (piece.getTeam() != this.getTeam()) {
                        moves.add(new Move(this.position, nextPosition, this, piece));
                    }
                    break;
                }

                moves.add(new Move(this.position, nextPosition, this));
            }
        }

        return moves;
    }

    protected abstract Position[] getSlidingDirections();
}
