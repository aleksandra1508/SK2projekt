package helpers;

import figures.*;

import java.util.ArrayList;
import java.util.HashSet;

public class GameHelper {

    public static final String NOBODY = "nobody";

    public static final String WHITE = "white";

    public void prepareChessBoard(int[][] chessboard){
        for (int i = 1; i < 9; i++)
            for (int j = 1; j < 9; j++) {
                if(j < 3) {
                    chessboard[i][j] = 1;
                }
                if(j < 7 && j >= 3) {
                    chessboard[i][j] = 0;
                }
                if(j >= 7) {
                    chessboard[i][j] = -1;
                }
            }

    }
    private void prepareWhiteFigures(ArrayList<Figure> whiteFigures){
        for (int i = 1; i < 9; i++) {
            int[] tab = {i, 2};
            whiteFigures.add(new Pawn(false, tab, DecoEncoderHelper.codePosition(tab[0], tab[1])));
        }
        whiteFigures.add(new Rook(false, DecoEncoderHelper.decodePosition("A1"), "A1"));
        whiteFigures.add(new Knight(false, DecoEncoderHelper.decodePosition("B1"), "B1"));
        whiteFigures.add(new Bishop(false, DecoEncoderHelper.decodePosition("C1"), "C1"));
        whiteFigures.add(new Queen(false, DecoEncoderHelper.decodePosition("D1"), "D1"));
        whiteFigures.add(new King(false, DecoEncoderHelper.decodePosition("E1"), "E1"));
        whiteFigures.add(new Bishop(false, DecoEncoderHelper.decodePosition("F1"), "F1"));
        whiteFigures.add(new Knight(false, DecoEncoderHelper.decodePosition("G1"), "G1"));
        whiteFigures.add(new Rook(false, DecoEncoderHelper.decodePosition("H1"), "H1"));

    }

    private void prepareBlackFigures(ArrayList<Figure> blackFigures){
        for (int i = 1; i < 9; i++) {
            int[] tab = {i, 7};
            blackFigures.add(new Pawn(true, tab, DecoEncoderHelper.codePosition(tab[0], tab[1])));
        }
        blackFigures.add(new Rook(true, DecoEncoderHelper.decodePosition("A8"), "A8"));
        blackFigures.add(new Knight(true, DecoEncoderHelper.decodePosition("B8"), "B8"));
        blackFigures.add(new Bishop(true, DecoEncoderHelper.decodePosition("C8"), "C8"));
        blackFigures.add(new Queen(true, DecoEncoderHelper.decodePosition("D8"), "D8"));
        blackFigures.add(new King(true, DecoEncoderHelper.decodePosition("E8"), "E8"));
        blackFigures.add(new Bishop(true, DecoEncoderHelper.decodePosition("F8"), "F8"));
        blackFigures.add(new Knight(true, DecoEncoderHelper.decodePosition("G8"), "G8"));
        blackFigures.add(new Rook(true, DecoEncoderHelper.decodePosition("H8"), "H8"));

    }


    public void prepareFigures(ArrayList<Figure> whiteFigures, ArrayList<Figure> blackFigures){
        prepareWhiteFigures(whiteFigures);
        prepareBlackFigures(blackFigures);

    }

    //check if is there enemy figure and delete
    public void ifBeating(String stand,ArrayList<Figure> whiteFigures, ArrayList<Figure> blackFigures, boolean ifWhite) {
        ArrayList<Figure> list;
        if (ifWhite) list = blackFigures;
        else list = whiteFigures;
        for (Figure figure : list) {
            String staFigure = figure.getStandInString();
            if (stand.equals(staFigure)) {
                System.out.println("Zbicie bierki przeciwnika");
                if (ifWhite) {
                    blackFigures.remove(figure);
                }
                else {
                    whiteFigures.remove(figure);
                }
                break;
            }
        }
    }


    public Figure isHereFigurePlayerX(Boolean white, String where,ArrayList<Figure> whiteFigures, ArrayList<Figure> blackFigures) {
        if (white) {
            for (Figure figure : whiteFigures) {
                String standFigure = figure.getStandInString();
                if (standFigure.equals(where)) return figure;
            } return null;
        } else {
            for (Figure figure : blackFigures) {
                String standFigure = figure.getStandInString();
                if (standFigure.equals(where)) return figure;
            } return null;
        }
    }

    public int[] findPossitionOfKing(boolean ifWhite, ArrayList<Figure> whiteFigures, ArrayList<Figure> blackFigures) {
        int[] standKing = new int[2];
        ArrayList<Figure> list;

        if (ifWhite) list = whiteFigures;
        else list = blackFigures;

        for (Figure figure : list)
            if (figure.getNameFigure().equals("king")) standKing = figure.getStand();

        return standKing;
    }


    public ArrayList<String> posibleMovesWithoutCheck(int[][] chessboard,boolean ifWhite, ArrayList<Figure> whiteFigures, ArrayList<Figure> blackFigures,
                                                      int[] stand, ArrayList<String> possible, boolean kingSelected
                                                    , ArrayList<String> possibleMoves, Figure selectedFigure
                                                    , boolean shortCastling, boolean longCastling) {
        int[][] tmpChessBoard = new int[9][9];
        boolean whether;
        int[] newStand;
        int[] sta = findPossitionOfKing(ifWhite, whiteFigures, blackFigures);
        int[] standKing = new int[2];
        ArrayList<String> toDeleted = new ArrayList<>();

        for (String move : possible) {
            for (int i = 1; i < 9; i++)
                for (int j = 1; j < 9; j++) tmpChessBoard[i][j] = chessboard[i][j];
            newStand = DecoEncoderHelper.decodePosition(move);
            tmpChessBoard[stand[0]][stand[1]] = 0;
            tmpChessBoard[newStand[0]][newStand[1]] = 1;

            if (kingSelected) {
                standKing[0] = newStand[0];
                standKing[1] = newStand[1];
            } else {
                standKing[0] = sta[0];
                standKing[1] = sta[1];
            }

            whether = checkIfCheck(tmpChessBoard, newStand, standKing,ifWhite,whiteFigures,blackFigures);
            if (whether) {
                toDeleted.add(move);
            }
        }
        if (kingSelected) addPossibleCastling(chessboard,ifWhite,possibleMoves,whiteFigures,blackFigures, selectedFigure,shortCastling,longCastling);
        possible.removeAll(toDeleted);
        return possible;
    }


    public void posibiltityOfCastling(int[][] chessboard, ArrayList<Figure> whiteFigures, ArrayList<Figure> blackFigures
                                     ,boolean ifWhite, Figure selectedFigure, ArrayList<String> fieldTransition, boolean longC
            ,boolean shortCastling, boolean longCastling) {
        int[][] tmpChessBoard = new int[9][9];
        int[] newStand;
        int[] stand = selectedFigure.getStand();
        int count = 0;
        boolean whether;

        for (String transition : fieldTransition) {
            for (int i = 1; i < 9; i++)
                for (int j = 1; j < 9; j++)
                    tmpChessBoard[i][j] = chessboard[i][j];
            newStand = DecoEncoderHelper.decodePosition(transition);
            tmpChessBoard[stand[0]][stand[1]] = 0;
            tmpChessBoard[newStand[0]][newStand[1]] = 1;

            whether = checkIfCheck(tmpChessBoard, newStand, newStand, ifWhite, whiteFigures, blackFigures);
            if (!whether) count++;
        }
        if (count == 3) {
            if (longC) longCastling = true;
            else shortCastling = true;
        }
    }

    public void updateChessBoard(int[][] chessboard, int[] old, int[] newOne) {
        int i = old[0];
        int j = old[1];
        chessboard[i][j] = 0;
        i = newOne[0];
        j = newOne[1];
        chessboard[i][j] = 1;
    }

    public void reverseChessBoard(int[][] chessboard ) {
        for (int i = 1; i < 9; i++)
            for (int j = 1; j < 9; j++)
                chessboard[i][j] = (-1) * chessboard[i][j];
    }

    public boolean checkIfCheck(int[][] chessboard, ArrayList<Figure> whiteFigures
            , ArrayList<Figure> blackFigures, boolean ifWhite) {
        reverseChessBoard(chessboard);

        ArrayList<String> tmp;
        boolean ans;

        tmp = listOfFieldsUnderAttack(chessboard, ifWhite, whiteFigures, blackFigures);
        if (tmp != null) {
            ans = checkIfKingOnFieldUnderAttack(tmp, findPossitionOfKing(ifWhite, whiteFigures, blackFigures));
            if (ans) {
                reverseChessBoard(chessboard);
                return true;
            }
        }
        reverseChessBoard(chessboard);
        return false;
    }

    public boolean checkIfCheckMat(int[][] chessboard,boolean ifWhite
            , ArrayList<Figure> whiteFigures, ArrayList<Figure> blackFigures, ArrayList<String> possibleMoves
            , Figure selectedFigure
            , boolean shortCastling, boolean longCastling) {
        ArrayList<Figure> list;
        if (ifWhite) list = whiteFigures;
        else list = blackFigures;
        boolean kingMove;

        for (Figure figure : list) {
            possibleMoves.clear();
            possibleMoves = figure.findPossibleMoves(chessboard);
            if (figure.getNameFigure().equals("king")) kingMove = true;
            else kingMove = false;

            posibleMovesWithoutCheck(chessboard, ifWhite, whiteFigures, blackFigures, figure.getStand(), possibleMoves, kingMove,possibleMoves,selectedFigure,shortCastling,longCastling);
            if (possibleMoves.size() != 0) {
                possibleMoves.clear();
                return false;
            }
        }
        return true;
    }

    public boolean checkIfCheck(int[][] arr, int[] potentiallyBeating, int[] standKing, boolean ifWhite,
                                ArrayList<Figure> whiteFigures, ArrayList<Figure> blackFigures) {
        int[][] chessBoa = new int[9][9];
        for (int i = 1; i < 9; i++)
            for (int j = 1; j < 9; j++)
                chessBoa[i][j] = (-1) * arr[i][j];

        HashSet<String> fieldsUnderAttack = new HashSet<>();
        ArrayList<String> tmp = new ArrayList<>();
        ArrayList<Figure> listOfFigures;

        if (ifWhite) listOfFigures = blackFigures;
        else listOfFigures = whiteFigures;

        Figure potentiallyBeatFigure = isHereFigurePlayerX(!ifWhite, DecoEncoderHelper.codePosition(potentiallyBeating[0],potentiallyBeating[1]), whiteFigures, blackFigures);

        for (Figure figure : listOfFigures) {
            if (figure != potentiallyBeatFigure) {
                tmp.clear();
                tmp = figure.findPossibleMoves(chessBoa);
                fieldsUnderAttack.addAll(tmp);
            }
        }
        tmp.clear();
        tmp.addAll(fieldsUnderAttack);

        boolean odp;

        if (!tmp.isEmpty()) {
            odp = checkIfKingOnFieldUnderAttack(tmp, standKing);
            return odp;
        }
        return false;
    }

    public boolean checkIfKingOnFieldUnderAttack(ArrayList<String> fieldsUnderAttack, int[] standKing) {
        int[] stand;
        if (!fieldsUnderAttack.isEmpty())
            for (String field : fieldsUnderAttack) {
                stand = DecoEncoderHelper.decodePosition(field);
                if (stand[0] == standKing[0] && stand[1] == standKing[1])
                    return true;
            }
        return false;
    }

    public ArrayList<String> listOfFieldsUnderAttack(int[][] chessboard, boolean attackingColorIsBlack, ArrayList<Figure> whiteFigures, ArrayList<Figure> blackFigures) {
        HashSet<String> fieldsUnderAttack = new HashSet<>();
        ArrayList<String> tmp = new ArrayList<>();
        ArrayList<Figure> listOfFigure;

        if (attackingColorIsBlack) listOfFigure = blackFigures;
        else listOfFigure = whiteFigures;

        for (Figure figure : listOfFigure) {
            tmp.clear();
            tmp = figure.findPossibleMoves(chessboard);
            fieldsUnderAttack.addAll(tmp);
        }
        tmp.clear();
        tmp.addAll(fieldsUnderAttack);
        return tmp;
    }

    public void addPossibleCastling(int[][] chessboard,boolean ifWhite, ArrayList<String> possibleMoves
            ,ArrayList<Figure> whiteFigures,ArrayList<Figure> blackFigures, Figure selectedFigure
            , boolean shortCastling, boolean longCastling) {
        ArrayList<Figure> list;
        if (ifWhite) list = whiteFigures;
        else list = blackFigures;

        ArrayList<String> fieldTransition = new ArrayList<>();

        if (selectedFigure.getCountOfMoves() == 0)
            for (Figure figure : list) {
                if (figure.getFieldStart().equals("A1")) {
                    if (figure.getCountOfMoves() == 0 &&
                            chessboard[2][1] == 0 &&
                            chessboard[3][1] == 0 &&
                            chessboard[4][1] == 0) {
                        fieldTransition.add("D1");
                        fieldTransition.add("C1");
                        fieldTransition.add("E1");
                        posibiltityOfCastling(chessboard,whiteFigures,blackFigures,ifWhite,selectedFigure,fieldTransition, true, shortCastling, longCastling);
                        fieldTransition.clear();
                    }
                } else if (figure.getFieldStart().equals("A8")) {
                    if (figure.getCountOfMoves() == 0 &&
                            chessboard[2][8] == 0 &&
                            chessboard[3][8] == 0 &&
                            chessboard[4][8] == 0) {
                        fieldTransition.add("D8");
                        fieldTransition.add("C8");
                        fieldTransition.add("E8");
                        posibiltityOfCastling(chessboard,whiteFigures,blackFigures,ifWhite,selectedFigure,fieldTransition, true, shortCastling, longCastling);
                        fieldTransition.clear();
                    }
                } else if (figure.getFieldStart().equals("H8")) {
                    if (figure.getCountOfMoves() == 0 &&
                            chessboard[6][8] == 0 &&
                            chessboard[7][8] == 0) {
                        fieldTransition.add("F8");
                        fieldTransition.add("G8");
                        fieldTransition.add("E8");
                        posibiltityOfCastling(chessboard,whiteFigures,blackFigures,ifWhite,selectedFigure,fieldTransition, false, shortCastling, longCastling);
                        fieldTransition.clear();
                    }
                } else if (figure.getFieldStart().equals("H1")) {
                    if (figure.getCountOfMoves() == 0 &&
                            chessboard[6][1] == 0 &&
                            chessboard[7][1] == 0) {
                        fieldTransition.add("F1");
                        fieldTransition.add("G1");
                        fieldTransition.add("E1");
                        posibiltityOfCastling(chessboard,whiteFigures,blackFigures,ifWhite,selectedFigure, fieldTransition, false,shortCastling,longCastling);
                        fieldTransition.clear();
                    }
                }
            }

        if (ifWhite) {
            if (shortCastling) possibleMoves.add("G1");
            if (longCastling) possibleMoves.add("C1");
        } else {
            if (shortCastling) possibleMoves.add("G8");
            if (longCastling) possibleMoves.add("C8");
        }
    }

    public String[] resultOfGame(String who, boolean ifWhite){
        String title;
        String content;
        if (who.equals(NOBODY)) {
            title = "Wystąpił błąd";
            content = "Nastąpił problem z połączeniem. Powrót do okna logowania.";
        } else if (who.equals(WHITE)) {
            content = "Zwyciężyły białe";
            if (ifWhite) title = "Zwycięstwo!";
            else title = "Porażka...";
        } else {
            content = "Zwyciężyły czarne";
            if (!ifWhite) title = "Zwycięstwo!";
            else title = "Porażka...";
        }
        return new String[]{title,content};

    }
}
