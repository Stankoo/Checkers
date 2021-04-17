package com.kodilla.checkers;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import static com.kodilla.checkers.CheckersData.WHITE;
import static com.kodilla.checkers.CheckersData.BLACK;
import static com.kodilla.checkers.CheckersData.BLACK_KING;
import static com.kodilla.checkers.CheckersData.WHITE_KING;

public class CheckersBoard extends Canvas {
    public static final int PieceSize = 28;
    public static final int HIGHLIGHT_LINE = 4;
    private final CheckersData board;
    private int selectedRow, selectedCol;
    CheckersMove[] legalMoves;
    private final Label message;
    boolean gameInProgress;
    private final Button newGameButton;
    private final Button resignButton;
    int currentPlayer;
    private final GraphicsContext g = getGraphicsContext2D();
    private final int BOARD_SIZE = 8;
    private final int TILE_SIZE = 40;


    CheckersBoard(Label message, boolean gameInProgress, int currentPlayer, Button newGameButton, Button resignButton) {
        super(324, 324);
        this.gameInProgress = gameInProgress;
        this.currentPlayer = currentPlayer;
        this.message = message;
        this.newGameButton = newGameButton;
        this.resignButton = resignButton;
        board = new CheckersData();
        doNewGame();
    }

    void doNewGame() {
        if (gameInProgress) {
            message.setText("Finish the current game first!");
            return;
        }
        board.setUpGame();
        currentPlayer = WHITE;
        legalMoves = board.getLegalMoves(WHITE);
        selectedRow = -1;
        message.setText("White:  Make your move.");
        gameInProgress = true;
        newGameButton.setDisable(true);
        resignButton.setDisable(false);
        drawBoard();
    }

    void doResign() {

        if (currentPlayer == WHITE)
            gameOver("WHITE resigns.  BLACK wins.");
        else
            gameOver("BLACK resigns.  WHITE wins.");
    }

    void gameOver(String str) {
        message.setText(str);
        newGameButton.setDisable(false);
        resignButton.setDisable(true);
        gameInProgress = false;
    }

    void doClickSquare(int row, int col) {
        for (CheckersMove legalMove : legalMoves)
            if (legalMove.fromRow == row && legalMove.fromCol == col) {
                selectedRow = row;
                selectedCol = col;
                if (currentPlayer == WHITE)
                    message.setText("WHITE:  Make your move.");
                else
                    message.setText("BLACK:  Make your move.");
                drawBoard();
                return;
            }
        if (selectedRow < 0) {
            message.setText("Click on the piece you want to move.");
            return;
        }
        for (CheckersMove legalMove : legalMoves)
            if (legalMove.fromRow == selectedRow && legalMove.fromCol == selectedCol
                    && legalMove.toRow == row && legalMove.toCol == col) {
                doMakeMove(legalMove);
                return;
            }

        message.setText("Click the square you want to move to.");

    }

    void doMakeMove(CheckersMove move) {
        board.makeMove(move);
        if (move.isJump()) {
            legalMoves = board.getLegalJumpsFrom(currentPlayer, move.toRow, move.toCol);
            if (legalMoves != null) {
                if (currentPlayer == WHITE)
                    message.setText("WHITE:  You must continue jumping.");
                else
                    message.setText("BLACK:  You must continue jumping.");
                selectedRow = move.toRow;
                selectedCol = move.toCol;
                drawBoard();
                return;
            }
        }
        if (currentPlayer == WHITE) {
            currentPlayer = BLACK;
            legalMoves = board.getLegalMoves(currentPlayer);
            if (legalMoves == null)
                gameOver("BLACK has no moves. WHITE wins.");
            else if (legalMoves[0].isJump())
                message.setText("BLACK:  Make your move.  You must jump.");
            else
                message.setText("BLACK:  Make your move.");
        } else {
            currentPlayer = WHITE;
            legalMoves = board.getLegalMoves(currentPlayer);
            if (legalMoves == null)
                gameOver("WHITE has no moves.  BLACK wins.");
            else if (legalMoves[0].isJump())
                message.setText("WHITE:  Make your move.  You must jump.");
            else
                message.setText("WHITE:  Make your move.");
        }

        selectedRow = -1;
        if (legalMoves != null) {
            boolean sameStartSquare = true;
            for (int i = 1; i < legalMoves.length; i++)
                if (isNormalMove(legalMoves[i])) {
                    sameStartSquare = false;
                    break;
                }
            if (sameStartSquare) {
                selectedRow = legalMoves[0].fromRow;
                selectedCol = legalMoves[0].fromCol;
            }
        }
        drawBoard();
    }

    private boolean isNormalMove(CheckersMove legalMove) {
        return legalMove.fromRow != legalMoves[0].fromRow
                || legalMove.fromCol != legalMoves[0].fromCol;
    }

    public void initGraphicContext() {
        GraphicsContext g = getGraphicsContext2D();
        g.setFont(Font.font(18));
        g.setStroke(Color.BLACK);
        g.setLineWidth(2);
        g.strokeRect(1, 1, 322, 322);
    }

    public void drawBoard() {
        initGraphicContext();

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if (row % 2 == col % 2)
                    g.setFill(Color.LIGHTGRAY);
                else
                    g.setFill(Color.DARKGRAY);
                g.fillRect(2 + col * TILE_SIZE, 2 + row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                drawPiece(row, col);
            }
        }

        if (gameInProgress) {
            g.setStroke(Color.CYAN);
            g.setLineWidth(HIGHLIGHT_LINE);

            for (CheckersMove legalMove : legalMoves) {
                drawAvailableMoveStroke(legalMove.fromCol, legalMove.fromRow);
            }
            if (selectedRow >= 0) {
                g.setStroke(Color.YELLOW);
                g.setLineWidth(HIGHLIGHT_LINE);
                drawAvailableMoveStroke(selectedCol, selectedRow);
                g.setStroke(Color.LIME);
                g.setLineWidth(HIGHLIGHT_LINE);
                for (CheckersMove legalMove : legalMoves) {
                    if (legalMove.fromCol == selectedCol && legalMove.fromRow == selectedRow) {
                        drawAvailableMoveStroke(legalMove.toCol, legalMove.toRow);
                    }
                }
            }
        }

    }

    private void drawAvailableMoveStroke(int fromCol, int fromRow) {
        g.strokeRect(HIGHLIGHT_LINE + fromCol * TILE_SIZE, HIGHLIGHT_LINE + fromRow * TILE_SIZE, 36, 36);
    }

    private void drawPiece(int row, int col,Color c,boolean isKing) {
        g.setFill(c);
        g.fillOval(BOARD_SIZE + col * TILE_SIZE, BOARD_SIZE + row * TILE_SIZE, PieceSize, PieceSize);
        if (!isKing)
            return;
        g.setFill(Color.BLACK);
        g.fillText("K", 15 + col * TILE_SIZE, 29 + row * TILE_SIZE);
    }
    private void drawPiece(int row, int col) {
        switch (board.pieceAt(row, col)) {
            case WHITE:
                drawPiece(row,col,Color.WHITE,false);
                break;
            case BLACK:
                drawPiece(row,col,Color.BLACK,false);
                break;
            case WHITE_KING:
                drawPiece(row,col,Color.WHITE,true);
                break;
            case BLACK_KING:
                drawPiece(row,col,Color.BLACK,true);
                break;
        }
    }

    public void mousePressed(MouseEvent evt) {
        if (!gameInProgress)
            message.setText("Click \"New Game\" to start a new game.");
        else {
            int col = (int) ((evt.getX() - 2) / TILE_SIZE);
            int row = (int) ((evt.getY() - 2) / TILE_SIZE);
            if (col >= 0 && col < BOARD_SIZE && row >= 0 && row < BOARD_SIZE)
                doClickSquare(row, col);
        }
    }
}