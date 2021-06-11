package com.kodilla.checkers;


public class BoardField {
    private final int row;
    private final int col;
    private Piece piece = null;
    private final Integer id;


    public BoardField(int row, int col) {
        this.row = row;
        this.col = col;
        this.id = row * 10 + col;
    }
    public BoardField(int row, int col, Piece piece) {
        this.row = row;
        this.col = col;
        this.piece =piece;
        this.id = row * 10 + col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public Integer getId() {
        return id;
    }

    public void setEmpty(){
        this.piece = null;
    }
    public boolean isEmpty(){
        return getPiece() == null;
    }
    public void setKing(){
        this.piece.setPieceType(PieceType.KING);
    }
    public boolean isNotEmpty(){
        return getPiece() != null;
    }}


