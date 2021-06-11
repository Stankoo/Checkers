package com.kodilla.checkers;


public  class CheckersMove {
    private final BoardField fromField;
    private final BoardField toField;
    private  boolean isJump =false;
    private int idToDelete;
    public CheckersMove(BoardField fromField, BoardField toField) {
        this.fromField = fromField;
        this.toField = toField;

    }
    public CheckersMove(BoardField fromField, BoardField toField, int idToDelete) {
        this.fromField = fromField;
        this.toField = toField;
        this.idToDelete = idToDelete;
        this.isJump = true;
    }

    public BoardField getFromField() {
        return fromField;
    }

    public BoardField getToField() {
        return toField;
    }

    public boolean isJump() {
        return isJump;
    }

    public void setJump(boolean jump) {
        isJump = jump;
    }

    public int getIdToDelete() {
        return idToDelete;
    }

    public void setIdToDelete(int idToDelete) {
        this.idToDelete = idToDelete;
    }
}