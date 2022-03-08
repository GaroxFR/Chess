package chess.piece;

import chess.Board;
import chess.Move;
import chess.Position;
import chess.player.Team;

import java.util.Set;

public abstract class Piece {
    protected Team team;
    protected Position position;
    protected boolean alive = true;

    public Piece(Team team, Position position) {
        this.team = team;
        this.position = position;
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

    public abstract Set<Move> computePossibleMoves(Board board);

    public void playMove(Move move) {
        if (move.getStartPosition().equals(this.position)) {
            this.position = move.getEndPosition();
        }
    }
}
