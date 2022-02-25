#include "figure.hpp" 
#include <string.h>
#include <vector>
#include <iostream>

using namespace std;


void Figure :: doneMoves(Position newStand) {
        countOfMoves++;
        stand = newStand;
    };