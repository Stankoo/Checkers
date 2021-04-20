package com.kodilla.checkers;

import java.util.ArrayList;

import static com.kodilla.checkers.PieceType.BLACK_KING;
import static com.kodilla.checkers.PieceType.WHITE_PIECE;


public class CheckersData {

    PieceType[][] board;
    public static final int board_size = 8;


    CheckersData() {

        board = new PieceType[board_size][board_size];
        setUpGame();
    }

    void setUpGame() {
        for (int row = 0; row < board_size; row++) {
            for (int col = 0; col < board_size; col++) {
                if (row % 2 == col % 2) {
                    if (row < 3)
                        board[row][col] = PieceType.BLACK_PIECE;
                    else if (row > 4)
                        board[row][col] = PieceType.WHITE_PIECE;
                    else
                        board[row][col] = PieceType.EMPTY;
                } else {
                    board[row][col] = PieceType.EMPTY;
                }
            }
        }
    }

    PieceType pieceAt(int row, int col) {
        return board[row][col];
    }

    void makeMove(CheckersMove move) {
        makeMove(move.fromRow, move.fromCol, move.toRow, move.toCol);
    }

    void makeMove(int fromRow, int fromCol, int toRow, int toCol) {
        board[toRow][toCol] = board[fromRow][fromCol];
        board[fromRow][fromCol] = PieceType.EMPTY;
        if (fromRow - toRow == 2 || fromRow - toRow == -2) {
            int jumpRow = (fromRow + toRow) / 2;
            int jumpCol = (fromCol + toCol) / 2;
            board[jumpRow][jumpCol] = PieceType.EMPTY;
        }
        if (toRow == 0 && board[toRow][toCol] == PieceType.WHITE_PIECE)
            board[toRow][toCol] = PieceType.WHITE_KING;
        if (toRow == 7 && board[toRow][toCol] == PieceType.BLACK_PIECE)
            board[toRow][toCol] = BLACK_KING;

    }

    CheckersMove[] getLegalMoves(PieceType pieceType, Team player) {

        PieceType playerKing;
        if (player == Team.WHITE && pieceType == WHITE_PIECE)
            playerKing = PieceType.WHITE_KING;
        else
            playerKing = BLACK_KING;
        ArrayList<CheckersMove> moves = new ArrayList<>();
        for (int row = 0; row < board_size; row++) {
            for (int col = 0; col < board_size; col++) {
                jumpMoves(pieceType, row, col, playerKing, moves);
            }
        }
        if (moves.size() == 0) {
            for (int row = 0; row < board_size; row++) {
                for (int col = 0; col < board_size; col++) {
                    if (board[row][col] == pieceType || board[row][col] == playerKing) {
                        if (canMove(pieceType, row, col, row + 1, col + 1))
                            moves.add(new CheckersMove(row, col, row + 1, col + 1));
                        if (canMove(pieceType, row, col, row - 1, col + 1))
                            moves.add(new CheckersMove(row, col, row - 1, col + 1));
                        if (canMove(pieceType, row, col, row + 1, col - 1))
                            moves.add(new CheckersMove(row, col, row + 1, col - 1));
                        if (canMove(pieceType, row, col, row - 1, col - 1))
                            moves.add(new CheckersMove(row, col, row - 1, col - 1));
                    }
                }
            }
        }
        if (moves.size() == 0)
            return null;
        else {
            return moves.toArray(new CheckersMove[0]);
        }

    }

    CheckersMove[] getLegalJumpsFrom(PieceType pieceType, int row, int col) {
        if (pieceType != PieceType.WHITE_PIECE && pieceType != PieceType.BLACK_PIECE)
            return null;
        PieceType playerKing;
        if (pieceType == PieceType.WHITE_PIECE)
            playerKing = PieceType.WHITE_KING;
        else
            playerKing = PieceType.BLACK_KING;
        ArrayList<CheckersMove> moves = new ArrayList<>();
        jumpMoves(pieceType, row, col, playerKing, moves);
        if (moves.size() == 0)
            return null;
        else {
            CheckersMove[] moveArray = new CheckersMove[moves.size()];
            for (int i = 0; i < moves.size(); i++)
                moveArray[i] = moves.get(i);
            return moveArray;
        }
    }


    private void jumpMoves(PieceType pieceType, int row, int col, PieceType playerKing, ArrayList<CheckersMove> moves) {
        if (board[row][col] == pieceType || board[row][col] == playerKing) {
            if (canJump(pieceType, row, col, row + 1, col + 1, row + 2, col + 2))
                moves.add(new CheckersMove(row, col, row + 2, col + 2));
            if (canJump(pieceType, row, col, row - 1, col + 1, row - 2, col + 2))
                moves.add(new CheckersMove(row, col, row - 2, col + 2));
            if (canJump(pieceType, row, col, row + 1, col - 1, row + 2, col - 2))
                moves.add(new CheckersMove(row, col, row + 2, col - 2));
            if (canJump(pieceType, row, col, row - 1, col - 1, row - 2, col - 2))
                moves.add(new CheckersMove(row, col, row - 2, col - 2));
        }
    }

    private boolean canJump(PieceType type, int r1, int c1, int r2, int c2, int r3, int c3) {
        if (r3 < 0 || r3 >= board_size || c3 < 0 || c3 >= board_size)
            return false;
        if (board[r3][c3] != PieceType.EMPTY)
            return false;
        if (type == PieceType.WHITE_PIECE) {
            if (board[r1][c1] == PieceType.WHITE_PIECE && r3 > r1)
                return false;
            return board[r2][c2] == PieceType.BLACK_PIECE || board[r2][c2] == BLACK_KING;
        } else {
            if (board[r1][c1] == PieceType.BLACK_PIECE && r3 < r1)
                return false;
            return board[r2][c2] == PieceType.WHITE_PIECE || board[r2][c2] == PieceType.WHITE_KING;
        }

    }

    public boolean canMove(PieceType type, int r1, int c1, int r2, int c2) {

        if (r2 < 0 || r2 >= board_size || c2 < 0 || c2 >= board_size)
            return false;

        if (board[r2][c2] != PieceType.EMPTY)
            return false;

        if (type == PieceType.WHITE_PIECE) {
            return board[r1][c1] != PieceType.WHITE_PIECE || r2 <= r1;

        } else {
            return board[r1][c1] != PieceType.BLACK_PIECE || r2 >= r1;

        }
    }
}

