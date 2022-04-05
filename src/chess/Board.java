package chess;

import chess.ihm.panels.PromotionPanel;
import chess.move.*;
import chess.piece.*;
import chess.player.Player;
import chess.player.Team;

import java.awt.*;
import java.util.*;
import java.util.List;
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

    private final LinkedList<Piece> capturedPiece = new LinkedList<>();
    private final LinkedList<Move> moveHistory = new LinkedList<>();
    private int moveIndex = 0;

    private final ChessAudioPlayer chessAudioPlayer = new ChessAudioPlayer();
    private boolean waiting = false;

    public Board() {
    }

    private Board(Piece[][] pieces, Player[] players, Team toPlay) {
        this.pieces = pieces;
        this.players = players;
        this.toPlay = toPlay;
    }

    public void makeMove(Move move, boolean addToHistory) {
        move.setPreMoveState(new PreMoveState(this.enPassantPossibleCapture, move.getPiece().hasMoved()));
        move.getPiece().setMoved(true);

        if (move.isCapture()) {
            move.getCapturedPiece().setAlive(false); // Dit a la pièce capturée qu'elle ne joue plus
            this.capturedPiece.add(move.getCapturedPiece()); // ajouts dans la liste des pions morts
            this.setPiece(move.getCapturedPiece().getPosition(), null);
            this.chessAudioPlayer.playCaptureSound();
        } else {
            this.chessAudioPlayer.playMoveSound();
        }

        if (move.doGenerateEnPassant()) {
            this.enPassantPossibleCapture = move.getEnPassantPossibleCapture();
        } else {
            this.enPassantPossibleCapture = null;
        }

        if (move.getCastleInfo() != null) {
            this.setPiece(move.getCastleInfo().getNewRookPosition(), move.getCastleInfo().getRook());
            this.setPiece(move.getCastleInfo().getOldRookPosition(), null);
        }

        this.setPiece(move.getStartPosition(), null);
        this.setPiece(move.getEndPosition(), move.getPiece());

        if (move.getPromotion() != null) {
            this.setPiece(move.getEndPosition(), move.getPromotion().getPiece());
        }

        this.switchTurn();

        if (addToHistory) {
            this.moveHistory.add(0, move);
        }
    }

    public void unmakeMove(Move move) {
        this.setPiece(move.getStartPosition(), move.getPiece());
        this.setPiece(move.getEndPosition(), null);

        this.enPassantPossibleCapture = move.getPreMoveState().getEnPassantPossibleCapture();
        move.getPiece().setMoved(move.getPreMoveState().hadPieceMove());

        if (move.isCapture()) {
            this.setPiece(move.getCapturedPiece().getPosition(), move.getCapturedPiece());
            move.getCapturedPiece().setAlive(true);
        }

        if (move.getCastleInfo() != null) {
            this.setPiece(move.getCastleInfo().getOldRookPosition(), move.getCastleInfo().getRook());
            this.setPiece(move.getCastleInfo().getNewRookPosition(), null);
        }

        if (move.getPromotion() != null) {
            this.setPiece(move.getStartPosition(), move.getPromotion().getPawn());
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

            this.makeMove(possibleMove, false);
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
        if (piece != null) {
            piece.setPosition(new Position(x, y));
        }
        this.pieces[x][y] = piece;
    }

    public void setPiece(Position position, Piece piece) {
        this.setPiece(position.getX(), position.getY(), piece);
    }

    public void onPressed(int x, int y) {
        if (this.waiting) {
            return;
        }

        if (this.moveIndex != 0) {
            return;
        }

        Piece piece = this.getPiece(x, y);
        if (piece != null && piece.getTeam() == this.toPlay) {
            this.selectedPiece = piece;
        } else {
            this.selectedPiece = null;
        }
    }

    public Optional<PromotionPanel> onRelease(int x, int y) {
        if (this.selectedPiece != null) {
            List<Move> moves = this.getSelectedPieceMoves(new Position(x, y));

            if (moves.size() == 1) {
                this.makeMove(moves.get(0), true);
                this.computePossibleMove();
            }
            this.selectedPiece = null;

            if (moves.size() > 1) {
                return Optional.of(new PromotionPanel(0, 0, moves, move -> {
                    this.makeMove(move, true);
                    this.computePossibleMove();
                }));
            }
        }

        return Optional.empty();
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

    public List<Move> getSelectedPieceMoves(Position endPosition) {
        if (this.selectedPiece == null) {
            return new ArrayList<>();
        }
        return this.possibleMoves.stream().filter(move -> move.getPiece() == this.selectedPiece && move.getEndPosition().equals(endPosition)).toList();
    }

    public Optional<Move> getSelectedPieceMove(Position endPosition) {
        List<Move> selectedPieceMoves = this.getSelectedPieceMoves(endPosition);
        if (selectedPieceMoves.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(this.getSelectedPieceMoves().get(0));
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

    public ChessAudioPlayer getAudioPlayer() {
        return this.chessAudioPlayer;
    }

    public LinkedList<Piece> getCapturedPiece(){
        return this.capturedPiece;
    }

    public void goBackHistory() {
        if (this.moveIndex < this.moveHistory.size()) {
            this.unmakeMove(this.moveHistory.get(this.moveIndex));
            this.moveIndex++;
        }
    }

    public void goForwardHistory() {
        if (this.moveIndex > 0) {
            this.moveIndex--;
            this.makeMove(this.moveHistory.get(this.moveIndex), false);
        }
    }

}
