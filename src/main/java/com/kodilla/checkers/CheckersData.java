package com.kodilla.checkers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.kodilla.checkers.PieceType.KING;
import static com.kodilla.checkers.PieceType.NORMAL;
import static com.kodilla.checkers.Team.WHITE;
import static  com.kodilla.checkers.Team.BLACK;


public class CheckersData {

    private final KingMovesCalculator kingMovesCalculator = new KingMovesCalculator(this);
    private final Map<Integer, BoardField> board = new HashMap<>();
    private final ArrayList<CheckersMove> moves = new ArrayList<>();
    public static final int board_size = 8;


    CheckersData() {
        setUpGame();
    }

    void setUpGame() {
        for (int row = 0; row < board_size; row++) {
            for (int col = 0; col < board_size; col++) {
                if (row % 2 == col % 2) {
                    if (row < 3) {
                        BoardField field = new BoardField(row, col, new Piece(BLACK, NORMAL));
                        board.put(field.getId(), field);
                    } else if (row > 4) {
                        BoardField field = new BoardField(row, col, new Piece(WHITE, NORMAL));
                        board.put(field.getId(), field);
                    } else {
                        BoardField field = new BoardField(row, col);
                        board.put(field.getId(), field);
                    }
                } else {
                    BoardField field = new BoardField(row, col);
                    board.put(field.getId(), field);
                }
            }
        }
    }

    public Piece pieceAt(int row, int col) {
        return board.get(row * 10 + col).getPiece();
    }

    public boolean isPieceEmpty(int row, int col) {
        return board.get(createId(row, col)).isEmpty();
    }

    public void makeMove(CheckersMove move) {
        board.get(move.getToField().getId()).setPiece(move.getFromField().getPiece());
        board.get(move.getFromField().getId()).setEmpty();
        if (move.isJump())
            board.get(move.getIdToDelete()).setEmpty();

        if (isLastRowForWhites(move))
            board.get(move.getToField().getId()).getPiece().setPieceType(KING);

        if (isLastRowForBlacks(move))
            board.get(move.getToField().getId()).getPiece().setPieceType(KING);
    }

    private boolean isLastRowForBlacks(CheckersMove move) {
        return move.getToField().getRow() == 7 && getPieceTeam(move.getToField().getId()) == BLACK;
    }

    private boolean isLastRowForWhites(CheckersMove move) {
        return move.getToField().getRow() == 0 && getPieceTeam(move.getToField().getId()) == WHITE;
    }

    public ArrayList<CheckersMove> getLegalMoves(Team player) {

        moves.clear();
        board.forEach((k, v) -> {
            if (board.get(k).getPiece() != null && getPieceTeam(k) == player)
                jumpMoves(player, k);
        });

        if (moves.size() == 0) {
            board.forEach((key, v) -> {
                if (board.get(key).isNotEmpty() && getPieceTeam(key) == player) {

                    if (getPieceType(key) == NORMAL)
                        piecesMoves(player, key);

                    if (getPieceType(key) == KING)
                        moves.addAll(kingMovesCalculator.kingMoves(key, this.board));
                }

            });
        }
        if (moves.size() == 0)
            return null;
        else {
            return moves;
        }

    }

    private void piecesMoves(Team team, int id) {
        if (canMove(team, id, getDownRightId(id)))
            moves.add(new CheckersMove(board.get(id), board.get(getDownRightId(id))));
        if (canMove(team, id, getUpRightId(id)))
            moves.add(new CheckersMove(board.get(id), board.get(getUpRightId(id))));
        if (canMove(team, id, getDownLeftId(id)))
            moves.add(new CheckersMove(board.get(id), board.get(getDownLeftId(id))));
        if (canMove(team, id, getUpLeftId(id)))
            moves.add(new CheckersMove(board.get(id), board.get(getUpLeftId(id))));
    }

    public ArrayList<CheckersMove> getLegalJumpsFor(Team team, int id) {

        moves.clear();
        jumpMoves(team, id);

        if (moves.size() == 0)
            return null;
        else {
            return moves;
        }
    }


    private void jumpMoves(Team player, int id) {
        final int row = id / 10;
        final int col = id % 10;
        if (getPieceTeam(id) == player && getPieceType(id) == KING) {
            moves.addAll(kingMovesCalculator.kingJumps(id, board));
        }
        if (getPieceTeam(id) == player) {
            if (canJump(id, getDownRightId(id), createId(row + 2, col + 2)))
                moves.add(new CheckersMove(board.get(id), board.get(createId(row + 2, col + 2)), getDownRightId(id)));
            if (canJump(id, getUpRightId(id), createId(row - 2, col + 2)))
                moves.add(new CheckersMove(board.get(id), board.get(createId(row - 2, col + 2)), getUpRightId(id)));
            if (canJump(id, getDownLeftId(id), createId(row + 2, col - 2)))
                moves.add(new CheckersMove(board.get(id), board.get(createId(row + 2, col - 2)), getDownLeftId(id)));
            if (canJump(id, getUpLeftId(id), createId(row - 2, col - 2)))
                moves.add(new CheckersMove(board.get(id), board.get(createId(row - 2, col - 2)), getUpLeftId(id)));
        }
    }

    private boolean canJump(int actualId, int moveId, int kickId) {
        if (isMoveIllegal(kickId))
            return false;
        if (isMoveIllegal(moveId))
            return false;
        if (board.get(kickId).isNotEmpty())
            return false;
        if (board.get(moveId).isEmpty())
            return false;
        if (getPieceTeam(actualId) == WHITE) {
            if (kickId / 10 > actualId / 10)
                return false;
            return getPieceTeam(moveId) == BLACK;
        } else {
            if (kickId / 10 < actualId / 10)
                return false;
            return getPieceTeam(moveId) == WHITE;
        }

    }

    public boolean canMove(Team player, int currentId, int moveToId) {

        final int moveRow = moveToId / 10;
        final int currentRow = currentId / 10;
        if (isMoveIllegal(moveToId))
            return false;

        if (board.get(moveToId).isNotEmpty())
            return false;

        if (player == WHITE) {
            return moveRow < currentRow;

        } else {
            return moveRow > currentRow;

        }
    }
    public boolean isMoveIllegal(int moveToId) {
        return moveToId / 10 < 0 || moveToId / 10 > 7 || moveToId % 10 < 0 || moveToId % 10 > 7;
    }

    public Team getPieceTeam(int id) {
        return board.get(id).getPiece().getTeam();
    }

    public PieceType getPieceType(int id) {
        return board.get(id).getPiece().getPieceType();
    }

    public int createId(int row, int col) {
        return row * 10 + col;
    }

    public int getUpLeftId(int id) {
        return (id / 10 - 1) * 10 + (id % 10 - 1);
    }

    public int getUpRightId(int id) {
        return (id / 10 - 1) * 10 + (id % 10 + 1);
    }

    public int getDownLeftId(int id) {
        return (id / 10 + 1) * 10 + (id % 10 - 1);
    }

    public int getDownRightId(int id) {
        return (id / 10 + 1) * 10 + (id % 10 + 1);
    }
}

