#ifndef FUNCTION_HPP
#define FUNCTION_HPP
#include <iostream>
#include "position.h"
#include <string.h>
#include "figure.hpp"
#include <vector>

using namespace std;


Position decodePosition(string stand) ;
string codePosition(int a, int b) ;
bool checkIfCorrect(char *bufor) ;
bool checkIfCheckKingWithMove(int tab[][9], Position potentiallyBeating, Position standKing, vector<Figure *> figuresOponent) ;
vector<string> posibleMovesWithoutCheck(Position standFigure, vector<string> possible, bool ifKing ,int chessboard[][9], Position maybeKing,vector<Figure *> figureOponent);
bool checkIfCheckmat(vector<Figure *> myFigures, vector<Figure *> figuresOponent, int arr[][9], Position King) ;
void prepareWhiteFigures(vector<Figure *> &whiteFigures);
void prepareBlackFigure(vector<Figure *> &blackFigures);
void prepareFigure(vector<Figure *> &blackFigures, vector<Figure *> &whiteFigures);
void writeInfo( int &plOrEn, char * &info, int &result_write);


#endif