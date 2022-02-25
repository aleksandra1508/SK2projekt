#ifndef KING_HPP
#define KING_HPP
#include "position.h"
#include "figure.hpp"
#include <iostream>
#include <string.h>
#include <vector>

using namespace std;


class King : public Figure {
public:
    King(bool black, Position start) {
        colorBlack = black;
        stand = start;
    };
    string getNameFigure() ;
    vector<string> findPossibleMoves(int tab[][9]) ;
};


#endif