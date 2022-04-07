package chess.piece;

import chess.Position;
import chess.player.Team;

import java.awt.*;

public class Queen extends SlidingPiece {

    private static Image ImgPieceWhite;
    private static Image ImgPieceBlack;

    static {
        Queen.ImgPieceWhite = Toolkit.getDefaultToolkit().getImage("res/reine_b.png");
        Queen.ImgPieceBlack = Toolkit.getDefaultToolkit().getImage("res/reine_n.png");
    }

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

    public Queen(Team team, Position position, boolean moved) {
        super(team, position, moved);
    }

    @Override
    protected Position[] getSlidingDirections() {
        return Queen.DIRECTIONS;
    }

    public Image getImage() {
        if (this.team == Team.WHITE) {
            return ImgPieceWhite;
        } else {
            return ImgPieceBlack;
        }
    }

    @Override
    public int getValue() {
        return 9;
    }

    @Override
    public Piece clone() {
        return new Queen(this.team, this.position, this.moved);
    }
}
