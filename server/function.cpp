#include <sys/types.h>
#include <sys/wait.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <netdb.h>
#include <signal.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <pthread.h>
#include <iostream>
#include <vector>
#include <queue>
#include "function.hpp"
#include "figure.hpp"
#include "position.h"
#include "pawn.hpp"
#include "bishop.hpp"
#include "rook.hpp"
#include "knight.hpp"
#include "king.hpp"
#include "queen.hpp"


#define BUFF_SIZE 5

using namespace std;


Position decodePosition(string stand) {
    int arr[2];
    switch (stand.at(0)) {
        case 'A':
            arr[0] = 1;
            break;
        case 'B':
            arr[0] = 2;
            break;
        case 'C':
            arr[0] = 3;
            break;
        case 'D':
            arr[0] = 4;
            break;
        case 'E':
            arr[0] = 5;
            break;
        case 'F':
            arr[0] = 6;
            break;
        case 'G':
            arr[0] = 7;
            break;
        case 'H':
            arr[0] = 8;
            break;
    }
    arr[1] = stand.at(1) - '0';
    Position sta;
    sta.k = arr[0];
    sta.w = arr[1];
    return sta;
}


string codePosition(int a, int b) {
    string stand;
    switch (a) {
        case 1:
            stand.append("A");
            break;
        case 2:
            stand.append("B");
            break;
        case 3:
            stand.append("C");
            break;
        case 4:
            stand.append("D");
            break;
        case 5:
            stand.append("E");
            break;
        case 6:
            stand.append("F");
            break;
        case 7:
            stand.append("G");
            break;
        case 8:
            stand.append("H");
            break;
    }
    stand.append(to_string(b));
    return stand;
}


bool checkIfCorrect(char *bufor) {
    bool answer = false;
    int z1 = (int) bufor[0];
    int z2 = (int) bufor[1];
    int z3 = (int) bufor[2];
    int z4 = (int) bufor[3];
    int z5 = (int) bufor[4];
    if ((64 < z1) && (z1 < 73) &&
        (64 < z4) && (z4 < 73) &&
        (48 < z2) && (z2 < 57) &&
        (48 < z5) && (z5 < 57) &&
        (z3 == 58)) {
        answer = true;
    }
    return answer;
}


bool checkIfCheckKingWithMove(int arr[][9], Position potentiallyBeating, Position standKing, vector<Figure *> figuresOponent) {
    int chessboard[9][9];
    for (int i = 1; i < 9; i++)
        for (int j = 1; j < 9; j++)
            chessboard[i][j] = (-1) * arr[i][j];
    bool KingAttack = false;
    for (vector<Figure *>::iterator figure = figuresOponent.begin(); figure != figuresOponent.end(); ++figure) {
        if ((*figure)->stand.k != potentiallyBeating.k || (*figure)->stand.w != potentiallyBeating.w) {
            vector<string> tmp = (*figure)-> findPossibleMoves(chessboard);
            for (vector<string>::iterator move = tmp.begin(); move != tmp.end(); ++move) {
                Position pleace = decodePosition(*move);
                if (pleace.k == standKing.k && pleace.w == standKing.w) {
                    KingAttack = true;
                }
            }
        }
    }
    return KingAttack;
}


vector<string> posibleMovesWithoutCheck(Position standFigure, vector<string> possible, bool ifKing ,int chessboard[][9], Position maybeKing,vector<Figure *> figureOponent) {
    vector<string> possibleWithoutCheck = possible;
    for (vector<string>::iterator it = possibleWithoutCheck.begin(); it != possibleWithoutCheck.end(); ++it) {
        int tmp_chessboard[9][9];
        for (int i = 1; i < 9; i++)
            for (int j = 1; j < 9; j++) tmp_chessboard[i][j] = chessboard[i][j];
        Position newStand = decodePosition(*it);
        tmp_chessboard[standFigure.k][standFigure.w] = 0;
        tmp_chessboard[newStand.k][newStand.w] = 1;
        Position standKing;
        if (ifKing) {
            standKing.k = newStand.k;
            standKing.w = newStand.w;
        } else {
            standKing.k = maybeKing.k;
            standKing.w = maybeKing.w;
        }
        bool whether = checkIfCheckKingWithMove(tmp_chessboard, newStand, standKing, figureOponent);
        if (whether) {
            possibleWithoutCheck.erase(it--);
        }
    }
    return possibleWithoutCheck;
}


bool checkIfCheckmat(vector<Figure *> myFigures, vector<Figure *> figuresOponent, int arr[][9], Position King) {
    for (vector<Figure *>::iterator figure = myFigures.begin(); figure != myFigures.end(); ++figure) {
        bool maybeKing = false;
        if ((*figure)->getNameFigure().compare("king") == 0) maybeKing = true;
        vector<string> possible = (*figure)->findPossibleMoves(arr);
        vector<string> possibleWithoutCheck = posibleMovesWithoutCheck((*figure)->stand, possible, maybeKing,
                                                                           arr, King, figuresOponent);
        if (possibleWithoutCheck.size() > 0) return false;
    }
    return true;
}



void prepareWhiteFigure(vector<Figure *> &whiteFigures){
    for (int i = 1; i < 9; i++) {
        int tab[2] = {i, 2};
        whiteFigures.push_back(new Pawn(false, decodePosition(codePosition(tab[0], tab[1]))));
    }

    whiteFigures.push_back(new Rook(false, decodePosition("A1")));
    whiteFigures.push_back(new Knight(false, decodePosition("B1")));
    whiteFigures.push_back(new Bishop(false, decodePosition("C1")));
    whiteFigures.push_back(new Queen(false, decodePosition("D1")));
    whiteFigures.push_back(new King(false, decodePosition("E1")));
    whiteFigures.push_back(new Bishop(false, decodePosition("F1")));
    whiteFigures.push_back(new Knight(false, decodePosition("G1")));
    whiteFigures.push_back(new Rook(false, decodePosition("H1")));
}


void prepareBlackFigure(vector<Figure *> &blackFigures){
    for (int i = 1; i < 9; i++) {
        int tab[2] = {i, 7};
        blackFigures.push_back(new Pawn(true, decodePosition(codePosition(tab[0], tab[1]))));
    }

    blackFigures.push_back(new Rook(true, decodePosition("A8")));
    blackFigures.push_back(new Knight(true, decodePosition("B8")));
    blackFigures.push_back(new Bishop(true, decodePosition("C8")));
    blackFigures.push_back(new Queen(true, decodePosition("D8")));
    blackFigures.push_back(new King(true, decodePosition("E8")));
    blackFigures.push_back(new Bishop(true, decodePosition("F8")));
    blackFigures.push_back(new Knight(true, decodePosition("G8")));
    blackFigures.push_back(new Rook(true, decodePosition("H8")));
}


void prepareFigure(vector<Figure *> &blackFigures, vector<Figure *> &whiteFigures){
    prepareWhiteFigure(whiteFigures);
    prepareBlackFigure(blackFigures);
}


void writeInfo( int &plOrEn, char * &info, int &result_write){
    int bytes_write = 0;
    while (bytes_write < BUFF_SIZE) {
        result_write = write(plOrEn, info + bytes_write, BUFF_SIZE - bytes_write);
        if (result_write < 1) break;
        bytes_write += result_write;
        }
}