#ifndef ROOK_HPP
#define ROOK_HPP
#include "position.h"
#include "figure.hpp"
#include <iostream>
#include <string.h>
#include <vector>

using namespace std;


class Rook : public Figure {
public:
    Rook(bool black, Position start) {
        colorBlack = black;
        stand = start;
    };
    string getNameFigure() ;
    vector<string> findPossibleMoves(int arr[][9]) ;
};


#endif