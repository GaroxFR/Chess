package chess.piece;

import chess.Position;
import chess.player.Team;

public class Queen extends SlidingPiece {

    private static final Position[] DIRECTIONS = {
            // Diagonales
            new Position(1, 1),
            new Position(1, -1),
            new Position(-1, -1),
            new Position(-1, 1),
            // Lignes
            new Position(0, 1),
            new Position(0, -1),
            new Position(1, 0),
            new Position(-1, 0)
    };

    public Queen(Team team, Position position) {
        super(team, position);
    }

    @Override
    protected Position[] getSlidingDirections() {
        return Queen.DIRECTIONS;
    }
}
