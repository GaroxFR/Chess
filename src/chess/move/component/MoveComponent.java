package chess.move.component;

import chess.Board;

public interface MoveComponent {

    /**
     * Méthode appelée lorsqu'un coup est joué
     */
    void apply(Board board);

    /**
     * Méthode appelée après que le coup soit joué. Ceci est seulement important pour la promotion qui doit absolument
     * être pris en compte après le déplacement et du pion.
     */
    default void applyPostMove(Board board) {
    }

    /**
     * Méthode appelée lorsqu'un coup est annulé
     */
    void revert(Board board);

    default void revertPostMove(Board board) {
    }
}
