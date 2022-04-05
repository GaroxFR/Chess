package chess.piece;

import chess.Position;
import chess.player.Team;

import java.awt.*;

public class Rook extends SlidingPiece {

    private static Image ImgPieceWhite;
    private static Image ImgPieceBlack;

    static {
        Rook.ImgPieceWhite = Toolkit.getDefaultToolkit().getImage("res/tour_b.png");
        Rook.ImgPieceBlack = Toolkit.getDefaultToolkit().getImage("res/tour_n.png");
    }

    private static final Position[] DIRECTIONS = {
            // Lignes
            new Position(0, 1),
            new Position(0, -1),
            new Position(1, 0),
            new Position(-1, 0)
    };

    public Rook(Team team, Position position) {
        super(team, position);
        this.moved = true;
    }

    @Override
    protected Position[] getSlidingDirections() {
        return Rook.DIRECTIONS;
    }

    public Image getImage() {
        if (this.team == Team.WHITE) {
            return ImgPieceWhite;
        } else {
            return ImgPieceBlack;
        }
    }
}
