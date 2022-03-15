package chess.piece;

import chess.Board;
import chess.Move;
import chess.Position;
import chess.player.Team;

import java.util.HashSet;
import java.util.Set;

public class Rook extends SlidingPiece {

    private static final Position[] DIRECTIONS = {
            // Lignes
            new Position(0, 1),
            new Position(0, -1),
            new Position(1, 0),
            new Position(-1, 0)
    };

    public Rook(Team team, Position position) {
        super(team, position);

    }

    @Override
    protected Position[] getSlidingDirections() {
        return Rook.DIRECTIONS;;
    }
}
