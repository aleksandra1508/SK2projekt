package figures;

import helpers.DecoEncoderHelper;

import java.util.ArrayList;

public class Queen extends Figure {

    private static final String nameOfFigure = "queen";

    public Queen(boolean ifBlack, int[] stand, String sta) {
        super(ifBlack, stand, 9, sta);
    }

    @Override
    public ArrayList<String> findPossibleMoves(int[][] arr) {
        ArrayList<String> possibleMoves = new ArrayList<>();
        int [] sta = getStand();

        int i = sta[0]-1;
        int j = sta[1]+1;
        while(i > 0 && i < 9 && j > 0 && j < 9) {
            if(arr[i][j] == 0) possibleMoves.add(DecoEncoderHelper.codePosition(i,j));
            else if(arr[i][j] == -1) {
                possibleMoves.add(DecoEncoderHelper.codePosition(i,j));
                break;
            } else break;
            i--;
            j++;
        }

        i = sta[0] + 1;
        j = sta[1] + 1;
        while(i > 0 && i < 9 && j > 0 && j < 9) {
            if(arr[i][j] == 0) possibleMoves.add(DecoEncoderHelper.codePosition(i,j));
            else if(arr[i][j] == -1) {
                possibleMoves.add(DecoEncoderHelper.codePosition(i,j));
                break;
            } else break;
            i++;
            j++;
        }

        i = sta[0] + 1;
        j = sta[1] - 1;
        while(i > 0 && i < 9 && j > 0 && j < 9) {
            if(arr[i][j] == 0) possibleMoves.add(DecoEncoderHelper.codePosition(i,j));
            else if(arr[i][j] == -1) {
                possibleMoves.add(DecoEncoderHelper.codePosition(i,j));
                break;
            } else break;
            i++;
            j--;
        }

        i = sta[0] - 1;
        j = sta[1] - 1;
        while(i > 0 && i < 9 && j > 0 && j < 9) {
            if(arr[i][j] == 0) possibleMoves.add(DecoEncoderHelper.codePosition(i,j));
            else if(arr[i][j] == -1) {
                possibleMoves.add(DecoEncoderHelper.codePosition(i,j));
                break;
            } else break;
            i--;
            j--;
        }

        i = sta[0]+1;
        j = sta[1];
        while(i > 0 && i < 9) {
            if(arr[i][j] == 0) possibleMoves.add(DecoEncoderHelper.codePosition(i,j));
            else if(arr[i][j] == -1) {
                possibleMoves.add(DecoEncoderHelper.codePosition(i,j));
                break;
            } else break;
            i++;
        }

        i = sta[0]-1;
        j = sta[1];
        while(i > 0 && i < 9) {
            if(arr[i][j] == 0) possibleMoves.add(DecoEncoderHelper.codePosition(i,j));
            else if(arr[i][j] == -1) {
                possibleMoves.add(DecoEncoderHelper.codePosition(i,j));
                break;
            } else break;
            i--;
        }

        i = sta[0];
        j = sta[1]+1;
        while(j > 0 && j < 9) {
            if(arr[i][j] == 0) possibleMoves.add(DecoEncoderHelper.codePosition(i,j));
            else if(arr[i][j] == -1) {
                possibleMoves.add(DecoEncoderHelper.codePosition(i,j));
                break;
            } else break;
            j++;
        }

        i = sta[0];
        j = sta[1]-1;
        while(j > 0 && j < 9) {
            if(arr[i][j] == 0) possibleMoves.add(DecoEncoderHelper.codePosition(i,j));
            else if(arr[i][j] == -1) {
                possibleMoves.add(DecoEncoderHelper.codePosition(i,j));
                break;
            } else break;
            j--;
        }

        return possibleMoves;
    }


    @Override
    public String getNameFigure() {
        return nameOfFigure;
    }
}
