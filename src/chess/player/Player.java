package chess.player;

import chess.Board;

public abstract class Player {
    private Team team;
    private String name;
    protected Board board;

    public Player(Team team, String name) {
        this.team = team;
        this.name = name;
    }

    public Team getTeam() {
        return this.team;
    }
    public String getName() {
        return this.name;
    }

    public void setBoard(Board board) {
        this.board = board;
    }
}
