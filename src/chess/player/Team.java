package chess.player;

public enum Team {
    WHITE(0), BLACK(1);

    // Il est intéressant d'attribuer 0 et 1 à chacune des teams pour certains calculs
    private int index;

    Team(int index) {
        this.index = index;
    }

    public int getIndex() {
        return this.index;
    }
}
