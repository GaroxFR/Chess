package chess;

import java.util.Objects;

/**
 * Représente une position sur le plateau.
 *
 * Cette classe ne possède aucun setter, elle est immutable. C'est-à-dire qu'elle ne peut pas être modifiée. Ceci permet d'éviter beaucoup de bugs
 * liés au passage des références dans de multiples méthodes.
 * Lors d'opérations sur les positions, une nouvelle position est renvoyée mais aucune n'est modifiée
 */
public class Position {
    private final int x;
    private final int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof Position)) {
            return false;
        }
        Position position = (Position) o;
        return this.x == position.x && this.y == position.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y);
    }

    public Position multiply(int scale) {
        return new Position(this.x * scale, this.y * scale);
    }

    public Position add(int x, int y) {
        return new Position(this.x + x, this.y + y);
    }

    public Position add(Position position) {
        return this.add(position.getX(), position.getY());
    }

    /**
     * Norme 1 du vecteur position. La class Position représente parfois un vecteur d'une pièce à une autre. C'est pourquoi calculer la distance peut être utile.
     * La norme 1 est ici intéressante et surtout plus rapide à calculer que la norme usuelle.
     */
    public int simpleNorm() {
        return Math.abs(this.x) + Math.abs(this.y);
    }

    public boolean isInBoard() {
        return this.x >= 0 && this.x <= 7
                && this.y >= 0 && this.y <= 7;
    }
}
