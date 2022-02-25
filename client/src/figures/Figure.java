package figures;

import helpers.DecoEncoderHelper;

import java.util.ArrayList;

public abstract class Figure {

    private boolean blackColor;
    private int[] stand;
    private int value;
    private int countOfMoves = 0;
    private String fieldStart;

    public Figure(boolean blackColor, int[] stand, int value, String fieldStart) {
        this.blackColor = blackColor;
        this.stand = stand;
        this.value = value;
        this.countOfMoves = 0;
        this.fieldStart = fieldStart;
    }

    public abstract ArrayList<String> findPossibleMoves(int [][] chessBoard);

    public abstract String getNameFigure();


    //gettery i settery

    public String getFieldStart() {
        return fieldStart;
    }

    public int getCountOfMoves() {
        return countOfMoves;
    }

    public void doneMove() {
        this.countOfMoves += 1;
    }

    public int getValue() {
        return value;
    }

    public int[] getStand() {
        return stand;
    }

    public void steStand(int[] stand) {
        this.stand = stand;
    }

    public String getStandInString() {
        return DecoEncoderHelper.codePosition(stand[0],stand[1]);
    }

    public boolean isBlackColor() {
        return blackColor;
    }

}
