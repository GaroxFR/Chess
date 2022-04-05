package chess.player;

import chess.Board;
import chess.move.Move;

import java.util.*;

public class Computer extends Player{

    public Computer(Team team) {
        super(team, "Ordinateur");
    }

    public Move getNextMove() {
        this.board.getAudioPlayer().setEnabled(false);

        List<Move> moves = this.board.getPossibleMoves();
        Map<Move, Double> evaluations = new HashMap<>();

        long time = System.currentTimeMillis();
        int depth = 0;
        while (System.currentTimeMillis() - time <= 2000) {
            depth++;
            evaluations.clear();
            for (Move move : moves) {
                this.board.makeMove(move, false);
                evaluations.put(move, -this.evaluate( depth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY));
                this.board.unmakeMove(move);
            }
            moves.sort(Comparator.comparing(evaluations::get, Comparator.reverseOrder()));

        }
        System.out.println(depth);
        this.board.getAudioPlayer().setEnabled(true);
        return moves.get(0);
    }

    public double evaluate(int depth, double alpha, double beta) {
        if (depth == 0) {
            return this.evaluateOnlyCaptures(alpha, beta);
        }

        this.board.computePossibleMove();
        List<Move> moves = this.board.getPossibleMoves();
        if (moves.isEmpty()) {
            if (!board.getCheckSources().isEmpty()) {
                return Double.NEGATIVE_INFINITY;
            }
            return 0;
        }

        for (Move childMove : moves) {
            this.board.makeMove(childMove, false);
            double evaluation = -this.evaluate(depth - 1, -beta, -alpha);
            this.board.unmakeMove(childMove);
            if (evaluation >= beta) {
                return beta;
            }
            alpha = Math.max(alpha, evaluation);
        }
        return alpha;
    }

    public double evaluateOnlyCaptures(double alpha, double beta) {

        double evaluation = this.board.evaluate();
        if (evaluation >= beta) {
            return beta;
        }
        alpha = Math.max(alpha, this.board.evaluate());

        this.board.computePossibleMove();
        List<Move> moves = this.board.getPossibleMoves();
        for (Move childMove : moves) {
            if (!childMove.isCapture()) {
                continue;
            }

            this.board.makeMove(childMove, false);
            evaluation = -this.evaluateOnlyCaptures(-beta, -alpha);
            this.board.unmakeMove(childMove);
            if (evaluation >= beta) {
                return beta;
            }
            alpha = Math.max(alpha, evaluation);
        }

        return alpha;
    }

}
