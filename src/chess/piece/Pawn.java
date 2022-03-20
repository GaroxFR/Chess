package chess.piece;

import chess.Board;
import chess.Move;
import chess.Position;
import chess.player.Team;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class Pawn extends Piece {

    private int doubleMoveRaw;
    private int moveDirection;
    private static Image ImgPieceWhite;
    private static Image ImgPieceBlack;

    static {
        Pawn.ImgPieceWhite = Toolkit.getDefaultToolkit().getImage("res/pion_b.png");
        Pawn.ImgPieceBlack = Toolkit.getDefaultToolkit().getImage("res/pion_n.png");
    }

    public Pawn(Team team, Position position) {
        super(team, position);

        if (team == Team.WHITE) {
            this.doubleMoveRaw = 1;
            this.moveDirection = +1;
        } else {
            this.doubleMoveRaw = 6;
            this.moveDirection = -1;
        }
    }

    @Override
    public Set<Move> computePossibleMoves(Board board) {
        Set<Move> moves = new HashSet<>();

        Position nextPosition = this.position.add(0, this.moveDirection);
        if (nextPosition.isInBoard() && board.getPiece(nextPosition) == null) {
            moves.add(new Move(this.position, nextPosition, this));
        }

        nextPosition = this.position.add(0, 2 * this.moveDirection);
        if (this.position.getY() == this.doubleMoveRaw && nextPosition.isInBoard() && board.getPiece(nextPosition) == null) {
            moves.add(new Move(this.position, nextPosition, this));
        }

        for (int i = -1; i <= 1; i+=2) {
            nextPosition = this.position.add(i, this.moveDirection);
            if (nextPosition.isInBoard() && board.getPiece(nextPosition) != null && board.getPiece(nextPosition).getTeam() != this.team) {
                moves.add(new Move(this.position, nextPosition, this, board.getPiece(this.position)));
            }
        }

        return moves;
    }

    public Image getImage() {
        if (this.team == Team.WHITE) {
            return ImgPieceWhite;
        } else {
            return ImgPieceBlack;
        }
    }
}
