#include <iostream>
#include <string.h>
#include <vector>
#include "knight.hpp"
#include "function.hpp"

using namespace std;


string Knight :: getNameFigure() {
    return "knight";
};


vector<string> Knight :: findPossibleMoves(int arr[][9]) {
    vector<string> possibleMoves;
    int sta[2];
    sta[0] = stand.k;
    sta[1] = stand.w;
    for (int i = 1; i < 9; i++)
        for (int j = 1; j < 9; j++) {
            if (arr[i][j] != 1) {
                if (i - 2 == sta[0] && j + 1 == sta[1]) possibleMoves.push_back(codePosition(i, j));
                if (i - 1 == sta[0] && j + 2 == sta[1]) possibleMoves.push_back(codePosition(i, j));
                if (i + 1 == sta[0] && j + 2 == sta[1]) possibleMoves.push_back(codePosition(i, j));
                if (i + 2 == sta[0] && j + 1 == sta[1]) possibleMoves.push_back(codePosition(i, j));
                if (i + 2 == sta[0] && j - 1 == sta[1]) possibleMoves.push_back(codePosition(i, j));
                if (i + 1 == sta[0] && j - 2 == sta[1]) possibleMoves.push_back(codePosition(i, j));
                if (i - 1 == sta[0] && j - 2 == sta[1]) possibleMoves.push_back(codePosition(i, j));
                if (i - 2 == sta[0] && j - 1 == sta[1]) possibleMoves.push_back(codePosition(i, j));
            }
        }
    return possibleMoves;
};