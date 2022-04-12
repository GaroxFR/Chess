package chess.piece;

import chess.Board;
import chess.move.CheckSource;
import chess.move.Move;
import chess.Position;
import chess.move.PiecePin;
import chess.move.component.Capture;
import chess.player.Team;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public abstract class SlidingPiece extends Piece {

    public SlidingPiece(Team team, Position position) {
        super(team, position);
    }

    public SlidingPiece(Team team, Position position, boolean moved) {
        super(team, position, moved);
    }
    @Override
    public Set<Move> computePossibleMoves(Board board) {
        Set<Move> moves = new HashSet<>();
        PiecePin pin = board.getPiecePin(this);

        for (Position direction : this.getSlidingDirections()) {
            for (int i = 1; i < 8; i++) {
                Position nextPosition = this.position.add(direction.multiply(i));

                if (!nextPosition.isInBoard() || (pin != null && !pin.isPossible(nextPosition))) {
                    break;
                }

                if (board.getPiece(nextPosition) != null) {
                    Piece piece = board.getPiece(nextPosition);
                    if (piece.getTeam() != this.getTeam()) {
                        moves.add(new Move(this.position, nextPosition, this, new Capture(piece)));
                    }
                    break;
                }

                moves.add(new Move(this.position, nextPosition, this));
            }
        }

        return moves;
    }

    @Override
    public Set<Position> computeThreatenedPositions(Board board) {
        Set<Position> threatenedPositions = new HashSet<>();

        for (Position direction : this.getSlidingDirections()) {
            for (int i = 1; i < 8; i++) {
                Position nextPosition = this.position.add(direction.multiply(i));

                if (!nextPosition.isInBoard()) {
                    break;
                }

                if (board.getPiece(nextPosition) != null && !(board.getPiece(nextPosition) instanceof King)) {
                    threatenedPositions.add(nextPosition);
                    break;
                }

                threatenedPositions.add(nextPosition);
            }
        }

        return threatenedPositions;
    }

    public Optional<PiecePin> computePiecePin(Board board) {
        for (Position direction : this.getSlidingDirections()) {
            Set<Position> positions = new HashSet<>();
            positions.add(this.position);
            Piece ennemyMet = null;
            for (int i = 1; i < 8; i++) {
                Position nextPosition = this.position.add(direction.multiply(i));
                if (!nextPosition.isInBoard()) {
                    break;
                }
                positions.add(nextPosition);

                Piece nextPiece = board.getPiece(nextPosition);
                if (nextPiece == null) {
                    continue;
                }
                if (nextPiece.getTeam() == this.getTeam()) {
                    break;
                }
                if (nextPiece instanceof King) {
                    if (ennemyMet != null) {
                        return Optional.of(new PiecePin(ennemyMet, positions));
                    } else {
                        board.addCheckSource(new CheckSource(this, positions));
                        break;
                    }
                }
                if (ennemyMet != null) {
                    break;
                }
                ennemyMet = nextPiece;
            }
        }

        return Optional.empty();
    }

    protected abstract Position[] getSlidingDirections();
}
