#ifndef BISHOP_HPP
#define BISHOP_HPP
#include <iostream>
#include <string.h>
#include <vector>
#include "position.h"
#include "figure.hpp"
#include "function.hpp"

using namespace std;


class Bishop : public Figure {
public:
    Bishop(bool black, Position start) {
        colorBlack = black;
        stand = start;
    };
    string getNameFigure();
    vector<string> findPossibleMoves(int arr[][9]);
};


#endif