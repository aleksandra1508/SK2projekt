package figures;

import helpers.DecoEncoderHelper;

import java.util.ArrayList;

public class Pawn extends Figure {

    private static final String nameOfFigure = "pawn";

    public Pawn(boolean ifBlack, int[] stand, String sta) {
        super(ifBlack, stand, 1, sta);
    }

    @Override
    public ArrayList<String> findPossibleMoves(int[][] arr) {
        ArrayList<String> possibleMoves = new ArrayList<>();
        int [] sta = getStand();
        if (isBlackColor()) {
            if (sta[1] != 1) {
                if (arr[sta[0]][sta[1] - 1] == 0) {
                    if (getCountOfMoves() == 0 && arr[sta[0]][sta[1] - 2] == 0)
                        possibleMoves.add(DecoEncoderHelper.codePosition(sta[0], sta[1] - 2));
                    possibleMoves.add(DecoEncoderHelper.codePosition(sta[0], sta[1] - 1));
                }
                if (sta[0] - 1 > 0 && sta[1] - 1 > 0)
                    if (arr[sta[0] - 1][sta[1] - 1] == -1)
                        possibleMoves.add(DecoEncoderHelper.codePosition(sta[0] - 1, sta[1] - 1));
                if (sta[0] + 1 < 9 && sta[1] - 1 > 0)
                    if (arr[sta[0] + 1][sta[1] - 1] == -1)
                        possibleMoves.add(DecoEncoderHelper.codePosition(sta[0] + 1, sta[1] - 1));
            }
        } else {
            if (sta[1] != 8) {
                if (arr[sta[0]][sta[1] + 1] == 0) {
                    if (getCountOfMoves() == 0 && arr[sta[0]][sta[1] + 2] == 0)
                        possibleMoves.add(DecoEncoderHelper.codePosition(sta[0], sta[1] + 2));
                    possibleMoves.add(DecoEncoderHelper.codePosition(sta[0], sta[1] + 1));
                }
                if (sta[0] - 1 > 0 && sta[1] + 1 < 9)
                    if (arr[sta[0] - 1][sta[1] + 1] == -1)
                        possibleMoves.add(DecoEncoderHelper.codePosition(sta[0] - 1, sta[1] + 1));
                if (sta[0] + 1 < 9 && sta[1] + 1 < 9)
                    if (arr[sta[0] + 1][sta[1] + 1] == -1)
                        possibleMoves.add(DecoEncoderHelper.codePosition(sta[0] + 1, sta[1] + 1));
            }
        }
        return possibleMoves;
    }

    @Override
    public String getNameFigure() {
        return nameOfFigure;
    }
}
