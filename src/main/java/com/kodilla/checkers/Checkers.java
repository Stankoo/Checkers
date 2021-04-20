package com.kodilla.checkers;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;


public class Checkers extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    private CheckersBoard board;
    private Button newGameButton;
    private Button resignButton;
    private Label message;
    public boolean gameInProgress;
    public Team currentPlayer;
    public PieceType pieceType;

    public void prepareGUIContent() {
        message = new Label("Click \"New Game\" to begin.");
        message.setTextFill(Color.rgb(9, 10, 9));
        message.setFont(Font.font(null, FontWeight.BOLD, 18));
        newGameButton = new Button("New Game");
        resignButton = new Button("Resign");
        board = new CheckersBoard(message, gameInProgress, currentPlayer, pieceType, newGameButton, resignButton);
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
    }

    public void start(Stage stage) {
        prepareGUIContent();
        Pane root = createRootPane();
        Scene scene = new Scene(root);

        prepareStage(stage, scene);
    }

    private void prepareStage(Stage stage, Scene scene) {
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Checkers");
        stage.show();
    }

    private Pane createRootPane() {
        Pane root = new Pane();
        root.setPrefWidth(500);
        root.setPrefHeight(420);
        root.getChildren().addAll(board, newGameButton, resignButton, message);
        root.setStyle("-fx-background-color: #515451; "
                + "-fx-border-color: #180404; -fx-border-width:3");
        return root;
    }

}


