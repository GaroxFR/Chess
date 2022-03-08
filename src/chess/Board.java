package chess;

import chess.piece.Piece;
import chess.player.Player;
import chess.player.Team;

import java.util.HashSet;
import java.util.Set;

public class Board {

    private Piece[][] pieces = new Piece[8][8];
    private Player[] players = new Player[2];
    private final Set<Move> possibleMoves = new HashSet<>();

    public Piece getPiece(int x, int y) {
        return this.pieces[x][y];
    }

    public Piece getPiece(Position position) {
        return this.getPiece(position.getX(), position.getY());
    }
}
