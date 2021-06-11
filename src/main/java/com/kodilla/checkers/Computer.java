package com.kodilla.checkers;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Random;

@RequiredArgsConstructor
public class Computer {
    private Random random = new Random();

    public CheckersMove selectMove(ArrayList<CheckersMove> possibleMoves) {
        Object[] object = possibleMoves.toArray();
        return (CheckersMove) object[random.nextInt(object.length)];
    }
}

