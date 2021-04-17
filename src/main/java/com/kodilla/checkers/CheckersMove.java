package com.kodilla.checkers;

public class CheckersMove {
    int fromRow, fromCol;
    int toRow, toCol;

    CheckersMove(int fromRow, int fromCol, int toRow, int toCol) {
        this.fromRow = fromRow;
        this.fromCol = fromCol;
        this.toRow = toRow;
        this.toCol = toCol;

    }

    boolean isJump() {
        return (fromRow - toRow == 2 || fromRow - toRow == -2);
    }
}