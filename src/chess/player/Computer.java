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

    /**
     * Méthode utilisée afin de récupérer le coup de l'ordinateur.
     * L'ordinateur ayant une durée limitée pour réfléchir, il ne peut pas savoir à quelle profondeur il peut aller.
     * C'est pourquoi il lance d'abord une évaluation à la profondeur 2, puis 4, puis 6 etc
     * Après chaque évaluation, il trie la liste de coups possibles en fonction de leur évaluation.
     * Ceci permet d'accélérer l'algorithme car en évaluant les meilleurs coups en premier, on peut éviter beaucoup de calculs
     * avec l'optimisation alpha-beta pruning.
     */
    public Move getNextMove() {
        Board computationalBoard = this.board.cloneComputationalBoard();

        List<Move> finalMoves = new ArrayList<>();

        int depth = 0;
        this.aborted = false;

        // Crée une tache qui stoppera l'évaluation au bout de x seconds
        CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep((int) (this.secondsToThink * 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.aborted = true;
        });

        // Tant qu'il reste du temps de calcul
        while (!this.aborted) {
            depth++;
            this.evaluations.clear();
            computationalBoard.computePossibleMove();
            List<Move> moves = computationalBoard.getPossibleMoves();
            this.evaluate(2*depth, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, computationalBoard, true);
            // Tri de la liste en fonction des évaluations
            moves.sort(Comparator.comparing(move -> this.evaluations.getOrDefault(move, Float.NEGATIVE_INFINITY) , Comparator.reverseOrder()));
            if (!this.aborted) {
                // Seulement si la recherche a abouti
                finalMoves = new ArrayList<>(moves);
                if (this.evaluations.get(finalMoves.get(0)) == Float.POSITIVE_INFINITY) {
                    // Si on trouve un mat forcé, on le joue et on arrête la recherche.
                    return finalMoves.get(0);
                }
            }
        }

        return finalMoves.get(0);
    }

    /**
     * Méthode récursive permettant d'évaluer tous les coups possibles avec une certaine profondeur.
     * @param depth la profondeur de l'évaluation
     * @param alpha paramètre donnant l'évaluation coup trouvé pour l'instant
     * @param beta évaluation du meilleur coup pour l'adversaire pour l'instant. Permet d'éviter de calculer des positions que l'adversaire évitera forcément, car trop bon pour nous
     * @param computationalBoard le plateau sur lequel les coups sont simulés
     * @param storeEvaluation si oui ou non ous stock les évaluations dans la Map. On veut seulement stocker les évaluations lors des coups actuellement atteignable, donc seulement aux premier appel
     *                        et non aux appels récursifs de la méthode.
     * @return l'évaluation de la position
     */
    public float evaluate(int depth, float alpha, float beta, Board computationalBoard, boolean storeEvaluation) {
        if (this.aborted) {
            return 0;
        }

        if (computationalBoard.isDraw() && !storeEvaluation) {
            // Égalité par répétition
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
                // Echec et mat
                return Float.NEGATIVE_INFINITY;
            }
            // Pat
            return 0;
        }

        for (Move childMove : moves) {
            computationalBoard.makeMove(childMove);
            float evaluation = -this.evaluate(depth - 1, -beta, -alpha, computationalBoard, false);
            computationalBoard.unmakeMove(childMove);
            if (storeEvaluation && !this.aborted) {
                this.evaluations.put(childMove, evaluation);
            }
            if (evaluation >= beta) {
                return beta;
            }
            alpha = Math.max(alpha, evaluation);
        }
        return alpha;
    }

    /**
     * Fait la même chose que la méthode au-dessus, mais considère seulement les captures. Cela permet de ne pas évaluer des positions
     * où des captures sont possibles puisque compter les valeurs de pièces pouvant être prises le coup d'après
     * n'est pas très représentatif de l'évaluation réelle de la position.
     */
    public float evaluateOnlyCaptures(int limit, float alpha, float beta, Board computationalBoard) {
        if (computationalBoard.isDraw()) {
            return 0;
        }

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
