package application;

import figures.*;
import helpers.DecoEncoderHelper;
import helpers.GameHelper;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.ArrayList;

public class Game {
    public boolean ifWhite = true;
    public boolean stageSelectOfPawn = true;
    public boolean timeOnEnemy;
    public boolean moveToSend = false;
    public ConnectionHandler connectionThread;
    private boolean longCastling = false;
    private boolean shortCastling = false;
    private boolean doneCastling = false;

    private int[][] chessboard;                                                                            

    public Figure selectedFigure = null;
    ArrayList<String> possibleMoves = new ArrayList<>();

    private Main main;

    private ArrayList<Figure> whiteFigures = new ArrayList<>();
    private ArrayList<Figure> blackFigures = new ArrayList<>();
    public String opponentStart;
    public String opponentEnd;
    public Figure opponentFigure;
    public BooleanProperty opponentMakeMove = new SimpleBooleanProperty();
    public String sending;
    private GameHelper gameHelper= new GameHelper();

    public Game() {
        chessboard = new int[9][9];
        gameHelper.prepareChessBoard(chessboard);
        gameHelper.prepareFigures(whiteFigures, blackFigures);

    }

    public int onClick(String where) {
        int res = 0;
        if (stageSelectOfPawn) {
            possibleMoves = choosingFigure(where);
            if (possibleMoves.isEmpty()) res = 0;
            else res = 2;
        }
        else {
            Figure beforeSelected = selectedFigure;
            ArrayList<String> newPossible;
            newPossible = choosingFigure(where);
            if (!newPossible.isEmpty()) {
                res = 4;
                possibleMoves.clear();
                possibleMoves.addAll(newPossible);
            } else { //choosing move
                selectedFigure = beforeSelected;
                for (String field : possibleMoves) {
                    if (field.equals(where)) {
                        res = 1;
                        //delete figure if was beat
                        gameHelper.ifBeating(where, whiteFigures, blackFigures, ifWhite);
                        selectedFigure.doneMove();
                        gameHelper.updateChessBoard(chessboard, selectedFigure.getStand(), DecoEncoderHelper.decodePosition(where));
                        sending = selectedFigure.getStandInString() + ':' + where;
                        break;
                    }

                }
                //if not on list possible moves then skip
                if (res == 0) {
                    System.out.println("Wykonanie ruchu w to miejsce jest niemożliwe");
                    res = 3;
                }
                //choosing pawn
                stageSelectOfPawn = true;
                possibleMoves.clear();
            }
        }
        return res;
    }


    public ArrayList<String> choosingFigure(String where) {
        ArrayList<String> possible = new ArrayList<>();
        selectedFigure = null;
        selectedFigure = gameHelper.isHereFigurePlayerX(ifWhite, where, whiteFigures, blackFigures);
        if (selectedFigure != null) { //selected figure
            possible= selectedFigure.findPossibleMoves(chessboard);
            if (possible.isEmpty()) {
                selectedFigure = null;
            } else { //selected figure with posibble moves
                boolean kingSelected = false;
                if (selectedFigure.getNameFigure().equals("king")) kingSelected = true;
                possible = gameHelper.posibleMovesWithoutCheck(chessboard, ifWhite, whiteFigures, blackFigures, selectedFigure.getStand(), possible, kingSelected,possibleMoves,selectedFigure,shortCastling,longCastling);
                if (!possible.isEmpty()) {
                    //if list not empty end
                    stageSelectOfPawn = false;
                }
            }
        }
        return possible;
    }

    public boolean opponentMakeMove(String start, String end) {
        Figure moved = gameHelper.isHereFigurePlayerX(!ifWhite, start, whiteFigures, blackFigures);
        Figure capture = gameHelper.isHereFigurePlayerX(ifWhite, end, whiteFigures, blackFigures);
        if (capture!=null) {
            if (ifWhite) { whiteFigures.remove(capture); }
            else { blackFigures.remove(capture); }
        }
        gameHelper.reverseChessBoard(chessboard);
        gameHelper.updateChessBoard(chessboard, moved.getStand(), DecoEncoderHelper.decodePosition(end));
        gameHelper.reverseChessBoard(chessboard);
        moved.steStand(DecoEncoderHelper.decodePosition(end));
        opponentStart = start;
        opponentEnd = end;
        opponentFigure = moved;
        opponentMakeMove.set(true);
        return gameHelper.checkIfCheckMat(chessboard, ifWhite, whiteFigures, blackFigures, possibleMoves, selectedFigure, shortCastling,longCastling);
    }


    public void updateStandOfFigure(String stand) {
        selectedFigure.steStand(DecoEncoderHelper.decodePosition(stand));
    }

    // reset change of castle
    public void resetCastle() {
        doneCastling = false;
        longCastling = false;
        shortCastling = false;
    }

    public void clearPossibleMoves() {
        possibleMoves.clear();
    }

    public void howeverBlack( ) {
        this.ifWhite = false;
        this.timeOnEnemy = true;
        gameHelper.reverseChessBoard(chessboard);
    }

    public ArrayList<String> getPossibleMoves() {
        return possibleMoves;
    }

    public void theEnd(String who) {

        String[] result=gameHelper.resultOfGame(who,ifWhite);
        connectionThread.stop = true;
        main.backToLoginView(result[0], result[1]);
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public void setConnectionThread(ConnectionHandler connectionHandler) {
        this.connectionThread = connectionHandler;
    }
}
