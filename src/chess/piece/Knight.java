package chess.piece;

import chess.Board;
import chess.Move;
import chess.Position;
import chess.player.Team;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Knight extends Piece {
    private int x;
    private int y;

    public Knight(Team team, Position position) {
        super(team, position);

    }

    public Set<Move> computePossibleMoves(Board board) {
        Set<Move> moves = new HashSet<>();

        ArrayList<Position> casePossible = new ArrayList<Position>(); //maybe changer en tableau statique
        casePossible.add(this.position.add(1, 2));
        casePossible.add(this.position.add(1, -2));
        casePossible.add(this.position.add(2, 1));
        casePossible.add(this.position.add(2, -1));
        casePossible.add(this.position.add(-2, 1));
        casePossible.add(this.position.add(-2, -1));
        casePossible.add(this.position.add(-1, 2));
        casePossible.add(this.position.add(-1, -2));

        for (Position test : casePossible) {
            if (test.isInBoard() && (board.getPiece(test) == null || (board.getPiece(test) != null && board.getPiece(test).getTeam() != this.team)) ) {
                moves.add(new Move(this.position, test, this,board.getPiece(test))); //renvoie null si pas de pion
            }

        }
        return moves;
    }
}
