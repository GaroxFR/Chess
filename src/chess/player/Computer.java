package chess.player;

import chess.Board;
import chess.move.Move;

import java.util.*;
import java.util.concurrent.*;

public class Computer extends Player{

    private boolean aborted = false;

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

        List<CompletableFuture<?>> futures = new LinkedList<>();
        while (!this.aborted) {
            depth++;
            final int d = depth;
            evaluations.clear();
            for (Move move : moves) {
                futures.add(CompletableFuture.runAsync(() -> {
                    Board computationalBoard = this.board.cloneComputationalBoard();
                    this.board.makeMove(move, false);
                    evaluations.put(move, -this.evaluate(d, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, computationalBoard));
                    this.board.unmakeMove(move);
                }));
            }
            try {
                CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
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

    public float evaluate(int depth, float alpha, float beta, Board computationalBoard) {
        if (this.aborted) {
            return 0;
        }
        if (depth == 0) {
            return this.evaluateOnlyCaptures(alpha, beta, computationalBoard);
        }

        computationalBoard.computePossibleMove();
        List<Move> moves = this.board.getPossibleMoves();
        if (moves.isEmpty()) {
            if (!computationalBoard.getCheckSources().isEmpty()) {
                return Float.NEGATIVE_INFINITY;
            }
            return 0;
        }

        for (Move childMove : moves) {
            computationalBoard.makeMove(childMove, false);
            float evaluation = -this.evaluate(depth - 1, -beta, -alpha, computationalBoard);
            computationalBoard.unmakeMove(childMove);
            if (evaluation >= beta) {
                return beta;
            }
            alpha = Math.max(alpha, evaluation);
        }
        return alpha;
    }

    public float evaluateOnlyCaptures(float alpha, float beta, Board computationalBoard) {

        float evaluation = this.board.evaluate();
        if (evaluation >= beta) {
            return beta;
        }
        alpha = Math.max(alpha, computationalBoard.evaluate());

        computationalBoard.computePossibleMove();
        List<Move> moves = this.board.getPossibleMoves();
        for (Move childMove : moves) {
            if (!childMove.isCapture()) {
                continue;
            }

            computationalBoard.makeMove(childMove, false);
            evaluation = -this.evaluateOnlyCaptures(-beta, -alpha, computationalBoard);
            computationalBoard.unmakeMove(childMove);
            if (evaluation >= beta) {
                return beta;
            }
            alpha = Math.max(alpha, evaluation);
        }

        return alpha;
    }

}
