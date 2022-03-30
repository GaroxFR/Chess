package chess.piece;

import chess.Board;
import chess.move.CastleInfo;
import chess.move.Move;
import chess.Position;
import chess.player.Team;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class King extends Piece{

    private final boolean kingMoved; //pour le roque, si pièce déjà jouée
    private final boolean roque; //si le roque a été joué

    private static Image ImgPieceWhite;
    private static Image ImgPieceBlack;
    private static final int[] CORNERS_X = {0, 7};

    private final int firstRaw;

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
        super(team, position);
        this.kingMoved = false;
        this.roque = false;
        if(team == Team.WHITE){
            this.firstRaw = 0;
        }else{
            this.firstRaw = 7;
        }
    }

    @Override
    public Set<Move> computePossibleMoves(Board board) {
        Set<Move> moves = new HashSet<>();

        //détecter l'échec et mat : si la position finale de la piece fait partie des coups possibles d'une piece de l'autre team
        /*le roque :
        nextPosition = ;
        test de non occupation des cases entre la tour et le roi
        test de non control des cases entre la tour et le roi
        if(!played && la tour1 n'a pas encore été jouée && rook1.position)
        */

        for (Position direction : King.DIRECTIONS) {
            Position nextPosition = this.position.add(direction);
            if (nextPosition.isInBoard() && !board.isThreatened(nextPosition) && (board.getPiece(nextPosition) == null || board.getPiece(nextPosition).getTeam() != this.team)) {
                moves.add(new Move(this.position, nextPosition, this, board.getPiece(nextPosition)));
            }
        }
        //roque
        Position kingCurrentPosition = this.getPosition();
        Position leftRookCurrentPosition = this.getPosition(); //position de la tour ?
        Position rightRookCurrentPosition = this.getPosition(); //position de la tour ?


        if(!this.kingMoved) {
            for (int i : King.CORNERS_X) {
                Piece piece = board.getPiece(i, this.firstRaw);
                if(piece instanceof Rook && !((Rook) piece).getRookMoved()) {

                    int dir = (int) Math.signum(piece.getPosition().getX() - this.position.getX());
                    boolean valid = true;
                    for (int x = this.position.getX(); (piece.getPosition().getX() - x) * dir >= 0; x += dir) {
                        if ((x != piece.getPosition().getX() && x != this.position.getX() && board.getPiece(x, this.firstRaw) != null) || board.isThreatened(new Position(x, this.firstRaw))) {
                            valid = false;
                            break;
                        }
                    }
                    if (valid) {
                        Move move = new Move(this.position, this.position.add(2 * dir, 0), this);
                        move.setCastleInfo(new CastleInfo(piece, piece.getPosition(), this.position.add(dir, 0)));
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
}
