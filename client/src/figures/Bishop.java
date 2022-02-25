package figures;

import helpers.DecoEncoderHelper;

import java.util.ArrayList;

public class Bishop extends Figure {

    private static final String nameOfFigure = "bishop";

    public Bishop(boolean ifBlack, int[] stand, String sta) {
        super(ifBlack, stand, 3, sta);
    }

    @Override
    public ArrayList<String> findPossibleMoves(int[][] tab) {
        ArrayList<String> possibleMoves = new ArrayList<>();
        int [] sta = getStand();

        int i = sta[0]-1;
        int j = sta[1]+1;
        while(i > 0 && i < 9 && j > 0 && j < 9) {
            if(tab[i][j] == 0) possibleMoves.add(DecoEncoderHelper.codePosition(i,j));
            else if(tab[i][j] == -1) {
                possibleMoves.add(DecoEncoderHelper.codePosition(i,j));
                break;
            } else break;
            i--;
            j++;
        }

        i = sta[0] + 1;
        j = sta[1] + 1;
        while(i > 0 && i < 9 && j > 0 && j < 9) {
            if(tab[i][j] == 0) possibleMoves.add(DecoEncoderHelper.codePosition(i,j));
            else if(tab[i][j] == -1) {
                possibleMoves.add(DecoEncoderHelper.codePosition(i,j));
                break;
            } else break;
            i++;
            j++;
        }

        i = sta[0] + 1;
        j = sta[1] - 1;
        while(i > 0 && i < 9 && j > 0 && j < 9) {
            if(tab[i][j] == 0) possibleMoves.add(DecoEncoderHelper.codePosition(i,j));
            else if(tab[i][j] == -1) {
                possibleMoves.add(DecoEncoderHelper.codePosition(i,j));
                break;
            } else break;
            i++;
            j--;
        }

        i = sta[0] - 1;
        j = sta[1] - 1;
        while(i > 0 && i < 9 && j > 0 && j < 9) {
            if(tab[i][j] == 0) possibleMoves.add(DecoEncoderHelper.codePosition(i,j));
            else if(tab[i][j] == -1) {
                possibleMoves.add(DecoEncoderHelper.codePosition(i,j));
                break;
            } else break;
            i--;
            j--;
        }
        return possibleMoves;
    }

    @Override
    public String getNameFigure() {
        return nameOfFigure;
    }
}
