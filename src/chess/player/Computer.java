package chess.player;

import chess.Board;
import chess.move.Move;
import chess.move.component.Capture;

import java.util.*;
import java.util.concurrent.*;

public class Computer extends Player{

    private boolean aborted = false;
    private final Map<Move, Float> evaluations = new HashMap<>();
    private final float secondsToThink;

    public Computer(Team team, float secondsToThink) {
        super(team, "Ordinateur");
        this.secondsToThink = secondsToThink;
    }

    public Move getNextMove() {
        this.board.getAudioPlayer().setEnabled(false);

        Board computationalBoard = this.board.cloneComputationalBoard();

        List<Move> finalMoves = new ArrayList<>();

        int depth = 0;
        this.aborted = false;
        CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep((int) (this.secondsToThink * 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.aborted = true;
        });

        while (!this.aborted) {
            depth++;
            this.evaluations.clear();
            computationalBoard.computePossibleMove();
            List<Move> moves = computationalBoard.getPossibleMoves();
            this.evaluate(depth, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, computationalBoard, true);
            moves.sort(Comparator.comparing(move -> this.evaluations.getOrDefault(move, Float.NEGATIVE_INFINITY) , Comparator.reverseOrder()));
            if (!this.aborted) {
                finalMoves = new ArrayList<>(moves);

            }

        }
        System.out.println(depth-1);
        this.board.getAudioPlayer().setEnabled(true);
        return finalMoves.get(0);
    }

    public float evaluate(int depth, float alpha, float beta, Board computationalBoard, boolean storeEvaluation) {
        if (this.aborted) {
            return 0;
        }
        if (depth == 0) {
            return this.evaluateOnlyCaptures(100, alpha, beta, computationalBoard);
        }

        if (!storeEvaluation) {
            computationalBoard.computePossibleMove();
        }
        List<Move> moves = computationalBoard.getPossibleMoves();
        if (moves.isEmpty()) {
            if (!computationalBoard.getCheckSources().isEmpty()) {
                return Float.NEGATIVE_INFINITY;
            }
            return 0;
        }

        for (Move childMove : moves) {
            computationalBoard.makeMove(childMove);
            float evaluation = -this.evaluate(depth - 1, -beta, -alpha, computationalBoard, false);
            computationalBoard.unmakeMove(childMove);
            if (storeEvaluation) {
                this.evaluations.put(childMove, evaluation);
            }
            if (evaluation >= beta) {
                return beta;
            }
            alpha = Math.max(alpha, evaluation);
        }
        return alpha;
    }

    public float evaluateOnlyCaptures(int limit, float alpha, float beta, Board computationalBoard) {
        float evaluation = computationalBoard.evaluate();
        if (evaluation >= beta) {
            return beta;
        }
        alpha = Math.max(alpha, computationalBoard.evaluate());

        if (limit == 0) {
            return alpha;
        }

        computationalBoard.computePossibleMove();
        List<Move> moves = computationalBoard.getPossibleMoves();
        for (Move childMove : moves) {
            if (childMove.getMoveComponent(Capture.class) != null) {
                continue;
            }

            computationalBoard.makeMove(childMove);
            evaluation = -this.evaluateOnlyCaptures(limit - 1, -beta, -alpha, computationalBoard);
            computationalBoard.unmakeMove(childMove);
            if (evaluation >= beta) {
                return beta;
            }
            alpha = Math.max(alpha, evaluation);
        }

        return alpha;
    }

}
