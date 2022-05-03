package chess.move;

import chess.Position;
import chess.piece.Piece;

import java.util.Set;

/**
 * Représente les pièces dont le mouvement est limitée, car quitter une ligne ou une diagonale causerait un échec.
 * Ces cases peuvent être affichées en jeu avec la touche 'p'
 */
public class PiecePin {

    private final Piece piece;
    private final Set<Position> possiblePositions;

    public PiecePin(Piece piece, Set<Position> possiblePositions) {
        this.piece = piece;
        this.possiblePositions = possiblePositions;
    }

    public Piece getPiece() {
        return this.piece;
    }

    public Set<Position> getPossiblePositions() {
        return this.possiblePositions;
    }

    public boolean isPossible(Position position) {
        return this.possiblePositions.contains(position);
    }
}
