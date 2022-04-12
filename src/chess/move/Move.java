package chess.move;

import chess.Board;
import chess.Position;
import chess.move.component.MoveComponent;
import chess.move.component.Promotion;
import chess.piece.Piece;

import java.util.Objects;

public class Move {

    private final Position startPosition;
    private final Position endPosition;
    private final Piece piece;
    private final MoveComponent[] moveComponents;

    private PreMoveState preMoveState = null;


    public Move(Position startPosition, Position endPosition, Piece piece, MoveComponent... moveComponents) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.piece = piece;
        this.moveComponents = moveComponents;
    }

    public void apply(Board board) {
        for (MoveComponent moveComponent : this.moveComponents) {
            moveComponent.apply(board);
        }

        board.setPiece(this.endPosition, this.piece);
        board.setPiece(this.startPosition, null);

        for (MoveComponent moveComponent : this.moveComponents) {
            moveComponent.applyPostMove(board);
        }
    }

    public void revert(Board board) {
        for (MoveComponent moveComponent : this.moveComponents) {
            moveComponent.revertPostMove(board);
        }

        board.setPiece(this.startPosition, this.piece);
        board.setPiece(this.endPosition, null);

        for (MoveComponent moveComponent : this.moveComponents) {
            moveComponent.revert(board);
        }
    }

    public Position getStartPosition() {
        return this.startPosition;
    }

    public Position getEndPosition() {
        return this.endPosition;
    }

    public Piece getPiece() {
        return this.piece;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        Move move = (Move) o;
        return this.startPosition.equals(move.startPosition) && this.endPosition.equals(move.endPosition);
    }

    public PreMoveState getPreMoveState() {
        return this.preMoveState;
    }

    public void setPreMoveState(PreMoveState preMoveState) {
        this.preMoveState = preMoveState;
    }

    public <T extends MoveComponent> T getMoveComponent(Class<T> clazz) {
        for (MoveComponent moveComponent : this.moveComponents) {
            if (moveComponent.getClass().equals(clazz)) {
                return clazz.cast(moveComponent);
            }
        }

        return null;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.startPosition, this.endPosition, this.getMoveComponent(Promotion.class));
    }
}
