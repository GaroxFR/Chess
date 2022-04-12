package chess.piece;

import chess.Board;
import chess.move.*;
import chess.Position;
import chess.move.component.Capture;
import chess.move.component.EnPassantPossibleCapture;
import chess.move.component.Promotion;
import chess.player.Team;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Pawn extends Piece {

    private final int doubleMoverow;
    private final int moveDirection;
    private final int promotionrow;
    private static Image ImgPieceWhite;
    private static Image ImgPieceBlack;

    static {
        Pawn.ImgPieceWhite = Toolkit.getDefaultToolkit().getImage("res/pion_b.png");
        Pawn.ImgPieceBlack = Toolkit.getDefaultToolkit().getImage("res/pion_n.png");
    }

    public Pawn(Team team, Position position) {
        this(team, position, false);
    }

    public Pawn(Team team, Position position, boolean moved) {
        super(team, position, moved);

        if (team == Team.WHITE) {
            this.doubleMoverow = 1;
            this.moveDirection = +1;
            this.promotionrow = 7;
        } else {
            this.doubleMoverow = 6;
            this.moveDirection = -1;
            this.promotionrow = 0;
        }
    }

    @Override
    public Set<Move> computePossibleMoves(Board board) {
        Set<Move> moves = new HashSet<>();
        PiecePin pin = board.getPiecePin(this);

        Position nextPosition = this.position.add(0, this.moveDirection);
        if (nextPosition.isInBoard() && (pin == null || pin.isPossible(nextPosition)) && board.getPiece(nextPosition) == null) {
            if (nextPosition.getY() == this.promotionrow) {
                moves.addAll(this.getPromotionMoves(nextPosition, null));
            } else {
                moves.add(new Move(this.position, nextPosition, this));
            }
        }

        nextPosition = this.position.add(0, 2 * this.moveDirection);
        if (this.position.getY() == this.doubleMoverow && nextPosition.isInBoard() && (pin == null || pin.isPossible(nextPosition)) && board.getPiece(this.position.add(0, this.moveDirection)) == null &&  board.getPiece(nextPosition) == null) {
            EnPassantPossibleCapture enPassantPossibleCapture = new EnPassantPossibleCapture(this.position.add(0, this.moveDirection), this);
            Move move = new Move(this.position, nextPosition, this, enPassantPossibleCapture);
            moves.add(move);
        }

        for (int i = -1; i <= 1; i+=2) {
            nextPosition = this.position.add(i, this.moveDirection);
            if (!nextPosition.isInBoard() || (pin != null && !pin.isPossible(nextPosition))) {
                continue;
            }

            if (board.getPiece(nextPosition) != null && board.getPiece(nextPosition).getTeam() != this.team) {
                if (nextPosition.getY() == this.promotionrow) {
                    moves.addAll(this.getPromotionMoves(nextPosition, board.getPiece(nextPosition)));
                } else {
                    moves.add(new Move(this.position, nextPosition, this, new Capture(board.getPiece(nextPosition))));
                }
            } else if (board.getEnPassantPossibleCapture() != null && board.getEnPassantPossibleCapture().getCapturePosition().equals(nextPosition)) {
                moves.add(new Move(this.position, nextPosition, this, new Capture(board.getEnPassantPossibleCapture().getCapturedPiece())));
            }
        }

        return moves;
    }

    @Override
    public Set<Position> computeThreatenedPositions(Board board) {
        Set<Position> threatenedPositions = new HashSet<>();
        for (int i = -1; i <= 1; i+=2) {
            Position nextPosition = this.position.add(i, this.moveDirection);
            if (nextPosition.isInBoard()) {
                threatenedPositions.add(nextPosition);

                Piece threatenedPiece = board.getPiece(nextPosition);
                if (threatenedPiece != null && threatenedPiece.getTeam() != this.getTeam() && threatenedPiece instanceof King) {
                    board.addCheckSource(new CheckSource(this, new HashSet<>(List.of(this.position))));
                }
            }
        }

        return threatenedPositions;
    }

    private List<Move> getPromotionMoves(Position nextPosition, Piece capture) {
        List<Move> moves = new ArrayList<>(4);

        if (capture != null) {
            moves.add(new Move(this.position, nextPosition, this, new Capture(capture), new Promotion(this, new Queen(this.team, this.position))));
            moves.add(new Move(this.position, nextPosition, this, new Capture(capture), new Promotion(this, new Rook(this.team, this.position))));
            moves.add(new Move(this.position, nextPosition, this, new Capture(capture), new Promotion(this, new Bishop(this.team, this.position))));
            moves.add(new Move(this.position, nextPosition, this, new Capture(capture), new Promotion(this, new Knight(this.team, this.position))));
        } else {
            moves.add(new Move(this.position, nextPosition, this, new Promotion(this, new Queen(this.team, this.position))));
            moves.add(new Move(this.position, nextPosition, this, new Promotion(this, new Rook(this.team, this.position))));
            moves.add(new Move(this.position, nextPosition, this, new Promotion(this, new Bishop(this.team, this.position))));
            moves.add(new Move(this.position, nextPosition, this, new Promotion(this, new Knight(this.team, this.position))));
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

    @Override
    public int getValue() {
        return 1;
    }

    @Override
    public Piece clone() {
        return new Pawn(this.team, this.position, this.moved);
    }
}
