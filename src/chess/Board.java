package chess;

import chess.move.CheckSource;
import chess.move.EnPassantPossibleCapture;
import chess.move.Move;
import chess.move.PiecePin;
import chess.piece.*;
import chess.player.Player;
import chess.player.Team;

import java.util.*;
import java.util.stream.Collectors;

public class Board {

    private Piece[][] pieces = new Piece[8][8];
    private Player[] players = new Player[2];
    private Set<Move> possibleMoves = new HashSet<>();
    private final Set<Position> threatenedPositions = new HashSet<>();
    private final List<PiecePin> pins = new LinkedList<>();
    private final List<CheckSource> checkSources = new LinkedList<>();
    private Team toPlay = Team.WHITE;

    private Piece selectedPiece = null;
    private EnPassantPossibleCapture enPassantPossibleCapture = null;
    private EnPassantPossibleCapture previousEnPassantPossibleCapture = null;

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

        this.previousEnPassantPossibleCapture = this.enPassantPossibleCapture;
        if (move.doGenerateEnPassant()) {
            this.enPassantPossibleCapture = move.getEnPassantPossibleCapture();
        } else {
            this.enPassantPossibleCapture = null;
        }

        this.setPiece(move.getStartPosition(), null);
        this.setPiece(move.getEndPosition(), move.getPiece());
        this.switchTurn();
    }

    public void unmakeMove(Move move) {
        this.setPiece(move.getStartPosition(), move.getPiece());
        this.setPiece(move.getEndPosition(), null);

        this.enPassantPossibleCapture = this.previousEnPassantPossibleCapture;

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
        this.threatenedPositions.clear();
        this.pins.clear();
        this.checkSources.clear();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = this.getPiece(i, j);
                if (piece != null && piece.isAlive() && piece.getTeam() != this.toPlay) {
                    this.threatenedPositions.addAll(piece.computeThreatenedPositions(this));
                    if (piece instanceof SlidingPiece) {
                        ((SlidingPiece) piece).computePiecePin(this).ifPresent(this.pins::add);
                    }
                }
            }
        }

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = this.getPiece(i, j);
                if (piece != null && piece.isAlive() && piece.getTeam() == this.toPlay) {
                    this.possibleMoves.addAll(piece.computePossibleMoves(this));
                }
            }
        }

        if (!this.checkSources.isEmpty()) {
            Set<Position> resolvingPositions = new HashSet<>(this.checkSources.get(0).getResolvingPositions());
            for (CheckSource checkSource : this.checkSources) {
                resolvingPositions.retainAll(checkSource.getResolvingPositions());
            }

            this.possibleMoves = this.possibleMoves
                    .stream()
                    .filter(move -> move.getPiece() instanceof King || resolvingPositions.contains(move.getEndPosition()))
                    .collect(Collectors.toSet());
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

    public void onPressed(int x, int y) {
        Piece piece = this.getPiece(x, y);
        if (piece != null && piece.getTeam() == this.toPlay) {
            this.selectedPiece = piece;
        } else {
            this.selectedPiece = null;
        }
    }

    public void onRelease(int x, int y) {
        if (this.selectedPiece != null) {
            Optional<Move> moveOptional = this.getSelectedPieceMove(new Position(x, y));
            if (moveOptional.isPresent()) {
                this.makeMove(moveOptional.get());
                this.computePossibleMove();
            }
            this.selectedPiece = null;
        }
    }

    public Piece getSelectedPiece() {
        return this.selectedPiece;
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

    public EnPassantPossibleCapture getEnPassantPossibleCapture() {
        return this.enPassantPossibleCapture;
    }

    public boolean isThreatened(Position position) {
        return this.threatenedPositions.contains(position);
    }

    public Set<Position> getThreatenedPositions() {
        return this.threatenedPositions;
    }

    public List<PiecePin> getPins() {
        return this.pins;
    }

    public PiecePin getPiecePin(Piece piece) {
        for (PiecePin pin : this.pins) {
            if (pin.getPiece().equals(piece)) {
                return pin;
            }
        }

        return null;
    }

    public List<CheckSource> getCheckSources() {
        return this.checkSources;
    }

    public void addCheckSource(CheckSource source) {
        this.checkSources.add(source);
    }
}
