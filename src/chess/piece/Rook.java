package chess.piece;

import chess.Board;
import chess.Move;
import chess.Position;
import chess.player.Team;

import java.util.HashSet;
import java.util.Set;

public class Rook extends Piece{

    private boolean hasMoved; //Pour le roque

    public Rook (Team team, Position position) {
        super(team, position);
        hasMoved = false ;
    }

    public Set<Move> computePossibleMoves(Board board) {
        Set<Move> moves = new HashSet<>();
        for(int i = -1; i <= 1; i+=2) {
            Position nextPosition = this.position.add(0, i);
            while (nextPosition.isInBoard()) {
                if( board.getPiece(nextPosition) == null) {
                    moves.add(new Move(this.position, nextPosition, this));
                    nextPosition = nextPosition.add(0, i);
                }
                if (board.getPiece(nextPosition) != null && board.getPiece(nextPosition).getTeam() != this.team) {
                    moves.add(new Move(this.position, nextPosition, this, board.getPiece(this.position)));
                    break;
                }
            }
        }
        for(int i = -1; i <= 1; i+=2) {
            Position nextPosition = this.position.add(i, 0);
            while (nextPosition.isInBoard()) {
                if( board.getPiece(nextPosition) == null) {
                    moves.add(new Move(this.position, nextPosition, this));
                    nextPosition = nextPosition.add(i, 0);
                }
                if (board.getPiece(nextPosition) != null && board.getPiece(nextPosition).getTeam() != this.team) {
                    moves.add(new Move(this.position, nextPosition, this, board.getPiece(this.position)));
                    break;
                }
            }
        }
        return moves;
    }


}
