package figures;

import helpers.DecoEncoderHelper;

import java.util.ArrayList;

public class King extends Figure {

    private static final String nameOfFigure = "king";

    public King(boolean ifBlack, int[] stand, String sta) {
        super(ifBlack, stand, 0, sta);
    }

    @Override
    public ArrayList<String> findPossibleMoves(int[][] chessBoard) {
        ArrayList<String> possibleMoves = new ArrayList<>();
        int [] sta = getStand();
        int i = sta[0];
        int j = sta[1];
        if (i-1 > 0 && j - 1 > 0)
            if (chessBoard[i-1][j-1] != 1) possibleMoves.add(DecoEncoderHelper.codePosition(i-1,j-1));
        if (i-1 > 0)
            if (chessBoard[i-1][j] != 1) possibleMoves.add(DecoEncoderHelper.codePosition(i-1,j));
        if (i-1 > 0 && j + 1 < 9)
            if (chessBoard[i-1][j+1] != 1) possibleMoves.add(DecoEncoderHelper.codePosition(i-1,j+1));
        if (j + 1 < 9)
            if (chessBoard[i][j+1] != 1) possibleMoves.add(DecoEncoderHelper.codePosition(i,j+1));
        if (i+1 < 9 && j + 1 < 9)
            if (chessBoard[i+1][j+1] != 1) possibleMoves.add(DecoEncoderHelper.codePosition(i+1,j+1));
        if (i+1 < 9)
            if (chessBoard[i+1][j] != 1) possibleMoves.add(DecoEncoderHelper.codePosition(i+1,j));
        if (i+1 < 9 && j - 1 > 0)
            if (chessBoard[i+1][j-1] != 1) possibleMoves.add(DecoEncoderHelper.codePosition(i+1,j-1));
        if (j - 1 > 0)
            if (chessBoard[i][j-1] != 1) possibleMoves.add(DecoEncoderHelper.codePosition(i,j-1));
        return possibleMoves;
    }


    @Override
    public String getNameFigure() {
        return nameOfFigure;
    }
}
