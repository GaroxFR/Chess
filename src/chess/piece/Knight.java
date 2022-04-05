package chess.piece;

import chess.Board;
import chess.move.CheckSource;
import chess.move.Move;
import chess.Position;
import chess.move.PiecePin;
import chess.player.Team;

import java.awt.Image;
import java.awt.Toolkit;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Knight extends Piece {

    private static Image ImgPieceWhite;
    private static Image ImgPieceBlack;

    static {
        Knight.ImgPieceWhite = Toolkit.getDefaultToolkit().getImage("res/cavalier_b.png");
        Knight.ImgPieceBlack = Toolkit.getDefaultToolkit().getImage("res/cavalier_n.png");
    }

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
        PiecePin pin = board.getPiecePin(this);
        
        for (Position direction : Knight.DIRECTIONS) {
           Position test = direction.add(this.position);
            if (test.isInBoard() && (pin == null || pin.isPossible(test)) && (board.getPiece(test) == null || board.getPiece(test).getTeam() != this.team)) {
                moves.add(new Move(this.position, test, this, board.getPiece(test))); //renvoie null si pas de pion
            }

        }
        return moves;
    }

    @Override
    public Set<Position> computeThreatenedPositions(Board board) {
        Set<Position> threatenedPositions = new HashSet<>();

        for (Position direction : Knight.DIRECTIONS) {

            Position test = direction.add(this.position);
            if (test.isInBoard()) {
                threatenedPositions.add(test); //renvoie null si pas de pion
                Piece threatenedPiece = board.getPiece(test);
                if (threatenedPiece != null && threatenedPiece.getTeam() != this.getTeam() && threatenedPiece instanceof King) {
                    board.addCheckSource(new CheckSource(this, new HashSet<>(List.of(this.position))));
                }
            }

        }

        return threatenedPositions;
    }

    public Image getImage() {
        if (this.team == Team.WHITE) {
            return ImgPieceWhite;
        } else {
            return ImgPieceBlack;
        }
    }

    @Override
    public int getValue() {
        return 3;
    }
}
