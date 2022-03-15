package chess.piece;

import chess.Board;
import chess.Move;
import chess.Position;
import chess.player.Team;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Knight extends Piece {
    
    private static final Position[] DIRECTIONS = {
            new Position(1, 2),
            new Position(1, -2),
            new Position(2, 1),
            new Position(2, -1),
            new Position(-1, 2),
            new Position(-1, -2),
            new Position(-2, 1),
            new Position(-2, -1)
    };
    

    public Knight(Team team, Position position) {
        super(team, position);

    }

    public Set<Move> computePossibleMoves(Board board) {
        Set<Move> moves = new HashSet<>();
        
        
        for (Position direction : DIRECTIONS) {
        
           Position test = direction.add(this.position);
            if (test.isInBoard() && (board.getPiece(test) == null || (board.getPiece(test) != null && board.getPiece(test).getTeam() != this.team))) {
                moves.add(new Move(this.position, test, this, board.getPiece(test))); //renvoie null si pas de pion
            }

        }
        return moves;
    }

    
}
