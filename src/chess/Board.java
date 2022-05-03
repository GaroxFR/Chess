package chess;

import chess.ihm.panels.PromotionPanel;
import chess.move.*;
import chess.move.component.Capture;
import chess.move.component.EnPassantPossibleCapture;
import chess.piece.*;
import chess.player.Computer;
import chess.player.Human;
import chess.player.Player;
import chess.player.Team;

import java.util.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Classe la plus importante du projet. Elle représente le plateau de jeu et beaucoup d'information sur le déroulement de la partie.
 */
public class Board {

    private final Piece[][] pieces = new Piece[8][8];
    private final Player[] players = new Player[2];

    private final Set<Move> possibleMoves = new HashSet<>();
    private final Set<Position> threatenedPositions = new HashSet<>();
    private final List<PiecePin> pins = new LinkedList<>();
    private final List<CheckSource> checkSources = new LinkedList<>();

    private HashMap<Long, Integer> positionMap = new HashMap<>();             // Map des position déjà atteintes utilisées pour l'égalité par répétition
    private long currentHash = 0;
    private Team toPlay = Team.WHITE;                                         // Joueur qui doit jouer dans la position affichée
    private Team timerTurn = Team.WHITE;                                      // Joueur qui doit réellement jouer (différent de toPlay puisqu'on peut revenir en arrière dans les positions visuellement)

    private Piece selectedPiece = null;                                       // Pièce tenue actuellement par le joueur humain si il est en train de drag and drop

    private EnPassantPossibleCapture enPassantPossibleCapture = null;

    private final LinkedList<Piece> capturedPiece = new LinkedList<>();
    private final LinkedList<Move> moveHistory = new LinkedList<>();
    private int moveIndex = 0;

    private final ChessAudioPlayer chessAudioPlayer = new ChessAudioPlayer();
    private boolean waitingForPromotion = false;

    public Board(Player[] players) {
        if (players.length != 2) {
            return;
        }

        for (Player player : players) {
            this.players[player.getTeam().getIndex()] = player;
            player.setBoard(this);
        }
    }

    /**
     * Constructeur utilisé pour créer une copie du plateau utilisée pour les calculs de l'ordinateur
     */
    public Board(Piece[][] pieces, EnPassantPossibleCapture enPassantPossibleCapture, Team toPlay, HashMap<Long, Integer> positionMap) {
        this.chessAudioPlayer.setEnabled(false);
        this.enPassantPossibleCapture = enPassantPossibleCapture;
        this.toPlay = toPlay;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (pieces[i][j] != null) {
                    this.pieces[i][j] = pieces[i][j].clone();
                }
            }
        }
        this.positionMap = (HashMap<Long, Integer>) positionMap.clone();
    }

    /**
     * Joue un coup définitivement et l'ajoute à l'historique
     */
    public void playMove(Move move) {
        Capture capture = move.getMoveComponent(Capture.class);
        if (capture != null) {
            this.capturedPiece.add(capture.getCapturedPiece());
            this.chessAudioPlayer.playCaptureSound();
        } else {
            this.chessAudioPlayer.playMoveSound();
        }

        this.makeMove(move);

        this.moveHistory.add(0, move);
        this.switchTimerTurn();
    }

    /**
     * Simule un coup qui peut être annulé
     */
    public void makeMove(Move move) {
        move.setPreMoveState(new PreMoveState(this.enPassantPossibleCapture, move.getPiece().hasMoved(), this.currentHash));
        move.getPiece().setMoved(true);
        this.enPassantPossibleCapture = null;

        move.apply(this);

        this.switchTurn();
        this.currentHash = ZobristHash.compute(this.pieces, this.toPlay);
        this.positionMap.compute(currentHash, (key, value) -> value == null ? 1 : value + 1);
    }

    /**
     * Annule un coup
     */
    public void unmakeMove(Move move) {
        this.enPassantPossibleCapture = move.getPreMoveState().getEnPassantPossibleCapture();
        move.getPiece().setMoved(move.getPreMoveState().hadPieceMove());

        move.revert(this);

        this.switchTurn();

        this.positionMap.compute(currentHash, (key, value) -> value == null ? 1 : value - 1);
        this.currentHash = move.getPreMoveState().getPositionHash();
    }

    /**
     * Joue le coup de l'ordinateur si c'est son tour
     */
    private void askNextMove() {
        if (this.players[this.toPlay.getIndex()] instanceof Computer) {
            // Calcul le coup de l'ordinateur de façon asynchrone afin de ne pas bloquer le Thread principal le temps du calcul
            CompletableFuture.supplyAsync(() -> ((Computer) this.players[this.toPlay.getIndex()]).getNextMove())
                    .thenAcceptAsync(move -> {
                        this.restoreHistory();
                        this.playMove(move);
                        this.computePossibleMove();
                        this.askNextMove();
                    }).whenComplete((unused, throwable) -> throwable.printStackTrace());

        }
    }



    public void switchTurn() {
        if (this.toPlay == Team.WHITE) {
            this.toPlay = Team.BLACK;
        } else {
            this.toPlay = Team.WHITE;
        }
    }

    public void switchTimerTurn() {
        if (this.timerTurn == Team.WHITE) {
            this.timerTurn = Team.BLACK;
        } else {
            this.timerTurn = Team.WHITE;
        }
    }

    /**
     * Evalue la position actuelle.
     */
    public float evaluate() {
        float friendlyPiecesValue = 0;
        float opponentPiecesValue = 0;
        Piece friendlyKing = null;
        Piece opponentKing = null;
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Piece piece = this.getPiece(x, y);
                if (piece != null && piece.isAlive()) {
                    if (piece.getTeam() == this.toPlay) {
                        friendlyPiecesValue += piece.getValue();
                        if (piece instanceof King) {
                            friendlyKing = piece;
                        }
                    } else {
                        opponentPiecesValue += piece.getValue();
                        if (piece instanceof King) {
                            opponentKing = piece;
                        }
                    }
                }
            }
        }

        int opponentKingDistToCenter = opponentKing.getPosition().add(-3, -3).simpleNorm();
        int friendlyKingDistToOpponentKing = 14 - opponentKing.getPosition().add(friendlyKing.getPosition().multiply(-1)).simpleNorm();
        float forceKingToCornerEval = (opponentKingDistToCenter  + friendlyKingDistToOpponentKing*5)  * (4000 - opponentPiecesValue) / 4000.0f;
        return friendlyPiecesValue - opponentPiecesValue + forceKingToCornerEval;
    }

    /**
     * Compte le nombre de positions possible après un nombre d'un coup donné. Cette fonction était utilisée pour tester la génération et vérifier par rapport aux conventions
     * déjà établi
     */
    public int countPossibleMoves(int depth) {
        if (depth == 0) {
            return 1;
        }

        int sum = 0;
        this.computePossibleMove();
        Set<Move> copyMove =  new HashSet<>(this.possibleMoves);
        for (Move possibleMove : copyMove) {

            this.makeMove(possibleMove);
            sum += this.countPossibleMoves(depth - 1);
            this.unmakeMove(possibleMove);
        }
        return sum;
    }

    /**
     * Méthode centrale mettant à jour a liste des coups possibles par rapport à la position actuelle
     */
    public void computePossibleMove() {
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
            Set<Position> resolvingPositions = new HashSet<>();;
            if (this.checkSources.size() == 1) {
                for (CheckSource checkSource : this.checkSources) {
                    resolvingPositions = checkSource.getResolvingPositions();
                }
            }

            Set<Move> illegalMove = new HashSet<>();
            for (Move possibleMove : this.possibleMoves) {
                if (!(possibleMove.getPiece() instanceof King) && !resolvingPositions.contains(possibleMove.getEndPosition())) {
                    illegalMove.add(possibleMove);
                }
            }
            this.possibleMoves.removeAll(illegalMove);
        }
    }

    /**
     * Charge une position à partir de la position à partir d'une chaine de caractère
     */
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
                this.setPiece(new Position(x, y), piece);
                x++;
            }
        }

        if (splitted[1].charAt(0) == 'w') {
            this.toPlay = Team.WHITE;
            this.timerTurn = Team.WHITE;
        } else {
            this.toPlay = Team.BLACK;
            this.timerTurn = Team.BLACK;
        }

        for(char c : splitted[2].toCharArray()) {
            if(c == '-'){
                break;
            }
            if(c == 'k'){
                this.getPiece(7,7).setMoved(false);
            }
            if(c == 'q'){
                this.getPiece(0,7).setMoved(false);
            }
            if(c == 'K'){
                this.getPiece(7,0).setMoved(false);
            }
            if(c == 'Q'){
                this.getPiece(0,0).setMoved(false);
            }

        }



        this.computePossibleMove();
        this.askNextMove();
    }

    public Piece getPiece(int x, int y) {
        return this.pieces[x][y];
    }

    public Piece getPiece(Position position) {
        return this.getPiece(position.getX(), position.getY());
    }


    public void setPiece(Position position, Piece piece) {
        if (piece != null) {
            piece.setPosition(position);
        }
        this.pieces[position.getX()][position.getY()] = piece;
    }

    /**
     * Méthode appelée quand le joueur enfonce la clique de la souris
     */
    public void onPressed(int x, int y) {
        if (this.waitingForPromotion || !(this.players[this.toPlay.getIndex()] instanceof Human)) {
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

    /**
     * Méthode appelée lorsque le joueur relache le clique de la souris
     * @return un PromotionPanel si jamais le joueur doit maintenant choisir une Promotion
     */
    public Optional<PromotionPanel> onRelease(int x, int y) {
        if (this.selectedPiece != null) {
            List<Move> moves = this.getSelectedPieceMoves(new Position(x, y));

            if (moves.size() == 1) {
                this.playMove(moves.get(0));
                this.computePossibleMove();
                this.askNextMove();
            }
            this.selectedPiece = null;

            if (moves.size() > 1) {
                this.waitingForPromotion = true;
                return Optional.of(new PromotionPanel(moves, move -> {
                    this.playMove(move);
                    this.computePossibleMove();
                    this.waitingForPromotion = false;
                    this.askNextMove();
                }));
            }
        }

        return Optional.empty();
    }

    public Piece getSelectedPiece() {
        return this.selectedPiece;
    }

    public List<Move> getPossibleMoves() {
        return new ArrayList<>(this.possibleMoves);
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

    public EnPassantPossibleCapture getEnPassantPossibleCapture() {
        return this.enPassantPossibleCapture;
    }

    public void setEnPassantPossibleCapture(EnPassantPossibleCapture enPassantPossibleCapture) {
        this.enPassantPossibleCapture = enPassantPossibleCapture;
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
            this.makeMove(this.moveHistory.get(this.moveIndex));
        }
    }

    public void restoreHistory() {
        while (this.moveIndex > 0) {
            this.goForwardHistory();
        }
    }

    public Board cloneComputationalBoard() {
        return new Board(this.pieces, this.enPassantPossibleCapture, this.toPlay, this.positionMap);
    }

    public Team getToPlay() {
        return this.toPlay;
    }

    public Player[] getPlayers() {
        return this.players;
    }

    public boolean isDraw() {
        return this.positionMap.getOrDefault(this.currentHash, 0) >= 3;
    }

    public Team getTimerTurn() {
        return this.timerTurn;
    }
}
