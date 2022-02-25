#include <iostream>
#include <string.h>
#include <vector>
#include "king.hpp"
#include "function.hpp"

using namespace std;


string King :: getNameFigure() {
    return "king";
};


vector<string> King :: findPossibleMoves(int arr[][9]) {
    vector<string> possibleMoves;
    int sta[2];
    sta[0] = stand.k;
    sta[1] = stand.w;
    int i = sta[0];
    int j = sta[1];
    if (i - 1 > 0 && j - 1 > 0)
        if (arr[i - 1][j - 1] != 1) possibleMoves.push_back(codePosition(i - 1, j - 1));
    if (i - 1 > 0)
        if (arr[i - 1][j] != 1) possibleMoves.push_back(codePosition(i - 1, j));
    if (i - 1 > 0 && j + 1 < 9)
        if (arr[i - 1][j + 1] != 1) possibleMoves.push_back(codePosition(i - 1, j + 1));
    if (j + 1 < 9)
        if (arr[i][j + 1] != 1) possibleMoves.push_back(codePosition(i, j + 1));
    if (i + 1 < 9 && j + 1 < 9)
        if (arr[i + 1][j + 1] != 1) possibleMoves.push_back(codePosition(i + 1, j + 1));
    if (i + 1 < 9)
        if (arr[i + 1][j] != 1) possibleMoves.push_back(codePosition(i + 1, j));
    if (i + 1 < 9 && j - 1 > 0)
        if (arr[i + 1][j - 1] != 1) possibleMoves.push_back(codePosition(i + 1, j - 1));
    if (j - 1 > 0)
        if (arr[i][j - 1] != 1) possibleMoves.push_back(codePosition(i, j - 1));
    return possibleMoves;
};