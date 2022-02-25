package figures;

import helpers.DecoEncoderHelper;

import java.util.ArrayList;

public class Knight extends Figure {

    private static final String nameOfFigure = "knight";

    public Knight(boolean ifBlack, int[] stand, String sta) {
        super(ifBlack, stand, 3, sta);
    }

    @Override
    public ArrayList<String> findPossibleMoves(int[][] arr) {
        ArrayList<String> possibleMoves = new ArrayList<>();
        int [] sta = getStand();
        for (int i = 1;i < 9;i++)
            for (int j = 1;j < 9;j++) {
                if (arr[i][j] != 1) {
                    if (i-2 == sta[0] && j+1 == sta[1]) possibleMoves.add(DecoEncoderHelper.codePosition(i,j));
                    if (i-1 == sta[0] && j+2 == sta[1]) possibleMoves.add(DecoEncoderHelper.codePosition(i,j));
                    if (i+1 == sta[0] && j+2 == sta[1]) possibleMoves.add(DecoEncoderHelper.codePosition(i,j));
                    if (i+2 == sta[0] && j+1 == sta[1]) possibleMoves.add(DecoEncoderHelper.codePosition(i,j));
                    if (i+2 == sta[0] && j-1 == sta[1]) possibleMoves.add(DecoEncoderHelper.codePosition(i,j));
                    if (i+1 == sta[0] && j-2 == sta[1]) possibleMoves.add(DecoEncoderHelper.codePosition(i,j));
                    if (i-1 == sta[0] && j-2 == sta[1]) possibleMoves.add(DecoEncoderHelper.codePosition(i,j));
                    if (i-2 == sta[0] && j-1 == sta[1]) possibleMoves.add(DecoEncoderHelper.codePosition(i,j));
                }
            }
        return possibleMoves;
    }

    @Override
    public String getNameFigure() {
        return nameOfFigure;
    }
}
