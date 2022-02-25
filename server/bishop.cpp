#include <iostream>
#include <string.h>
#include <vector>
#include "bishop.hpp"

using namespace std;


string Bishop :: getNameFigure() {
    return "bishop";
};


vector<string> Bishop :: findPossibleMoves(int arr[][9]) {
    vector<string> possibleMoves;
    int sta[2];
    sta[0] = stand.k;
    sta[1] = stand.w;
    int i = sta[0] - 1;
    int j = sta[1] + 1;
    while (i > 0 && i < 9 && j > 0 && j < 9) {
        if (arr[i][j] == 0) possibleMoves.push_back(codePosition(i, j));
        else if (arr[i][j] == -1) {
            possibleMoves.push_back(codePosition(i, j));
            break;
        } else break;
        i--;
        j++;
    }
    i = sta[0] + 1;
    j = sta[1] + 1;
    while (i > 0 && i < 9 && j > 0 && j < 9) {
        if (arr[i][j] == 0) possibleMoves.push_back(codePosition(i, j));
        else if (arr[i][j] == -1) {
            possibleMoves.push_back(codePosition(i, j));
            break;
        } else break;
        i++;
        j++;
    }
    i = sta[0] + 1;
    j = sta[1] - 1;
    while (i > 0 && i < 9 && j > 0 && j < 9) {
        if (arr[i][j] == 0) possibleMoves.push_back(codePosition(i, j));
        else if (arr[i][j] == -1) {
            possibleMoves.push_back(codePosition(i, j));
            break;
        } else break;
        i++;
        j--;
    }
    i = sta[0] - 1;
    j = sta[1] - 1;
    while (i > 0 && i < 9 && j > 0 && j < 9) {
        if (arr[i][j] == 0) possibleMoves.push_back(codePosition(i, j));
        else if (arr[i][j] == -1) {
            possibleMoves.push_back(codePosition(i, j));
            break;
        } else break;
        i--;
        j--;
    }
    return possibleMoves;
};