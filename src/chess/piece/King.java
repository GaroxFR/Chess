package chess.piece;

import chess.Board;
import chess.move.component.Capture;
import chess.move.component.Castle;
import chess.move.Move;
import chess.Position;
import chess.player.Team;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class King extends Piece{

    private static Image ImgPieceWhite;
    private static Image ImgPieceBlack;
    private static final int[] CORNERS_X = {0, 7};

    private final int firstrow;

    static {
        King.ImgPieceWhite = Toolkit.getDefaultToolkit().getImage("res/roi_b.png");
        King.ImgPieceBlack = Toolkit.getDefaultToolkit().getImage("res/roi_n.png");
    }

    private static final Position[] DIRECTIONS = {
            new Position(0, 1),
            new Position(1, 0),
            new Position(0, -1),
            new Position(-1, 0),
            new Position(1, 1),
            new Position(1, -1),
            new Position(-1, -1),
            new Position(-1, 1)
    };

    public King(Team team, Position position) {
        this(team, position, false);
    }

    public King(Team team, Position position, boolean moved) {
        super(team, position, moved);
        if(team == Team.WHITE){
            this.firstrow = 0;
        }else{
            this.firstrow = 7;
        }
    }

    @Override
    public Set<Move> computePossibleMoves(Board board) {
        Set<Move> moves = new HashSet<>();


        for (Position direction : King.DIRECTIONS) {
            Position nextPosition = this.position.add(direction);
            if (nextPosition.isInBoard() && !board.isThreatened(nextPosition) && (board.getPiece(nextPosition) == null || board.getPiece(nextPosition).getTeam() != this.team)) {
                if (board.getPiece(nextPosition) != null) {
                    moves.add(new Move(this.position, nextPosition, this, new Capture(board.getPiece(nextPosition))));
                } else {
                    moves.add(new Move(this.position, nextPosition, this));
                }
            }
        }

        if(!this.hasMoved()) {    //pour le roque
            for (int i : King.CORNERS_X) {
                Piece piece = board.getPiece(i, this.firstrow);
                if(piece instanceof Rook && !piece.hasMoved()) {

                    int dir = (int) Math.signum(piece.getPosition().getX() - this.position.getX());
                    boolean valid = true;
                    for (int x = this.position.getX(); (piece.getPosition().getX() - x) * dir >= 0; x += dir) {
                        if ((x != piece.getPosition().getX() && x != this.position.getX() && board.getPiece(x, this.firstrow) != null) || board.isThreatened(new Position(x, this.firstrow))) {
                            valid = false;
                            break;
                        }
                    }
                    if (valid) {
                        Move move = new Move(this.position, this.position.add(2 * dir, 0), this, new Castle(piece, piece.getPosition(), this.position.add(dir, 0)));
                        moves.add(move);
                    }
                }
            }
        }
        return moves;
    }


    @Override
    public Set<Position> computeThreatenedPositions(Board board) {
        Set<Position> threatenedPositions = new HashSet<>();

        for (Position direction : King.DIRECTIONS) {
            Position nextPosition = this.position.add(direction);
            if (nextPosition.isInBoard()) {
                threatenedPositions.add(nextPosition);
            }
        }

        return threatenedPositions;
    }

    public Image getImage() {
        if (this.team == Team.WHITE) {
            return King.ImgPieceWhite;
        } else {
            return King.ImgPieceBlack;
        }
    }

    @Override
    public int getValue() {
        return 0;
    }

    @Override
    public Piece clone() {
        return new King(this.team, this.position, this.moved);
    }
}
