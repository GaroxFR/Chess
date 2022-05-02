package chess;

import chess.piece.*;
import chess.player.Team;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ZobristHash {

    private static Map<Class<? extends Piece>, Integer > indexMap = new HashMap<>();
    private static long[][] randomTable = new long[12][64];
    private static Random random = new Random();
    private static long blackToMove = random.nextLong();

    static {
        // Initialisation
        indexMap.put(Pawn.class, 0);
        indexMap.put(Bishop.class, 1);
        indexMap.put(Knight.class, 2);
        indexMap.put(Rook.class, 3);
        indexMap.put(Queen.class, 4);
        indexMap.put(King.class, 5);

        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 64; j++) {
                randomTable[i][j] = random.nextLong();
            }
        }
    }

    public static long compute(final Piece[][] board, Team toPlay) {
        long hash = 0;
        if (toPlay == Team.BLACK) {
            hash ^= blackToMove;
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = board[i][j];
                if (piece == null) {
                    continue;
                }
                int index = 2 * indexMap.get(piece.getClass()) + piece.getTeam().getIndex();
                hash ^= randomTable[index][i * 8+j];
            }
        }

        return hash;
    }

}
