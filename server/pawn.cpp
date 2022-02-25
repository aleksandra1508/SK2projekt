#include "pawn.hpp"
#include <iostream>
#include <string.h>
#include <vector>
#include "function.hpp"

using namespace std;


string Pawn :: getNameFigure() {
        return "pawn";
    };


vector<string> Pawn :: findPossibleMoves(int arr[][9]) {
    vector<string> possibleMoves;
    int sta[2];
    sta[0] = stand.k;
    sta[1] = stand.w;
    if (colorBlack) {
        if (sta[1] != 1) {
            if (arr[sta[0]][sta[1] - 1] == 0) {
                if (countOfMoves == 0 && arr[sta[0]][sta[1] - 2] == 0)
                    possibleMoves.push_back(codePosition(sta[0], sta[1] - 2));
                possibleMoves.push_back(codePosition(sta[0], sta[1] - 1));
            }
            if (sta[0] - 1 > 0 && sta[1] - 1 > 0)
                if (arr[sta[0] - 1][sta[1] - 1] == -1)
                    possibleMoves.push_back(codePosition(sta[0] - 1, sta[1] - 1));
            if (sta[0] + 1 < 9 && sta[1] - 1 > 0)
                if (arr[sta[0] + 1][sta[1] - 1] == -1)
                    possibleMoves.push_back(codePosition(sta[0] + 1, sta[1] - 1));
        }
    } else {
        if (sta[1] != 8) {
            if (arr[sta[0]][sta[1] + 1] == 0) {
                if (countOfMoves == 0 && arr[sta[0]][sta[1] + 2] == 0)
                    possibleMoves.push_back(codePosition(sta[0], sta[1] + 2));
                possibleMoves.push_back(codePosition(sta[0], sta[1] + 1));
            }
            if (sta[0] - 1 > 0 && sta[1] + 1 < 9)
                if (arr[sta[0] - 1][sta[1] + 1] == -1)
                    possibleMoves.push_back(codePosition(sta[0] - 1, sta[1] + 1));
            if (sta[0] + 1 < 9 && sta[1] + 1 < 9)
                if (arr[sta[0] + 1][sta[1] + 1] == -1)
                    possibleMoves.push_back(codePosition(sta[0] + 1, sta[1] + 1));
        }
    }
    return possibleMoves;
};