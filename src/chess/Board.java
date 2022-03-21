package chess;

import chess.piece.*;
import chess.player.Player;
import chess.player.Team;

import java.util.*;

public class Board {

    private Piece[][] pieces = new Piece[8][8];
    private Player[] players = new Player[2];
    private final Set<Move> possibleMoves = new HashSet<>();
    private Team toPlay = Team.WHITE;

    private Piece selectedPiece = null;

    public Board() {
    }

    private Board(Piece[][] pieces, Player[] players, Team toPlay) {
        this.pieces = pieces;
        this.players = players;
        this.toPlay = toPlay;
    }

    public void makeMove(Move move) {
        if (move.isCapture()) {
            move.getCapturedPiece().setAlive(false); // Dit a la pièce capturée qu'elle ne joue plus
            this.setPiece(move.getCapturedPiece().getPosition(), null);
        }

        this.setPiece(move.getStartPosition(), null);
        this.setPiece(move.getEndPosition(), move.getPiece());
        this.switchTurn();
    }

    public void unmakeMove(Move move) {
        this.setPiece(move.getStartPosition(), move.getPiece());
        this.setPiece(move.getEndPosition(), null);

        if (move.isCapture()) {
            this.setPiece(move.getCapturedPiece().getPosition(), move.getCapturedPiece());
            move.getCapturedPiece().setAlive(true);
        }

        this.switchTurn();
    }

    public void switchTurn() {
        if (this.toPlay == Team.WHITE) {
            this.toPlay = Team.BLACK;
        } else {
            this.toPlay = Team.WHITE;
        }
    }

    public int countPossibleMoves(int depth) {
        if (depth == 0) {
            return 1;
        }

        int sum = 0;
        this.computePossibleMove();
        Set<Move> copyMove =  new HashSet<>(this.possibleMoves);
        for (Move possibleMove : copyMove) {

            this.makeMove(possibleMove);
            /*BoardPanel.INSTANCE.repaint();
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
            sum += this.countPossibleMoves(depth - 1);
            this.unmakeMove(possibleMove);
        }
        return sum;
    }

    private void computePossibleMove() {
        this.possibleMoves.clear();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = this.getPiece(i, j);
                if (piece != null && piece.isAlive() && piece.getTeam() == this.toPlay) {
                    this.possibleMoves.addAll(piece.computePossibleMoves(this));
                }
            }
        }
    }

    public void loadFEN(String fen) {
        int x = 0;
        int y = 7;
        String[] splitted = fen.split(" ");
        for (char c : splitted[0].toCharArray()) {
            if (c == '/') {
                y--;
                x = 0;
            } else if (Character.isDigit(c)) {
                int i = Integer.parseInt(Character.toString(c));
                x += i;
            } else {
                Piece piece = switch (c) {
                    case 'p' -> new Pawn(Team.BLACK, new Position(x, y));
                    case 'P' -> new Pawn(Team.WHITE, new Position(x, y));
                    case 'k' -> new King(Team.BLACK, new Position(x, y));
                    case 'K' -> new King(Team.WHITE, new Position(x, y));
                    case 'q' -> new Queen(Team.BLACK, new Position(x, y));
                    case 'Q' -> new Queen(Team.WHITE, new Position(x, y));
                    case 'n' -> new Knight(Team.BLACK, new Position(x, y));
                    case 'N' -> new Knight(Team.WHITE, new Position(x, y));
                    case 'b' -> new Bishop(Team.BLACK, new Position(x, y));
                    case 'B' -> new Bishop(Team.WHITE, new Position(x, y));
                    case 'r' -> new Rook(Team.BLACK, new Position(x, y));
                    case 'R' -> new Rook(Team.WHITE, new Position(x, y));
                    default -> null;
                };
                this.setPiece(x, y, piece);
                x++;
            }
        }

        if (splitted[1].charAt(0) == 'w') {
            this.toPlay = Team.WHITE;
        } else {
            this.toPlay = Team.BLACK;
        }

        this.computePossibleMove();
    }

    public Piece getPiece(int x, int y) {
        return this.pieces[x][y];
    }

    public Piece getPiece(Position position) {
        return this.getPiece(position.getX(), position.getY());
    }

    public void setPiece(int x, int y, Piece piece) {
        this.pieces[x][y] = piece;
    }

    public void setPiece(Position position, Piece piece) {
        if (piece != null) {
            piece.setPosition(position);
        }
        this.setPiece(position.getX(), position.getY(), piece);
    }

    public void onClick(int x, int y) {
        Piece piece = this.getPiece(x, y);
        if (piece != null && piece.getTeam() == this.toPlay) {
            this.selectedPiece = piece;
        } else {
            Optional<Move> moveOptional = this.getSelectedPieceMove(new Position(x, y));
            if (moveOptional.isPresent()) {
                this.makeMove(moveOptional.get());
                this.computePossibleMove();
            }
            this.selectedPiece = null;
        }
    }

    public List<Move> getSelectedPieceMoves() {
        if (this.selectedPiece == null) {
            return new ArrayList<>();
        }
        return this.possibleMoves.stream().filter(move -> move.getPiece() == this.selectedPiece).toList();
    }

    public Optional<Move> getSelectedPieceMove(Position endPosition) {
        if (this.selectedPiece == null) {
            return Optional.empty();
        }
        return this.possibleMoves.stream().filter(move -> move.getPiece() == this.selectedPiece && move.getEndPosition().equals(endPosition)).findFirst();
    }
}
