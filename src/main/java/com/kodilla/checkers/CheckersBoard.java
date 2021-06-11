package com.kodilla.checkers;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;

import static com.kodilla.checkers.Team.BLACK;
import static com.kodilla.checkers.Team.WHITE;


public class CheckersBoard extends Canvas {
    private final Checkers checkers;
    private final CheckersData board;
    private final Computer computer;
    private int selectedRow, selectedCol;
   private boolean gameInProgress;
    public Team currentPlayer;
    private boolean gameVsComputer;

    private ArrayList<CheckersMove> legalMoves = new ArrayList<>();



    CheckersBoard(Checkers checkers) {
        super(324, 324);
      this.checkers = checkers;
      computer = new Computer();
        board = new CheckersData();
        doNewGameVsPlayer();
    }

    void doNewGameVsPlayer() {
        if (gameInProgress) {
            checkers.message.setText("Finish the current game first!");
            return;
        }
        board.setUpGame();
        currentPlayer = WHITE;
        gameVsComputer = false;
        legalMoves = board.getLegalMoves(WHITE);
        selectedRow = -1;
        checkers.message.setText("White:  Make your move.");
        gameInProgress = true;
        checkers.newPlayersGameButton.setDisable(true);
        checkers.newGameVsComputerButton.setDisable(false);
        checkers.resignButton.setDisable(false);
        drawBoard();
    }

    public void doNewGameVsComputer() {
        if (gameInProgress) {
            checkers.message.setText("Finish the current game first!");
            return;
        }
        board.setUpGame();
        currentPlayer = WHITE;
        gameVsComputer = true;
        legalMoves = board.getLegalMoves(WHITE);
        selectedRow = -1;
        checkers.message.setText("White:  Make your move.");
        gameInProgress = true;
        checkers.newGameVsComputerButton.setDisable(true);
        checkers.newPlayersGameButton.setDisable(false);
        checkers.resignButton.setDisable(false);
        drawBoard();
    }

    void doResign() {
        if (!gameInProgress) {
            checkers.message.setText("There is no game in progress!");
            return;
        }

        if (currentPlayer == Team.WHITE)
            gameOver("WHITE resigns.  BLACK wins.");
        else
            gameOver("BLACK resigns.  WHITE wins.");
    }

    void gameOver(String str) {
        checkers.message.setText(str);
        checkers.newPlayersGameButton.setDisable(false);
        checkers.newGameVsComputerButton.setDisable(false);
        checkers.resignButton.setDisable(true);
        gameInProgress = false;
    }

    void doClickSquare(int id) {
        for (CheckersMove legalMove : legalMoves)
            if (legalMove.getFromField().getId() == id) {
                selectedRow = id / 10;
                selectedCol = id % 10;
                if (currentPlayer == WHITE)
                    checkers.message.setText("WHITE:  Make your move.");
                else
                    checkers.message.setText("BLACK:  Make your move.");
                drawBoard();
                return;
            }
        if (selectedRow < 0) {
            checkers.message.setText("Click on the piece you want to move.");
            return;
        }

        legalMoves.clear();
        legalMoves = board.getLegalMoves(currentPlayer);


        for (CheckersMove legalMove : legalMoves)
            if (legalMove.getFromField().getRow() == selectedRow && legalMove.getFromField().getCol() == selectedCol
                    && legalMove.getToField().getId() == id) {
                doMakeMove(legalMove);
                return;
            }

        checkers.message.setText("Click the square you want to move to.");

    }

    void doMakeMove(CheckersMove move) {
        board.makeMove(move);
        if (move.isJump()) {
            legalMoves.clear();
            legalMoves = board.getLegalJumpsFor(currentPlayer, move.getToField().getId());
            if (legalMoves != null) {
                if (currentPlayer == WHITE)
                    checkers.message.setText("WHITE:  You must continue jumping.");
                else
                    checkers.message.setText("BLACK:  You must continue jumping.");
                selectedRow = move.getToField().getRow();
                selectedCol = move.getToField().getCol();
                drawBoard();
                return;
            }
        }
        if (currentPlayer == WHITE) {
            blackPlayerTurn(move);
        } else {
            whitePlayerTurn(move);
        }
        selectedRow = -1;
        drawBoard();
    }
    private void whitePlayerTurn(CheckersMove move) {
        currentPlayer = WHITE;

        legalMoves = board.getLegalMoves(currentPlayer);
        if (legalMoves == null)
            gameOver("WHITE has no moves.  BLACK wins.");
        else if (move.isJump())
            checkers.message.setText("WHITE:  Make your move.  You must jump.");
        else
            checkers.message.setText("WHITE:  Make your move.");
    }

    private void blackPlayerTurn(CheckersMove move) {
        currentPlayer = BLACK;
        legalMoves = board.getLegalMoves(currentPlayer);
        if (legalMoves == null)
            gameOver("BLACK has no moves. WHITE wins.");
        else if (move.isJump())
            checkers.message.setText("BLACK:  Make your move.  You must jump.");
        else
            checkers.message.setText("BLACK:  Make your move.");
    }


    public void initGraphicContext() {
        GraphicsContext g = getGraphicsContext2D();
        g.setFont(Font.font(18));
        g.setStroke(Color.BLACK);
        g.setLineWidth(2);
        g.strokeRect(1, 1, 322, 322);
    }


    public void drawBoard() {
        GraphicsContext g = getGraphicsContext2D();
        g.setFont(Font.font(18));
        ambientField(g, Color.BLACK, 2);
        g.strokeRect(1, 1, 322, 322);
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                drawBoardField(g, row, col);
                if (!board.isPieceEmpty(row, col)) {
                    if (board.pieceAt(row, col).getTeam() == WHITE && board.pieceAt(row, col).getPieceType() == PieceType.NORMAL)
                        drawPiece(g, row, col, Color.WHITE);
                    else if (board.pieceAt(row, col).getTeam() == BLACK && board.pieceAt(row, col).getPieceType() == PieceType.NORMAL)
                        drawPiece(g, row, col, Color.BLACK);
                    else if (board.pieceAt(row, col).getTeam() == WHITE && board.pieceAt(row, col).getPieceType() == PieceType.KING)
                        drawKingPiece(g, row, col, Color.WHITE, Color.BLACK);
                    else if (board.pieceAt(row, col).getTeam() == BLACK && board.pieceAt(row, col).getPieceType() == PieceType.KING)
                        drawKingPiece(g, row, col, Color.BLACK, Color.WHITE);
                }
            }
        }

        if (gameInProgress) {
            ambientField(g, Color.CYAN, 4);
            for (CheckersMove legalMove : legalMoves) {
                drawLegalMoves(g, legalMove.getFromField());
            }
            if (selectedRow >= 0) {
                ambientAvailableMoves(g);
                for (CheckersMove legalMove : legalMoves) {
                    if (legalMove.getFromField().getCol() == selectedCol && legalMove.getFromField().getRow() == selectedRow) {
                        drawLegalMoves(g, legalMove.getToField());
                    }
                }
            }
        }

    }

    private void ambientField(GraphicsContext g, Color cyan, int i) {
        g.setStroke(cyan);
        g.setLineWidth(i);
    }

    private void ambientAvailableMoves(GraphicsContext g) {
        ambientField(g, Color.YELLOW, 4);
        g.strokeRect(4 + selectedCol * 40, 4 + selectedRow * 40, 36, 36);
        ambientField(g, Color.LIME, 4);
    }

    private void drawLegalMoves(GraphicsContext g, BoardField fromField) {
        g.strokeRect(4 + fromField.getCol() * 40, 4 + fromField.getRow() * 40, 36, 36);
    }

    private void drawBoardField(GraphicsContext g, int row, int col) {
        if (row % 2 == col % 2)
            g.setFill(Color.DARKGRAY);
        else
            g.setFill(Color.LIGHTGRAY);
        g.fillRect(2 + col * 40, 2 + row * 40, 40, 40);
    }

    private void drawKingPiece(GraphicsContext g, int row, int col, Color white, Color black) {
        drawPiece(g, row, col, white);
        g.setFill(black);
        g.fillText("K", 15 + col * 40, 29 + row * 40);
    }

    private void drawPiece(GraphicsContext g, int row, int col, Color color) {
        g.setFill(color);
        g.fillOval(8 + col * 40, 8 + row * 40, 28, 28);
    }

    public void mousePressed(MouseEvent evt) {
        if (!gameInProgress)
            checkers.message.setText("Click \"New Game\" to start a new game.");
        else {
            int col = (int) ((evt.getX() - 2) / 40);
            int row = (int) ((evt.getY() - 2) / 40);
            if (col >= 0 && col < 8 && row >= 0 && row < 8)
                doClickSquare(row * 10 + col);
            if (isGameVsComputer() && isBlackPlayerTurn()) {
                computerMove();
            }
        }
    }


    public boolean isGameVsComputer() {
        return gameVsComputer;
    }

    public boolean isBlackPlayerTurn() {
        return currentPlayer == BLACK;
    }

    public void computerMove() {
        CheckersMove move = computer.selectMove(board.getLegalMoves(currentPlayer));
        doMakeMove(move);
        move = computer.selectMove(board.getLegalMoves(currentPlayer));
        if (move.isJump()&& currentPlayer == BLACK)
            computerMove();
        currentPlayer = WHITE;
    }
}