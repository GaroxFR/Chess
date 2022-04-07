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

    public abstract Set<Move> computePossibleMoves(Board board);

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

    @Override
    public abstract Piece clone();
}
