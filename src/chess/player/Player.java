package chess.player;

public abstract class Player {
    private Team team;
    private String name;

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

}
