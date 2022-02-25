#ifndef PAWN_HPP
#define PAWN_HPP
#include "position.h"
#include "figure.hpp"
#include <iostream>
#include <string.h>
#include <vector>

using namespace std;


class Pawn : public Figure{
public:
    Pawn(bool black, Position start) {
        colorBlack = black;
        stand = start;
    };
    string getNameFigure() ;
    vector<string> findPossibleMoves(int arr[][9]) ;
};


#endif