package chess.player;

import chess.Board;
import chess.move.Move;

import java.util.*;

public class Computer extends Player{


    public Computer(Team team) {
        super(team, "Ordinateur");
        this.board = board;
    }

    public Move getNextMove() {
        this.board.getAudioPlayer().setEnabled(false);
        List<Move> moves = this.board.getPossibleMoves();
        Map<Move, Double> evaluations = new HashMap<>();
        for (Move move : moves) {
            evaluations.put(move, this.evaluate(move, 3));
        }
        this.board.getAudioPlayer().setEnabled(true);
        return evaluations.entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).get();
    }

    public double evaluate(Move move, int depth) {
        if (depth == 0) {
            return this.board.evaluate();
        }
        this.board.makeMove(move, false);
        this.board.computePossibleMove();
        List<Move> moves = this.board.getPossibleMoves();
        double max = Double.NEGATIVE_INFINITY;
        for (Move childMove : moves) {
            max = Math.max(max, -this.evaluate(childMove, depth - 1));
        }
        this.board.unmakeMove(move);
        return max;
    }
}
