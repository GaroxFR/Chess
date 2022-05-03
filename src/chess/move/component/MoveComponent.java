package chess.move.component;

import chess.Board;

public interface MoveComponent {

    /**
     * Méthode appelée lorsqu'un coup est joué
     */
    void apply(Board board);

    /**
     * Méthode appelée après que le coup ai été joué. Ceci est seulement important pour la promotion, qui doit absolument
     * être prise en compte après le déplacement du pion.
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
