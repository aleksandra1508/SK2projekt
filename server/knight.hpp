#ifndef KNIGHT_HPP
#define KNIGHT_HPP
#include "position.h"
#include "figure.hpp"
#include <iostream>
#include <string.h>
#include <vector>

using namespace std;


class Knight : public Figure {
public:
    Knight(bool black, Position start) {
        colorBlack = black;
        stand = start;
    };
    string getNameFigure();
    vector<string> findPossibleMoves(int tab[][9]);
};


#endif