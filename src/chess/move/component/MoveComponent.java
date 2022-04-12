package chess.move.component;

import chess.Board;

public interface MoveComponent {

    void apply(Board board);

    default void applyPostMove(Board board) {
    }

    void revert(Board board);

    default void revertPostMove(Board board) {
    }
}
