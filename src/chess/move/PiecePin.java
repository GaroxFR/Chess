package chess.move;

import chess.Position;
import chess.piece.Piece;

import java.util.HashSet;
import java.util.Set;

public class PiecePin {

    private final Piece piece;
    private final Set<Position> possiblePositions;

    public PiecePin(Piece piece, Set<Position> possiblePositions) {
        this.piece = piece;
        this.possiblePositions = possiblePositions;
    }

    public Piece getPiece() {
        return this.piece;
    }

    public Set<Position> getPossiblePositions() {
        return this.possiblePositions;
    }

    public boolean isPossible(Position position) {
        return this.possiblePositions.contains(position);
    }
}
