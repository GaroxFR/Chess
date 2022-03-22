package chess.move;

import chess.Position;
import chess.piece.Piece;

import java.util.Set;

public class CheckSource {

    private final Piece source;
    private final Set<Position> resolvingPositions;

    public CheckSource(Piece source, Set<Position> resolvingPositions) {
        this.source = source;
        this.resolvingPositions = resolvingPositions;
    }

    public Piece getSource() {
        return this.source;
    }

    public Set<Position> getResolvingPositions() {
        return this.resolvingPositions;
    }
}
