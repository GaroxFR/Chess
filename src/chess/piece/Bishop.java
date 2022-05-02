package chess.piece;

import chess.Position;
import chess.player.Team;

import java.awt.*;

public class Bishop extends SlidingPiece {

    private static Image ImgPieceWhite;
    private static Image ImgPieceBlack;

    static {
        Bishop.ImgPieceWhite = Toolkit.getDefaultToolkit().getImage("res/fou_b.png");
        Bishop.ImgPieceBlack = Toolkit.getDefaultToolkit().getImage("res/fou_n.png");
    }

    private static final Position[] DIRECTIONS = {
            new Position(1, 1),
            new Position(1, -1),
            new Position(-1, -1),
            new Position(-1, 1)
    };

    public Bishop(Team team, Position position) {
        super(team, position);
    }

    public Bishop(Team team, Position position, boolean moved) {
        super(team, position, moved);
    }

    @Override
    protected Position[] getSlidingDirections() {
        return Bishop.DIRECTIONS;
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
        return 300;
    }

    @Override
    public Piece clone() {
        return new Bishop(this.team, this.position, this.moved);
    }
}
