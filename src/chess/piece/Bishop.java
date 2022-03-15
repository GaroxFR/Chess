package chess.piece;

import chess.Position;
import chess.player.Team;

public class Bishop extends SlidingPiece {

    private static final Position[] DIRECTIONS = {
            new Position(1, 1),
            new Position(1, -1),
            new Position(-1, -1),
            new Position(-1, 1)
    };

    public Bishop(Team team, Position position) {
        super(team, position);
    }

    @Override
    protected Position[] getSlidingDirections() {
        return Bishop.DIRECTIONS;
    }

}
