package com.kodilla.checkers;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.input.MouseEvent;

public class Checkers extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    CheckersBoard board;
   public Button newGameButton;
    public Button resignButton;
  Label message;


    public void start(Stage stage) {
        message = new Label("Click \"New Game\" to begin.");
        message.setTextFill(Color.rgb(9, 10, 9));
        message.setFont(Font.font(null, FontWeight.BOLD, 18));
        newGameButton = new Button("New Game");
        resignButton = new Button("Resign");
        board = new CheckersBoard();
        board.drawBoard();
        newGameButton.setOnAction(e -> board.doNewGame());
        resignButton.setOnAction(e -> board.doResign());
        board.setOnMousePressed(e -> board.mousePressed(e));
        board.relocate(20, 20);
        newGameButton.relocate(370, 120);
        resignButton.relocate(370, 200);
        message.relocate(20, 370);
        resignButton.setManaged(false);
        resignButton.resize(100, 30);
        newGameButton.setManaged(false);
        newGameButton.resize(100, 30);
        Pane root = new Pane();
        root.setPrefWidth(500);
        root.setPrefHeight(420);
        root.getChildren().addAll(board, newGameButton, resignButton, message);
        root.setStyle("-fx-background-color: #515451; "
                + "-fx-border-color: #180404; -fx-border-width:3");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Checkers");
        stage.show();
    } // end start()

    public class CheckersBoard extends Canvas {
        CheckersData board;
        boolean gameInProgress;
        int currentPlayer;
        int selectedRow, selectedCol;
        CheckersMove[] legalMoves;


        CheckersBoard() {
            super(324, 324);
            board = new CheckersData();
            doNewGame();
        }

        void doNewGame() {
            if (gameInProgress) {
                message.setText("Finish the current game first!");
                return;
            }
            board.setUpGame();
            currentPlayer = CheckersData.WHITE;
            legalMoves = board.getLegalMoves(CheckersData.WHITE);
            selectedRow = -1;
            message.setText("White:  Make your move.");
            gameInProgress = true;
            newGameButton.setDisable(true);
            resignButton.setDisable(false);
            drawBoard();
        }

        void doResign() {
            if (!gameInProgress) {
                message.setText("There is no game in progress!");
                return;
            }
            if (currentPlayer == CheckersData.WHITE)
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
                    if (currentPlayer == CheckersData.WHITE)
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
                    if (currentPlayer == CheckersData.WHITE)
                        message.setText("WHITE:  You must continue jumping.");
                    else
                        message.setText("BLACK:  You must continue jumping.");
                    selectedRow = move.toRow;
                    selectedCol = move.toCol;
                    drawBoard();
                    return;
                }
            }
            if (currentPlayer == CheckersData.WHITE) {
                currentPlayer = CheckersData.BLACK;
                legalMoves = board.getLegalMoves(currentPlayer);
                if (legalMoves == null)
                    gameOver("BLACK has no moves. WHITE wins.");
                else if (legalMoves[0].isJump())
                    message.setText("BLACK:  Make your move.  You must jump.");
                else
                    message.setText("BLACK:  Make your move.");
            } else {
                currentPlayer = CheckersData.WHITE;
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
                    if (legalMoves[i].fromRow != legalMoves[0].fromRow
                            || legalMoves[i].fromCol != legalMoves[0].fromCol) {
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

        public void drawBoard() {
            GraphicsContext g = getGraphicsContext2D();
            g.setFont(Font.font(18));
            g.setStroke(Color.BLACK);
            g.setLineWidth(2);
            g.strokeRect(1, 1, 322, 322);
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    if (row % 2 == col % 2)
                        g.setFill(Color.LIGHTGRAY);
                    else
                        g.setFill(Color.DARKGRAY);
                    g.fillRect(2 + col * 40, 2 + row * 40, 40, 40);
                    switch (board.pieceAt(row, col)) {
                        case CheckersData.WHITE:
                            g.setFill(Color.WHITE);
                            g.fillOval(8 + col * 40, 8 + row * 40, 28, 28);
                            break;
                        case CheckersData.BLACK:
                            g.setFill(Color.BLACK);
                            g.fillOval(8 + col * 40, 8 + row * 40, 28, 28);
                            break;
                        case CheckersData.WHITE_KING:
                            g.setFill(Color.WHITE);
                            g.fillOval(8 + col * 40, 8 + row * 40, 28, 28);
                            g.setFill(Color.WHITE);
                            g.fillText("K", 15 + col * 40, 29 + row * 40);
                            break;
                        case CheckersData.BLACK_KING:
                            g.setFill(Color.BLACK);
                            g.fillOval(8 + col * 40, 8 + row * 40, 28, 28);
                            g.setFill(Color.WHITE);
                            g.fillText("K", 15 + col * 40, 29 + row * 40);
                            break;
                    }
                }
            }

            if (gameInProgress) {
                g.setStroke(Color.CYAN);
                g.setLineWidth(4);
                for (CheckersMove legalMove : legalMoves) {
                    g.strokeRect(4 + legalMove.fromCol * 40, 4 + legalMove.fromRow * 40, 36, 36);
                }
                if (selectedRow >= 0) {
                    g.setStroke(Color.YELLOW);
                    g.setLineWidth(4);
                    g.strokeRect(4 + selectedCol * 40, 4 + selectedRow * 40, 36, 36);
                    g.setStroke(Color.LIME);
                    g.setLineWidth(4);
                    for (CheckersMove legalMove : legalMoves) {
                        if (legalMove.fromCol == selectedCol && legalMove.fromRow == selectedRow) {
                            g.strokeRect(4 + legalMove.toCol * 40, 4 + legalMove.toRow * 40, 36, 36);
                        }
                    }
                }
            }

        }

        public void mousePressed(MouseEvent evt) {
            if (!gameInProgress)
                message.setText("Click \"New Game\" to start a new game.");
            else {
                int col = (int) ((evt.getX() - 2) / 40);
                int row = (int) ((evt.getY() - 2) / 40);
                if (col >= 0 && col < 8 && row >= 0 && row < 8)
                    doClickSquare(row, col);
            }
        }

    }
}
