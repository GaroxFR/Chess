package chess.player;

import chess.Board;
import chess.move.Move;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class Computer extends Player{

    boolean aborted = false;

    public Computer(Team team) {
        super(team, "Ordinateur");
    }

    public Move getNextMove() {
        this.board.getAudioPlayer().setEnabled(false);

        List<Move> moves = this.board.getPossibleMoves();
        List<Move> finalMoves = new ArrayList<>();
        Map<Move, Float> evaluations = new HashMap<>();

        int depth = -1;
        this.aborted = false;
        CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.aborted = true;
        });

        while (!this.aborted) {
            depth++;
            evaluations.clear();
            for (Move move : moves) {
                this.board.makeMove(move, false);
                evaluations.put(move, -this.evaluate(depth, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY));
                this.board.unmakeMove(move);
            }
            moves.sort(Comparator.comparing(evaluations::get, Comparator.reverseOrder()));
            if (!this.aborted) {
                finalMoves = new ArrayList<>(moves);
            }
        }
        System.out.println(depth-1);
        this.board.getAudioPlayer().setEnabled(true);
        return finalMoves.get(0);
    }

    public float evaluate(int depth, float alpha, float beta) {
        if (this.aborted) {
            return 0;
        }
        if (depth == 0) {
            return this.evaluateOnlyCaptures(alpha, beta);
        }

        this.board.computePossibleMove();
        List<Move> moves = this.board.getPossibleMoves();
        if (moves.isEmpty()) {
            if (!board.getCheckSources().isEmpty()) {
                return Float.NEGATIVE_INFINITY;
            }
            return 0;
        }

        for (Move childMove : moves) {
            this.board.makeMove(childMove, false);
            float evaluation = -this.evaluate(depth - 1, -beta, -alpha);
            this.board.unmakeMove(childMove);
            if (evaluation >= beta) {
                return beta;
            }
            alpha = Math.max(alpha, evaluation);
        }
        return alpha;
    }

    public float evaluateOnlyCaptures(float alpha, float beta) {

        float evaluation = this.board.evaluate();
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
