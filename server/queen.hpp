#ifndef QUEEN_HPP
#define QUEEN_HPP
#include <iostream>
#include <string.h>
#include <vector>
#include "position.h"
#include "figure.hpp"

using namespace std;


class Queen : public Figure {
public:
    Queen(bool black, Position start) {
        colorBlack = black;
        stand = start;
    };
    string getNameFigure() ;
    vector<string> findPossibleMoves(int arr[][9]) ;
};


#endif