package chess.move;

import chess.Position;
import chess.piece.Piece;

import java.util.Set;

/**
 * Représente une source d'échec, c'est-à-dire une pièce mettant en échec le roi.
 * Elle contient la pièce source de l'échec ainsi que les cases permettant de bloquer cette source d'échec.
 * Par exemple, si une tour met le roi en échec, l'ensemble des cases pouvant résoudre cet échec sans bouger les pièces
 * sont les cases se trouvant entre le roi et la tour incluse (on peut en effet capturer la tour pour sortir de
 * l'échec. En partie, il est possible d'afficher ces cases pour des raisons de debug avec la touche 'c'
 */
public class CheckSource {

    private final Piece source;
    private final Set<Position> resolvingPositions;

    public CheckSource(Piece source, Set<Position> resolvingPositions) {
        this.source = source;
        this.resolvingPositions = resolvingPositions;
    }

    public Piece getSource() {
        return this.source;
    }

    public Set<Position> getResolvingPositions() {
        return this.resolvingPositions;
    }
}
