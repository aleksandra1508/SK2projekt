#ifndef FIGURE_HPP
#define FIGURE_HPP
#include <iostream>
#include <string.h>
#include <vector>
#include "position.h"


class Figure {
public:
    bool colorBlack;
    Position stand;
    int countOfMoves = 0;
    void doneMoves(Position newStand);
    virtual std::vector <std::string> findPossibleMoves(int chessboard[][9]) = 0;
    virtual std::string getNameFigure() = 0;
};


#endif