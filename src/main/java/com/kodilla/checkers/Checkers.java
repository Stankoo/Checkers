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
    public Button newPlayersGameButton;
    public Button resignButton;
    public Button newGameVsComputerButton;
    public Label message;



    public void prepareGUIContent() {
        message = new Label("Click \"New Game\" to begin.");
        message.setTextFill(Color.rgb(9, 10, 9));
        message.setFont(Font.font(null, FontWeight.BOLD, 18));
        newPlayersGameButton = new Button("New Game vs Player");
        newGameVsComputerButton = new Button("New Game vs Computer");
        resignButton = new Button("Resign");
        board = new CheckersBoard(this);
        board.drawBoard();
        newPlayersGameButton.setOnAction(e -> board.doNewGameVsPlayer());
        newGameVsComputerButton.setOnAction(e -> board.doNewGameVsComputer());
        resignButton.setOnAction(e -> board.doResign());
        board.setOnMousePressed(e -> board.mousePressed(e));
        board.relocate(20, 20);
        newGameVsComputerButton.relocate(350, 100);
        newPlayersGameButton.relocate(360, 140);
        resignButton.relocate(370, 200);
        message.relocate(20, 370);
        resignButton.setManaged(false);
        resignButton.resize(100, 30);
        newPlayersGameButton.setManaged(true);
        newGameVsComputerButton.setManaged(true);
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
        root.getChildren().addAll(board, newPlayersGameButton, resignButton,newGameVsComputerButton, message);
        root.setStyle("-fx-background-color: #515451; "
                + "-fx-border-color: #180404; -fx-border-width:3");
        return root;
    }

}


