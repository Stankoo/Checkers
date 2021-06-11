package com.kodilla.checkers;

import java.util.ArrayList;
import java.util.Map;


public class kingMovesCalculator {
    private final CheckersData checkersData;
    private final ArrayList arrayList = new ArrayList<CheckersMove>();

    public kingMovesCalculator(CheckersData checkersData) {
        this.checkersData = checkersData;
    }

    public ArrayList<CheckersMove> kingJumps(int id, Map<Integer, BoardField> board) {
        arrayList.clear();
        final int row = id / 10;
        final int col = id % 10;
        int jumpUpLeft = 0;
        int jumpDownLeft = 1000;
        int jumpUpRight = 0;
        int jumpDownRight = 1000;
        for (int i = 1; i < 8; i++) {
            int upLeft = checkersData.createId(row - i, col - i);
            int blockLeft = checkersData.createId(row - i-1, col - i-1);
            if (!checkersData.isMoveIllegal(upLeft)&&!checkersData.isMoveIllegal(blockLeft)) {
                if (board.get(upLeft).isNotEmpty()&&board.get(blockLeft).isNotEmpty())
                    break;
                if (board.get(upLeft).isNotEmpty() && checkersData.getPieceTeam(upLeft) != checkersData.getPieceTeam(id) && jumpUpLeft == 0)
                    jumpUpLeft = upLeft;
                if (upLeft < jumpUpLeft && board.get(upLeft).isEmpty()) {
                    arrayList.add(new CheckersMove(board.get(id), board.get(upLeft), jumpUpLeft));
                }
                if (upLeft < jumpUpLeft && board.get(upLeft).isNotEmpty()) {
                    break;
                }

            } else break;
        }
        for (int i = 1; i < 8; i++) {
            int upRight = checkersData.createId(row - i, col + i);
            int blockRight = checkersData.createId(row - i -1, col + i +1);
            if (!checkersData.isMoveIllegal(upRight)&&!checkersData.isMoveIllegal(blockRight)) {
                if (board.get(upRight).isNotEmpty()&&board.get(blockRight).isNotEmpty())
                    break;
                if (board.get(upRight).isNotEmpty()&& checkersData.getPieceTeam(upRight) != checkersData.getPieceTeam(id) && jumpUpRight == 0)
                    jumpUpRight = upRight;
                if (upRight < jumpUpRight && board.get(upRight).isEmpty())
                    arrayList.add(new CheckersMove(board.get(id), board.get(upRight), jumpUpRight));
                if (upRight < jumpUpRight && board.get(upRight).isNotEmpty())
                    break;
            } else break;
        }
        for (int i = 1; i < 8; i++) {
            int downLeft = checkersData.createId(row + i, col - i);
            int blockLeft = checkersData.createId(row + i +1, col - i-1);
            if (!checkersData.isMoveIllegal(downLeft)&&!checkersData.isMoveIllegal(blockLeft)) {
                if (board.get(downLeft).isNotEmpty()&&board.get(blockLeft).isNotEmpty())
                    break;
                if (board.get(downLeft).isNotEmpty() && checkersData.getPieceTeam(downLeft) != checkersData.getPieceTeam(id) && jumpDownLeft == 1000)
                    jumpDownLeft = downLeft;
                if (downLeft > jumpDownLeft && board.get(downLeft).isEmpty())
                    arrayList.add(new CheckersMove(board.get(id), board.get(downLeft), jumpDownLeft));
                if (downLeft > jumpDownLeft && board.get(downLeft).isEmpty())
                    break;
            } else break;
        }
        for (int i = 1; i < 8; i++) {
            int downRight = checkersData.createId(row + i, col + i);
            int blockRight = checkersData.createId(row + i+1, col + i+1);
            if (!checkersData.isMoveIllegal(downRight)&&!checkersData.isMoveIllegal(blockRight)) {
                if (board.get(downRight).isNotEmpty()&&board.get(blockRight).isNotEmpty())
                    break;
                if (board.get(downRight).isNotEmpty() && checkersData.getPieceTeam(downRight) != checkersData.getPieceTeam(id) && jumpDownRight == 1000)
                    jumpDownRight = downRight;
                if (downRight > jumpDownRight && board.get(downRight).isEmpty())
                    arrayList.add(new CheckersMove(board.get(id), board.get(downRight), jumpDownRight));
                if (downRight > jumpDownRight && board.get(downRight).isEmpty())
                    break;

            } else break;
        }

        return arrayList;
    }

    public ArrayList<CheckersMove> kingMoves(int id, Map<Integer, BoardField> board) {
        arrayList.clear();
        final int currentRow = id / 10;
        final int currentCol = id % 10;
        for (int i = 1; i < 8; i++) {
            int upLeft = checkersData.createId(currentRow - i, currentCol - i);
            if (!checkersData.isMoveIllegal(upLeft)) {
                if (board.get(upLeft).isEmpty())
                    arrayList.add(new CheckersMove(board.get(checkersData.createId(currentRow, currentCol)), board.get(upLeft)));
                else break;
            } else break;
        }
        for (int i = 1; i < 8; i++) {
            int downLeft = checkersData.createId(currentRow - i, currentCol + i);
            if (!checkersData.isMoveIllegal(downLeft)) {
                if (board.get(downLeft).isEmpty())
                    arrayList.add(new CheckersMove(board.get(checkersData.createId(currentRow, currentCol)), board.get(downLeft)));
                else break;
            } else break;
        }
        for (int i = 1; i < 8; i++) {
            int upRight = checkersData.createId(currentRow + i, currentCol - i);
            if (!checkersData.isMoveIllegal(upRight)) {
                if (board.get(upRight).isEmpty())
                    arrayList.add(new CheckersMove(board.get(checkersData.createId(currentRow, currentCol)), board.get(upRight)));
                else break;
            } else break;
        }
        for (int i = 1; i < 8; i++) {
            int downRight = checkersData.createId(currentRow + i, currentCol + i);
            if (!checkersData.isMoveIllegal(downRight)) {
                if (board.get(downRight).isEmpty())
                    arrayList.add(new CheckersMove(board.get(checkersData.createId(currentRow, currentCol)), board.get(downRight)));
                else break;
            } else break;
        }

        return arrayList;
    }
}
