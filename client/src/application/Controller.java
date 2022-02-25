package application;

import figures.Figure;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    public static final String BLACK = "black";
    public ImageView a1, a2, a3, a4, a5, a6, a7, a8,
            b1, b2, b3, b4, b5, b6, b7, b8,
            c1, c2, c3, c4, c5, c6, c7, c8,
            d1, d2, d3, d4, d5, d6, d7, d8,
            e1, e2, e3, e4, e5, e6, e7, e8,
            f1, f2, f3, f4, f5, f6, f7, f8,
            g1, g2, g3, g4, g5, g6, g7, g8,
            h1, h2, h3, h4, h5, h6, h7, h8;

    public Pane a1pane, a2pane, a3pane, a4pane, a5pane, a6pane, a7pane, a8pane,
            b1pane, b2pane, b3pane, b4pane, b5pane, b6pane, b7pane, b8pane,
            c1pane, c2pane, c3pane, c4pane, c5pane, c6pane, c7pane, c8pane,
            d1pane, d2pane, d3pane, d4pane, d5pane, d6pane, d7pane, d8pane,
            e1pane, e2pane, e3pane, e4pane, e5pane, e6pane, e7pane, e8pane,
            f1pane, f2pane, f3pane, f4pane, f5pane, f6pane, f7pane, f8pane,
            g1pane, g2pane, g3pane, g4pane, g5pane, g6pane, g7pane, g8pane,
            h1pane, h2pane, h3pane, h4pane, h5pane, h6pane, h7pane, h8pane;

    public Label yourLogin;
    public Label whoNow;
    public Label enemyLogin;


    private Game game = new Game();
    private ArrayList<String> greenFields = new ArrayList<>();


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        game.opponentMakeMove.addListener((observable, oldValue, newValue) -> {
            // Only if completed
            if (newValue) {
                moveFigureOpponent();
                game.opponentMakeMove.set(false);
            }
        });
    }

    public void startGame(String color, Main main) {
        if (color.equals(BLACK)) {
            System.out.println("czarny");
            game.howeverBlack();
        }
        game.setMain(main);
        String my;
        String opponent;
        if (game.ifWhite) {
            whoNow.setVisible(false);
            my = "white";
            opponent = "black";
            d1.setImage(new Image("file:figureImages/"+my+"_queen.png"));
            e1.setImage(new Image("file:figureImages/"+my+"_king.png"));
            d8.setImage(new Image("file:figureImages/"+opponent+"_queen.png"));
            e8.setImage(new Image("file:figureImages/"+opponent+"_king.png"));
        } else {
            whoNow.setVisible(true);
            my = "black";
            opponent = "white";
            d1.setImage(new Image("file:figureImages/"+my+"_king.png"));
            e1.setImage(new Image("file:figureImages/"+my+"_queen.png"));
            d8.setImage(new Image("file:figureImages/"+opponent+"_king.png"));
            e8.setImage(new Image("file:figureImages/"+opponent+"_queen.png"));
        }
        a1.setImage(new Image("file:figureImages/"+my+"_rook.png"));
        b1.setImage(new Image("file:figureImages/"+my+"_knight.png"));
        c1.setImage(new Image("file:figureImages/"+my+"_bishop.png"));
        f1.setImage(new Image("file:figureImages/"+my+"_bishop.png"));
        g1.setImage(new Image("file:figureImages/"+my+"_knight.png"));
        h1.setImage(new Image("file:figureImages/"+my+"_rook.png"));
        a2.setImage(new Image("file:figureImages/"+my+"_pawn.png"));
        b2.setImage(new Image("file:figureImages/"+my+"_pawn.png"));
        c2.setImage(new Image("file:figureImages/"+my+"_pawn.png"));
        d2.setImage(new Image("file:figureImages/"+my+"_pawn.png"));
        e2.setImage(new Image("file:figureImages/"+my+"_pawn.png"));
        f2.setImage(new Image("file:figureImages/"+my+"_pawn.png"));
        g2.setImage(new Image("file:figureImages/"+my+"_pawn.png"));
        h2.setImage(new Image("file:figureImages/"+my+"_pawn.png"));

        a8.setImage(new Image("file:figureImages/"+opponent+"_rook.png"));
        b8.setImage(new Image("file:figureImages/"+opponent+"_knight.png"));
        c8.setImage(new Image("file:figureImages/"+opponent+"_bishop.png"));
        f8.setImage(new Image("file:figureImages/"+opponent+"_bishop.png"));
        g8.setImage(new Image("file:figureImages/"+opponent+"_knight.png"));
        h8.setImage(new Image("file:figureImages/"+opponent+"_rook.png"));
        a7.setImage(new Image("file:figureImages/"+opponent+"_pawn.png"));
        b7.setImage(new Image("file:figureImages/"+opponent+"_pawn.png"));
        c7.setImage(new Image("file:figureImages/"+opponent+"_pawn.png"));
        d7.setImage(new Image("file:figureImages/"+opponent+"_pawn.png"));
        e7.setImage(new Image("file:figureImages/"+opponent+"_pawn.png"));
        f7.setImage(new Image("file:figureImages/"+opponent+"_pawn.png"));
        g7.setImage(new Image("file:figureImages/"+opponent+"_pawn.png"));
        h7.setImage(new Image("file:figureImages/"+opponent+"_pawn.png"));
    }

    private void moveFigureOpponent() {
        String start = game.opponentStart;
        String end = game.opponentEnd;
        if (!game.ifWhite) {
            start = reverseWhere(start);
            end = reverseWhere(end);
        }
        clearFigure(start);
        movePiece(game.opponentFigure, end);
        whoNow.setVisible(false);

    }

    public void  movePiece(Figure figure, String where) {
        String color, name;
        if (figure.isBlackColor()) color = "black_";
        else color = "white_";
        name = figure.getNameFigure();
        String all = "file:figureImages/"+color+name+".png";
        drawFigure(where, all);

    }

    public void clickOn(String what) {
        if(!game.ifWhite) what = reverseWhere(what);
        if(!game.timeOnEnemy) {
            int wyn = game.onClick(what);
            switch (wyn) {
                case 0: //selected figure without possible moves
                    System.out.println("Brak możliwych ruchów dla wybranego piona");
                    undrawAll();
                    break;
                case 1: // figure move
                    undrawAll();
                    if (!game.ifWhite) {
                        clearFigure(reverseWhere(game.selectedFigure.getStandInString()));
                        what = reverseWhere(what);
                    } else clearFigure(game.selectedFigure.getStandInString());
                    movePiece(game.selectedFigure, what);
                    if(!game.ifWhite) what = reverseWhere(what);
                    game.updateStandOfFigure(what);
                    game.clearPossibleMoves();
                    game.moveToSend = true;
                    whoNow.setVisible(true);

                    break;
                case 2: //correct figure
                    System.out.println("Wybrano piona z możliwymi ruchami");
                    undrawAll();
                    markInGreen();
                    break;

                case 3: //unclicking (field without possible move)
                    undrawAll();
                    game.clearPossibleMoves();
                    game.resetCastle();
                    game.stageSelectOfPawn = true;
                    break;

                case 4: //correct figure after another
                    System.out.println("Wybrano piona z możliwymi ruchami (po innej)");
                    undrawAll();
                    markInGreen();
                    break;
            }
        }
    }

    private String reverseWhere(String where) {
        int fir = 9 - (where.charAt(0) - 64) + 64;
        int sec = 9 - (where.charAt(1) - 48);
        where = (char) fir + Integer.toString(sec);
        return where;
    }

    //delete mark
    public void undrawAll() {
        colorFields(greenFields, "#dbd7d3", "#50393b");
        greenFields.clear();
    }

    // mark possible moves
    public void markInGreen() {
        if (!game.ifWhite) {
            for (String move : game.getPossibleMoves()) {
                move = reverseWhere(move);
                greenFields.add(move);
            }
        } else {
            greenFields.addAll(game.getPossibleMoves());
        }

        colorFields(greenFields, "#46EB48", "#35B628");
    }

    public void drawFigure(String field, String all) {
        switch (field) {
            case "A1": {
                a1.setImage(new Image(all));
                break;
            }
            case "B1": {
                b1.setImage(new Image(all));
                break;
            }
            case "C1": {
                c1.setImage(new Image(all));
                break;
            }
            case "D1": {
                d1.setImage(new Image(all));
                break;
            }
            case "E1": {
                e1.setImage(new Image(all));
                break;
            }
            case "F1": {
                f1.setImage(new Image(all));
                break;
            }
            case "G1": {
                g1.setImage(new Image(all));
                break;
            }
            case "H1": {
                h1.setImage(new Image(all));
                break;
            }

            case "A2": {
                a2.setImage(new Image(all));
                break;
            }
            case "B2": {
                b2.setImage(new Image(all));
                break;
            }
            case "C2": {
                c2.setImage(new Image(all));
                break;
            }
            case "D2": {
                d2.setImage(new Image(all));
                break;
            }
            case "E2": {
                e2.setImage(new Image(all));
                break;
            }
            case "F2": {
                f2.setImage(new Image(all));
                break;
            }
            case "G2": {
                g2.setImage(new Image(all));
                break;
            }
            case "H2": {
                h2.setImage(new Image(all));
                break;
            }

            case "A3": {
                a3.setImage(new Image(all));
                break;
            }
            case "B3": {
                b3.setImage(new Image(all));
                break;
            }
            case "C3": {
                c3.setImage(new Image(all));
                break;
            }
            case "D3": {
                d3.setImage(new Image(all));
                break;
            }
            case "E3": {
                e3.setImage(new Image(all));
                break;
            }
            case "F3": {
                f3.setImage(new Image(all));
                break;
            }
            case "G3": {
                g3.setImage(new Image(all));
                break;
            }
            case "H3": {
                h3.setImage(new Image(all));
                break;
            }

            case "A4": {
                a4.setImage(new Image(all));
                break;
            }
            case "B4": {
                b4.setImage(new Image(all));
                break;
            }
            case "C4": {
                c4.setImage(new Image(all));
                break;
            }
            case "D4": {
                d4.setImage(new Image(all));
                break;
            }
            case "E4": {
                e4.setImage(new Image(all));
                break;
            }
            case "F4": {
                f4.setImage(new Image(all));
                break;
            }
            case "G4": {
                g4.setImage(new Image(all));
                break;
            }
            case "H4": {
                h4.setImage(new Image(all));
                break;
            }

            case "A5": {
                a5.setImage(new Image(all));
                break;
            }
            case "B5": {
                b5.setImage(new Image(all));
                break;
            }
            case "C5": {
                c5.setImage(new Image(all));
                break;
            }
            case "D5": {
                d5.setImage(new Image(all));
                break;
            }
            case "E5": {
                e5.setImage(new Image(all));
                break;
            }
            case "F5": {
                f5.setImage(new Image(all));
                break;
            }
            case "G5": {
                g5.setImage(new Image(all));
                break;
            }
            case "H5": {
                h5.setImage(new Image(all));
                break;
            }

            case "A6": {
                a6.setImage(new Image(all));
                break;
            }
            case "B6": {
                b6.setImage(new Image(all));
                break;
            }
            case "C6": {
                c6.setImage(new Image(all));
                break;
            }
            case "D6": {
                d6.setImage(new Image(all));
                break;
            }
            case "E6": {
                e6.setImage(new Image(all));
                break;
            }
            case "F6": {
                f6.setImage(new Image(all));
                break;
            }
            case "G6": {
                g6.setImage(new Image(all));
                break;
            }
            case "H6": {
                h6.setImage(new Image(all));
                break;
            }

            case "A7": {
                a7.setImage(new Image(all));
                break;
            }
            case "B7": {
                b7.setImage(new Image(all));
                break;
            }
            case "C7": {
                c7.setImage(new Image(all));
                break;
            }
            case "D7": {
                d7.setImage(new Image(all));
                break;
            }
            case "E7": {
                e7.setImage(new Image(all));
                break;
            }
            case "F7": {
                f7.setImage(new Image(all));
                break;
            }
            case "G7": {
                g7.setImage(new Image(all));
                break;
            }
            case "H7": {
                h7.setImage(new Image(all));
                break;
            }

            case "A8": {
                a8.setImage(new Image(all));
                break;
            }
            case "B8": {
                b8.setImage(new Image(all));
                break;
            }
            case "C8": {
                c8.setImage(new Image(all));
                break;
            }
            case "D8": {
                d8.setImage(new Image(all));
                break;
            }
            case "E8": {
                e8.setImage(new Image(all));
                break;
            }
            case "F8": {
                f8.setImage(new Image(all));
                break;
            }
            case "G8": {
                g8.setImage(new Image(all));
                break;
            }
            case "H8": {
                h8.setImage(new Image(all));
                break;
            }
        }
    }

    public void clearFigure(String field) {
        switch (field) {
            case "A1": {
                a1.setImage(null);
                break;
            }
            case "B1": {
                b1.setImage(null);
                break;
            }
            case "C1": {
                c1.setImage(null);
                break;
            }
            case "D1": {
                d1.setImage(null);
                break;
            }
            case "E1": {
                e1.setImage(null);
                break;
            }
            case "F1": {
                f1.setImage(null);
                break;
            }
            case "G1": {
                g1.setImage(null);
                break;
            }
            case "H1": {
                h1.setImage(null);
                break;
            }

            case "A2": {
                a2.setImage(null);
                break;
            }
            case "B2": {
                b2.setImage(null);
                break;
            }
            case "C2": {
                c2.setImage(null);
                break;
            }
            case "D2": {
                d2.setImage(null);
                break;
            }
            case "E2": {
                e2.setImage(null);
                break;
            }
            case "F2": {
                f2.setImage(null);
                break;
            }
            case "G2": {
                g2.setImage(null);
                break;
            }
            case "H2": {
                h2.setImage(null);
                break;
            }

            case "A3": {
                a3.setImage(null);
                break;
            }
            case "B3": {
                b3.setImage(null);
                break;
            }
            case "C3": {
                c3.setImage(null);
                break;
            }
            case "D3": {
                d3.setImage(null);
                break;
            }
            case "E3": {
                e3.setImage(null);
                break;
            }
            case "F3": {
                f3.setImage(null);
                break;
            }
            case "G3": {
                g3.setImage(null);
                break;
            }
            case "H3": {
                h3.setImage(null);
                break;
            }

            case "A4": {
                a4.setImage(null);
                break;
            }
            case "B4": {
                b4.setImage(null);
                break;
            }
            case "C4": {
                c4.setImage(null);
                break;
            }
            case "D4": {
                d4.setImage(null);
                break;
            }
            case "E4": {
                e4.setImage(null);
                break;
            }
            case "F4": {
                f4.setImage(null);
                break;
            }
            case "G4": {
                g4.setImage(null);
                break;
            }
            case "H4": {
                h4.setImage(null);
                break;
            }

            case "A5": {
                a5.setImage(null);
                break;
            }
            case "B5": {
                b5.setImage(null);
                break;
            }
            case "C5": {
                c5.setImage(null);
                break;
            }
            case "D5": {
                d5.setImage(null);
                break;
            }
            case "E5": {
                e5.setImage(null);
                break;
            }
            case "F5": {
                f5.setImage(null);
                break;
            }
            case "G5": {
                g5.setImage(null);
                break;
            }
            case "H5": {
                h5.setImage(null);
                break;
            }

            case "A6": {
                a6.setImage(null);
                break;
            }
            case "B6": {
                b6.setImage(null);
                break;
            }
            case "C6": {
                c6.setImage(null);
                break;
            }
            case "D6": {
                d6.setImage(null);
                break;
            }
            case "E6": {
                e6.setImage(null);
                break;
            }
            case "F6": {
                f6.setImage(null);
                break;
            }
            case "G6": {
                g6.setImage(null);
                break;
            }
            case "H6": {
                h6.setImage(null);
                break;
            }

            case "A7": {
                a7.setImage(null);
                break;
            }
            case "B7": {
                b7.setImage(null);
                break;
            }
            case "C7": {
                c7.setImage(null);
                break;
            }
            case "D7": {
                d7.setImage(null);
                break;
            }
            case "E7": {
                e7.setImage(null);
                break;
            }
            case "F7": {
                f7.setImage(null);
                break;
            }
            case "G7": {
                g7.setImage(null);
                break;
            }
            case "H7": {
                h7.setImage(null);
                break;
            }

            case "A8": {
                a8.setImage(null);
                break;
            }
            case "B8": {
                b8.setImage(null);
                break;
            }
            case "C8": {
                c8.setImage(null);
                break;
            }
            case "D8": {
                d8.setImage(null);
                break;
            }
            case "E8": {
                e8.setImage(null);
                break;
            }
            case "F8": {
                f8.setImage(null);
                break;
            }
            case "G8": {
                g8.setImage(null);
                break;
            }
            case "H8": {
                h8.setImage(null);
                break;
            }
        }
    }

    public void colorFields(ArrayList<String> possibleMoves, String whiteColor, String blackColor) {
        for (String field : possibleMoves) {
            char a = field.charAt(0);
            char b = field.charAt(1);
            String color;
            if (a == 'A' || a == 'C' || a == 'E' || a == 'G') {
                if (b == '2' || b == '4' || b == '6' || b == '8') color = whiteColor;
                else color = blackColor;
            } else {
                if (b == '2' || b == '4' || b == '6' || b == '8') color = blackColor;
                else color = whiteColor;
            }
            switch (field) {
                case "A1": {
                    a1pane.setStyle("-fx-background-color: " + color);
                    break;
                }
                case "B1": {
                    b1pane.setStyle("-fx-background-color: " + color);
                    break;
                }
                case "C1": {
                    c1pane.setStyle("-fx-background-color: " + color);
                    break;
                }
                case "D1": {
                    d1pane.setStyle("-fx-background-color: " + color);
                    break;
                }
                case "E1": {
                    e1pane.setStyle("-fx-background-color: " + color);
                    break;
                }
                case "F1": {
                    f1pane.setStyle("-fx-background-color: " + color);
                    break;
                }
                case "G1": {
                    g1pane.setStyle("-fx-background-color: " + color);
                    break;
                }
                case "H1": {
                    h1pane.setStyle("-fx-background-color: " + color);
                    break;
                }

                case "A2": {
                    a2pane.setStyle("-fx-background-color: " + color);
                    break;
                }
                case "B2": {
                    b2pane.setStyle("-fx-background-color: " + color);
                    break;
                }
                case "C2": {
                    c2pane.setStyle("-fx-background-color: " + color);
                    break;
                }
                case "D2": {
                    d2pane.setStyle("-fx-background-color: " + color);
                    break;
                }
                case "E2": {
                    e2pane.setStyle("-fx-background-color: " + color);
                    break;
                }
                case "F2": {
                    f2pane.setStyle("-fx-background-color: " + color);
                    break;
                }
                case "G2": {
                    g2pane.setStyle("-fx-background-color: " + color);
                    break;
                }
                case "H2": {
                    h2pane.setStyle("-fx-background-color: " + color);
                    break;
                }

                case "A3": {
                    a3pane.setStyle("-fx-background-color: " + color);
                    break;
                }
                case "B3": {
                    b3pane.setStyle("-fx-background-color: " + color);
                    break;
                }
                case "C3": {
                    c3pane.setStyle("-fx-background-color: " + color);
                    break;
                }
                case "D3": {
                    d3pane.setStyle("-fx-background-color: " + color);
                    break;
                }
                case "E3": {
                    e3pane.setStyle("-fx-background-color: " + color);
                    break;
                }
                case "F3": {
                    f3pane.setStyle("-fx-background-color: " + color);
                    break;
                }
                case "G3": {
                    g3pane.setStyle("-fx-background-color: " + color);
                    break;
                }
                case "H3": {
                    h3pane.setStyle("-fx-background-color: " + color);
                    break;
                }

                case "A4": {
                    a4pane.setStyle("-fx-background-color: " + color);
                    break;
                }
                case "B4": {
                    b4pane.setStyle("-fx-background-color: " + color);
                    break;
                }
                case "C4": {
                    c4pane.setStyle("-fx-background-color: " + color);
                    break;
                }
                case "D4": {
                    d4pane.setStyle("-fx-background-color: " + color);
                    break;
                }
                case "E4": {
                    e4pane.setStyle("-fx-background-color: " + color);
                    break;
                }
                case "F4": {
                    f4pane.setStyle("-fx-background-color: " + color);
                    break;
                }
                case "G4": {
                    g4pane.setStyle("-fx-background-color: " + color);
                    break;
                }
                case "H4": {
                    h4pane.setStyle("-fx-background-color: " + color);
                    break;
                }

                case "A5": {
                    a5pane.setStyle("-fx-background-color: " + color);
                    break;
                }
                case "B5": {
                    b5pane.setStyle("-fx-background-color: " + color);
                    break;
                }
                case "C5": {
                    c5pane.setStyle("-fx-background-color: " + color);
                    break;
                }
                case "D5": {
                    d5pane.setStyle("-fx-background-color: " + color);
                    break;
                }
                case "E5": {
                    e5pane.setStyle("-fx-background-color: " + color);
                    break;
                }
                case "F5": {
                    f5pane.setStyle("-fx-background-color: " + color);
                    break;
                }
                case "G5": {
                    g5pane.setStyle("-fx-background-color: " + color);
                    break;
                }
                case "H5": {
                    h5pane.setStyle("-fx-background-color: " + color);
                    break;
                }

                case "A6": {
                    a6pane.setStyle("-fx-background-color: " + color);
                    break;
                }
                case "B6": {
                    b6pane.setStyle("-fx-background-color: " + color);
                    break;
                }
                case "C6": {
                    c6pane.setStyle("-fx-background-color: " + color);
                    break;
                }
                case "D6": {
                    d6pane.setStyle("-fx-background-color: " + color);
                    break;
                }
                case "E6": {
                    e6pane.setStyle("-fx-background-color: " + color);
                    break;
                }
                case "F6": {
                    f6pane.setStyle("-fx-background-color: " + color);
                    break;
                }
                case "G6": {
                    g6pane.setStyle("-fx-background-color: " + color);
                    break;
                }
                case "H6": {
                    h6pane.setStyle("-fx-background-color: " + color);
                    break;
                }

                case "A7": {
                    a7pane.setStyle("-fx-background-color: " + color);
                    break;
                }
                case "B7": {
                    b7pane.setStyle("-fx-background-color: " + color);
                    break;
                }
                case "C7": {
                    c7pane.setStyle("-fx-background-color: " + color);
                    break;
                }
                case "D7": {
                    d7pane.setStyle("-fx-background-color: " + color);
                    break;
                }
                case "E7": {
                    e7pane.setStyle("-fx-background-color: " + color);
                    break;
                }
                case "F7": {
                    f7pane.setStyle("-fx-background-color: " + color);
                    break;
                }
                case "G7": {
                    g7pane.setStyle("-fx-background-color: " + color);
                    break;
                }
                case "H7": {
                    h7pane.setStyle("-fx-background-color: " + color);
                    break;
                }

                case "A8": {
                    a8pane.setStyle("-fx-background-color: " + color);
                    break;
                }
                case "B8": {
                    b8pane.setStyle("-fx-background-color: " + color);
                    break;
                }
                case "C8": {
                    c8pane.setStyle("-fx-background-color: " + color);
                    break;
                }
                case "D8": {
                    d8pane.setStyle("-fx-background-color: " + color);
                    break;
                }
                case "E8": {
                    e8pane.setStyle("-fx-background-color: " + color);
                    break;
                }
                case "F8": {
                    f8pane.setStyle("-fx-background-color: " + color);
                    break;
                }
                case "G8": {
                    g8pane.setStyle("-fx-background-color: " + color);
                    break;
                }
                case "H8": {
                    h8pane.setStyle("-fx-background-color: " + color);
                    break;
                }
            }
        }
    }

    @FXML
    public void setLogins(String you, String enemy) {
        yourLogin.setText(you);
        enemyLogin.setText(enemy);
    }

    public void setConnectionThread(Socket socket) {
        ConnectionHandler connectionHandler = new ConnectionHandler(socket, game);
        Thread connectionThread = new Thread(connectionHandler);
        connectionThread.start();
        game.setConnectionThread(connectionHandler);
    }

    public void clickA1( ) {
        clickOn("A1");
    }
    public void clickB1( ) {
        clickOn("B1");
    }
    public void clickC1( ) {
        clickOn("C1");
    }
    public void clickD1( ) {
        clickOn("D1");
    }
    public void clickE1( ) {
        clickOn("E1");
    }
    public void clickF1( ) {
        clickOn("F1");
    }
    public void clickG1( ) {
        clickOn("G1");
    }
    public void clickH1( ) {
        clickOn("H1");
    }

    public void clickA2( ) {
        clickOn("A2");
    }
    public void clickB2( ) {
        clickOn("B2");
    }
    public void clickC2( ) {
        clickOn("C2");
    }
    public void clickD2( ) {
        clickOn("D2");
    }
    public void clickE2( ) {
        clickOn("E2");
    }
    public void clickF2( ) {
        clickOn("F2");
    }
    public void clickG2( ) {
        clickOn("G2");
    }
    public void clickH2( ) {
        clickOn("H2");
    }

    public void clickA3( ) {
        clickOn("A3");
    }
    public void clickB3( ) {
        clickOn("B3");
    }
    public void clickC3( ) {
        clickOn("C3");
    }
    public void clickD3( ) {
        clickOn("D3");
    }
    public void clickE3( ) {
        clickOn("E3");
    }
    public void clickF3( ) {
        clickOn("F3");
    }
    public void clickG3( ) {
        clickOn("G3");
    }
    public void clickH3( ) {
        clickOn("H3");
    }

    public void clickA4( ) {
        clickOn("A4");
    }
    public void clickB4( ) {
        clickOn("B4");
    }
    public void clickC4( ) {
        clickOn("C4");
    }
    public void clickD4( ) {
        clickOn("D4");
    }
    public void clickE4( ) {
        clickOn("E4");
    }
    public void clickF4( ) {
        clickOn("F4");
    }
    public void clickG4( ) {
        clickOn("G4");
    }
    public void clickH4( ) {
        clickOn("H4");
    }

    public void clickA5( ) {
        clickOn("A5");
    }
    public void clickB5( ) {
        clickOn("B5");
    }
    public void clickC5( ) {
        clickOn("C5");
    }
    public void clickD5( ) {
        clickOn("D5");
    }
    public void clickE5( ) {
        clickOn("E5");
    }
    public void clickF5( ) {
        clickOn("F5");
    }
    public void clickG5( ) {
        clickOn("G5");
    }
    public void clickH5( ) {
        clickOn("H5");
    }

    public void clickA6( ) {
        clickOn("A6");
    }
    public void clickB6( ) {
        clickOn("B6");
    }
    public void clickC6( ) {
        clickOn("C6");
    }
    public void clickD6( ) {
        clickOn("D6");
    }
    public void clickE6( ) {
        clickOn("E6");
    }
    public void clickF6( ) {
        clickOn("F6");
    }
    public void clickG6( ) {
        clickOn("G6");
    }
    public void clickH6( ) {
        clickOn("H6");
    }

    public void clickA7( ){
        clickOn("A7");
    }
    public void clickB7( ){
        clickOn("B7");
    }
    public void clickC7( ) {
        clickOn("C7");
    }
    public void clickD7( ) {
        clickOn("D7");
    }
    public void clickE7( ) {
        clickOn("E7");
    }
    public void clickF7( ) {
        clickOn("F7");
    }
    public void clickG7( ) {
        clickOn("G7");
    }
    public void clickH7( ) {
        clickOn("H7");
    }

    public void clickA8( ) {
        clickOn("A8");
    }
    public void clickB8( ) {
        clickOn("B8");
    }
    public void clickC8( ) {
        clickOn("C8");
    }
    public void clickD8( ) {
        clickOn("D8");
    }
    public void clickE8( ) {
        clickOn("E8");
    }
    public void clickF8( ) {
        clickOn("F8");
    }
    public void clickG8( ) {
        clickOn("G8");
    }
    public void clickH8( ) {
        clickOn("H8");
    }



}
