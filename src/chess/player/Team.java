package chess.player;

public enum Team {
    WHITE(0), BLACK(1);

    private int index;

    Team(int index) {
        this.index = index;
    }

    public int getIndex() {
        return this.index;
    }
}
