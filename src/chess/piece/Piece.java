package chess.piece;

import chess.Board;
import chess.move.Move;
import chess.Position;
import chess.player.Team;

import java.awt.*;
import java.util.Set;

public abstract class Piece {
    protected Team team;
    protected Position position;
    protected boolean alive = true;
    protected boolean moved = false;

    public Piece(Team team, Position position) {
        this.team = team;
        this.position = position;
    }

    public Piece(Team team, Position position, boolean moved) {
        this.team = team;
        this.position = position;
        this.moved = moved;
    }

    public Team getTeam() {
        return this.team;
    }

    public Position getPosition() {
        return this.position;
    }

    public boolean isAlive() {
        return this.alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    /**
     * Calcul les coups possible pour cette pièce en particulier.
     */
    public abstract Set<Move> computePossibleMoves(Board board);

    /**
     * Calcul les cases contrôlées par cette pièce (Ces cases peuvent être affichées en jeu à l'aide la touche 'm')
     */
    public abstract Set<Position> computeThreatenedPositions(Board board);

    public void setPosition(Position position) {
        this.position = position;
    }

    public abstract Image getImage();

    public boolean hasMoved() {
        return this.moved;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }

    public abstract int getValue();

    /**
     * Permet de créer une copie d'une pièce afin de créer l'environnement de calcul de l'ordinateur sans modifier le plateau principal
     */
    @Override
    public abstract Piece clone();
}
