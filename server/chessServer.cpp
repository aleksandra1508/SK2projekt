#include <sys/types.h>
#include <sys/wait.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <netdb.h>
#include <signal.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <pthread.h>
#include <iostream>
#include <vector>
#include <queue>
#include "position.h"
#include "figure.hpp"
#include "pawn.hpp"
#include "function.hpp"
#include "bishop.hpp"
#include "rook.hpp"
#include "knight.hpp"
#include "king.hpp"
#include "queen.hpp"


#define QUEUE_SIZE 5
#define BUFF_SIZE 5
#define LOGIN_SIZE 8
#define DESCRIPTION_ARRAY_SIZE 100
pthread_mutex_t queue_mutex = PTHREAD_MUTEX_INITIALIZER;
pthread_cond_t at_least_two_players = PTHREAD_COND_INITIALIZER;

using namespace std;

//struct with data transfered to thread
struct data_thread_join {
    queue<int> &players_queue;
    data_thread_join(queue<int> &players) : players_queue(players) {}
};


struct data_thread_game {
    int first_socket_descriptor;
    int second_socket_descriptor;
};


void release(data_thread_game *th_data, char *login_1, char *login_2, char *login_request){
    write(th_data->second_socket_descriptor, "INFOR",BUFF_SIZE);
    write(th_data->second_socket_descriptor, "ENDDD",BUFF_SIZE);
    printf("[%d-%d] Koniec gry\n", th_data->first_socket_descriptor,th_data->second_socket_descriptor);
    free(login_request);
    free(th_data);
    free(login_1);
    free(login_2);
    close(th_data->first_socket_descriptor);
    close(th_data->second_socket_descriptor);
    pthread_exit(NULL);
}
void release2(data_thread_game *th_data, char *p1, char *p2){
    printf("[%d-%d] Koniec gry\n", th_data->first_socket_descriptor,th_data->second_socket_descriptor);
    free(p1);
    free(p2);
    free(th_data);
    close(th_data->first_socket_descriptor);
    close(th_data->second_socket_descriptor);
    pthread_exit(NULL);

}


void move_of_player(vector<Figure *> &whiteFigures, vector<Figure *> &blackFigures, bool actuallPlayerIsWhite, int &player, int &enemy, 
        bool &gameIsRunning, int &flag, data_thread_game *th_data, int chessboard[9][9], string end_s, char *buffor, Position start, Position end, bool &first_is_dead, bool &second_is_dead, 
        Position whiteKing, Position blackKing){
    // find figure from start position
    Figure *choosenFigure;
    bool choosen = false;
    int bytes_write = 0;
    int result_write = 0;
    for (vector<Figure *>::iterator it = whiteFigures.begin(); it != whiteFigures.end(); ++it) {
        if ((*it)->stand.k == start.k && (*it)->stand.w == start.w) {
            choosen = true;
            choosenFigure = *it;
            break;
        }
    }
    // start position have to be correct
    if (!choosen) {
        char * error = (char *) malloc(sizeof(char) * BUFF_SIZE);
        strcpy(error,"ErorS");
        bytes_write = 0;
        writeInfo(player, error,result_write);
        free(error);
        // we can't continue
        if (result_write < 1) {
            gameIsRunning = false;
            first_is_dead = true;
            flag = 1;
            return;
        }
    } else {
        // making list of posibile moves for figure
        vector<string> possibleThanksToFigure = choosenFigure->findPossibleMoves(chessboard);
        // substruct list of moves which uncover king 
        // if figure is a king that change function
        bool kingSelected = false;
        if (choosenFigure->getNameFigure().compare("king") == 0) kingSelected = true;
        vector<string> vectorek = posibleMovesWithoutCheck(choosenFigure->stand,
                                                                    possibleThanksToFigure, kingSelected,
                                                                    chessboard, whiteKing, blackFigures);
        // comaper if player move is in posibble
        bool correctMove = false;
        for (vector<string>::iterator choos = vectorek.begin(); choos != vectorek.end(); ++choos) {
            if (end_s.compare(*choos) == 0) {
                correctMove = true;
                break;
            }
        }
        //if not in possible, incorect end possition
        if (!correctMove) {
            char * error = (char *) malloc(sizeof(char) * BUFF_SIZE);
            strcpy(error,"ErrorE");
            bytes_write = 0;
            writeInfo(player,error,result_write);
            free(error);
            // we can't continue
            if (result_write < 1) {
                gameIsRunning = false;
                first_is_dead = true;
                flag = 1;
                return;
            }
        } else {
            //if correct and there is enemy figure delete it
            for (vector<Figure *>::iterator enemy = blackFigures.begin();
                    enemy != blackFigures.end(); ++enemy) {
                if (end_s.compare(codePosition((*enemy)->stand.k, (*enemy)->stand.w)) == 0) {
                    blackFigures.erase(enemy);
                    break;
                }
            }
            //update actual possition figure
            choosenFigure->doneMoves(end);
            //if it was king update  possition
            if (choosenFigure->getNameFigure().compare("king") == 0) whiteKing = choosenFigure->stand;
            //prepare all for second player move
            chessboard[start.k][start.w] = 0;
            chessboard[end.k][end.w] = 1;
            for (int i = 1; i < 9; i++)
                for (int j = 1; j < 9; j++)
                    chessboard[i][j] = (-1) * chessboard[i][j];
            //scheck if check mat (whether oponent have any possible move)
            bool szach_mat = checkIfCheckmat(blackFigures, whiteFigures, chessboard, blackKing);
            if (szach_mat) {
                //if end of the game, send info to players and end the game
                //if write < 1 however end of the game
                char * info = (char *) malloc(sizeof(char) * BUFF_SIZE);
                strcpy(info,"INFOR");
                bytes_write = 0;
                writeInfo( player, info, result_write);
                bytes_write = 0;
                writeInfo(enemy, info, result_write);
                free(info);

                char * info2 = (char *) malloc(sizeof(char) * BUFF_SIZE);
                
                if (actuallPlayerIsWhite == true){
                    printf("[%d-%d] White wins\n", th_data->first_socket_descriptor,th_data->second_socket_descriptor);
                    strcpy(info2,"WIN:W");
                }else{
                    printf("[%d-%d] Black wins\n", th_data->first_socket_descriptor,th_data->second_socket_descriptor);
                    strcpy(info2,"WIN:B");
                }
                bytes_write = 0;
                writeInfo(player, info2, result_write);
                bytes_write = 0;
                writeInfo(enemy, info2,result_write);
                free(info2);

                bytes_write = 0;
                writeInfo(enemy, buffor,result_write);
                gameIsRunning = false;
            } else {
                //if there isn't mat turn second player
                bytes_write = 0;
                writeInfo(enemy, buffor,result_write);
                // we can't continue
                if (result_write < 1) {
                    gameIsRunning = false;
                    second_is_dead = true;
                    flag = 1;
                    return;
                }
            }
        }
    }

}

//fuction describing thread behavior
void *ThreadBehavior(void *t_data) {
    pthread_detach(pthread_self());
    struct data_thread_game *th_data = (struct data_thread_game *) t_data;
    char *login_1 = (char *) malloc(sizeof(char) * LOGIN_SIZE);
    char *login_2 = (char *) malloc(sizeof(char) * LOGIN_SIZE);
    char *login_request = (char *) malloc(sizeof(char) * BUFF_SIZE);
    strcpy(login_request,"LOGIN");
    int bytes_read = 0;
    int bytes_write = 0;
    int result_read;
    int result_write;
    //waiting for logins players
    bytes_write = 0;

    while (bytes_write < BUFF_SIZE) {
        result_write = write(th_data->first_socket_descriptor, login_request + bytes_write, BUFF_SIZE - bytes_write);
        if (result_write < 1) break;
        bytes_write += result_write;
    }

    if (result_write < 1) {
        release(th_data, login_1, login_2, login_request);
    }
    bytes_read = 0;
    
    while (bytes_read < BUFF_SIZE) {
        result_read = read(th_data->first_socket_descriptor, login_1 + bytes_read, LOGIN_SIZE - bytes_read);
        if (result_read < 1) break;
        bytes_read += result_read;
    }
    if (result_read < 1) {
        release(th_data, login_1, login_2, login_request);
    }
    printf("[%d-%d] Biały login: %s\n", th_data->first_socket_descriptor,th_data->second_socket_descriptor,login_1);
    bytes_write = 0;

    while (bytes_write < BUFF_SIZE) {
        result_write = write(th_data->second_socket_descriptor, login_request + bytes_write, BUFF_SIZE - bytes_write);
        if (result_write < 1) break;
        bytes_write += result_write;
    }
    
    if (result_write < 1) {
        release(th_data, login_1,login_2, login_request);
    }
    bytes_read = 0;
    
    while (bytes_read < BUFF_SIZE) {
        result_read = read(th_data->second_socket_descriptor, login_2 + bytes_read, LOGIN_SIZE - bytes_read);
        if (result_read < 1) break;
        bytes_read += result_read;
    }
    
    if (result_read < 1) {
        release(th_data, login_1, login_2, login_request);
    }
    printf("[%d-%d] Czarny login: %s\n", th_data->first_socket_descriptor,th_data->second_socket_descriptor,login_2);
    free(login_request);
    //send login of oponent
    bytes_write = 0;

    while (bytes_write < LOGIN_SIZE) {
        result_write = write(th_data->first_socket_descriptor, login_2 + bytes_write, LOGIN_SIZE - bytes_write);
        if (result_write < 1) break;
        bytes_write += result_write;
    }
    
    if (result_write < 1) {
        write(th_data->second_socket_descriptor,"INFOLOGIN",LOGIN_SIZE);
        release2(th_data, login_1, login_2);
    }
    bytes_write = 0;
    
    while (bytes_write < LOGIN_SIZE) {
        result_write = write(th_data->second_socket_descriptor, login_1 + bytes_write, LOGIN_SIZE - bytes_write);
        if (result_write < 1) break;
        bytes_write += result_write;
    }
    
    if (result_write < 1) {
        write(th_data->first_socket_descriptor,"INFOLOGIN",LOGIN_SIZE);
        release2(th_data, login_1, login_2);
    }
    free(login_1);
    free(login_2);
    // the initial state of the game
    int chessboard[9][9] = {
            {0, 0, 0, 0, 0, 0, 0, 0,  0},
            {0, 1, 1, 0, 0, 0, 0, -1, -1},
            {0, 1, 1, 0, 0, 0, 0, -1, -1},
            {0, 1, 1, 0, 0, 0, 0, -1, -1},
            {0, 1, 1, 0, 0, 0, 0, -1, -1},
            {0, 1, 1, 0, 0, 0, 0, -1, -1},
            {0, 1, 1, 0, 0, 0, 0, -1, -1},
            {0, 1, 1, 0, 0, 0, 0, -1, -1},
            {0, 1, 1, 0, 0, 0, 0, -1, -1},
    };
    vector<Figure *> whiteFigures;
    vector<Figure *> blackFigures;
    Position whiteKing = decodePosition("E1");
    Position blackKing = decodePosition("E8");
    // preparing figures for the game in positions
    prepareFigure(blackFigures, whiteFigures);
    //send info about color to players
    //Begining of the game - waiting for white move
    char *c_white = (char *) malloc(sizeof(char) * BUFF_SIZE);
    strcpy(c_white,"white");
    char *c_black = (char *) malloc(sizeof(char) * BUFF_SIZE);
    strcpy(c_black,"black");
    bytes_write = 0;

    while (bytes_write < BUFF_SIZE) {
        result_write = write(th_data->first_socket_descriptor, c_white + bytes_write, BUFF_SIZE - bytes_write);
        if (result_write < 1) break;
        bytes_write += result_write;
    }

    if (result_write < 1) {
        write(th_data->second_socket_descriptor,"INFOR",BUFF_SIZE);
        write(th_data->second_socket_descriptor,"ENDDD",BUFF_SIZE);
        release2(th_data, c_white, c_black);
    }
    bytes_write = 0;
    
    while (bytes_write < BUFF_SIZE) {
        result_write = write(th_data->second_socket_descriptor, c_black + bytes_write, BUFF_SIZE - bytes_write);
        if (result_write < 1) break;
        bytes_write += result_write;
    }
    
    if (result_write < 1) {
        write(th_data->first_socket_descriptor,"INFOR",BUFF_SIZE);
        write(th_data->first_socket_descriptor,"ENDDD",BUFF_SIZE);
        release2(th_data, c_white, c_black);
    }
    free(c_white);
    free(c_black);

    //Start game
    bool actuallPlayerIsWhite = true;
    bool gameIsRunning = true;
    bool first_is_dead = false;
    bool second_is_dead = false;
    char *buffor = (char *) malloc(sizeof(char) * BUFF_SIZE);

    while (gameIsRunning) {
        //write descript to players
        //first_socket_descriptor white player
        int player;
        int enemy;
        if (actuallPlayerIsWhite) {
            player = th_data->first_socket_descriptor;
            enemy = th_data->second_socket_descriptor;
        } else {
            enemy = th_data->first_socket_descriptor;
            player = th_data->second_socket_descriptor;
        }
        // read move
        bytes_read = 0;
        while (bytes_read < BUFF_SIZE) {
            result_read = read(player, buffor + bytes_read, BUFF_SIZE - bytes_read);
            if (result_read < 1) break;
            bytes_read += result_read;
        }
        // we can't continue
        if (result_read < 1) {
            gameIsRunning = false;
            if (actuallPlayerIsWhite)
                first_is_dead = true;
            else second_is_dead = true;
            break;
        }
        //checking mess if is correct 
        //exp. A2:E3
        bool correct = checkIfCorrect(buffor);
        if (!correct) {
            //waiting for correct move
            char * error = (char *) malloc(sizeof(char) * BUFF_SIZE);
            strcpy(error,"ErorM");
            bytes_write = 0;
            writeInfo(player, error,result_write);
            free(error);
            // we can't continue
            if (result_write < 1) {
                gameIsRunning = false;
                if (actuallPlayerIsWhite)
                    first_is_dead = true;
                else second_is_dead = true;
                break;
            }
        } else {
            // asign start and ending of move
            string start_s = string() + buffor[0] + buffor[1];
            string end_s = string() + buffor[3] + buffor[4];
            Position start = decodePosition(start_s);
            Position end = decodePosition(end_s);
            Figure *choosenFigure;
            bool choosen = false;
            //if white make a move
            if (actuallPlayerIsWhite) {
                int flag = 0;
                move_of_player(whiteFigures, blackFigures, actuallPlayerIsWhite, player, enemy, gameIsRunning, 
                    flag, th_data, chessboard, end_s, buffor, start, 
                    end, first_is_dead, second_is_dead, whiteKing, blackKing);
                if (flag == 1){
                    break;
                }
                actuallPlayerIsWhite = false;
            //if black make move
            } else {
                int flag = 0;
                move_of_player(blackFigures, whiteFigures, actuallPlayerIsWhite, player, enemy, gameIsRunning, 
                    flag, th_data, chessboard, end_s, buffor, start, 
                    end, first_is_dead, second_is_dead, blackKing, whiteKing);
                if (flag == 1){
                    break;
                }
                actuallPlayerIsWhite = true;
            }        
        }
    }
    printf("[%d-%d] Koniec gry1\n", th_data->first_socket_descriptor,th_data->second_socket_descriptor);
    if (first_is_dead || second_is_dead) {
        char * info = (char *) malloc(sizeof(char) * BUFF_SIZE);
        strcpy(info,"INFOR");
        bytes_write = 0;
        while (bytes_write < BUFF_SIZE) {
            if (first_is_dead)
                result_write = write(th_data->second_socket_descriptor, info + bytes_write, BUFF_SIZE - bytes_write);
            else
                result_write = write(th_data->first_socket_descriptor, info + bytes_write, BUFF_SIZE - bytes_write);
            if (result_write < 1) break;
            bytes_write += result_write;
        }
        info = (char *) malloc(sizeof(char) * BUFF_SIZE);
        strcpy(info,"ENDDD");
        bytes_write = 0;
        while (bytes_write < BUFF_SIZE) {
            if (first_is_dead)
                result_write = write(th_data->second_socket_descriptor, info + bytes_write, BUFF_SIZE - bytes_write);
            else
                result_write = write(th_data->first_socket_descriptor, info + bytes_write, BUFF_SIZE - bytes_write);
            if (result_write < 1) break;
            bytes_write += result_write;
        }
        free(info);
    }
    free(buffor);
    free(th_data);
    close(th_data->first_socket_descriptor);
    close(th_data->second_socket_descriptor);
    pthread_exit(NULL);
}


//thread - join players in rooms
void *ThreadJoin(void *t_data) {
    pthread_detach(pthread_self());
    struct data_thread_join *th_data = (struct data_thread_join *) t_data;
    while (1) {
        //if is there less than 2 players wait for your turn
        pthread_mutex_lock(&queue_mutex);
        while (th_data->players_queue.size() < 2) {
            pthread_cond_wait(&at_least_two_players,&queue_mutex);
        }
        //new thread - care about game
        pthread_t thread1;
        //data transfered to thread
        struct data_thread_game *game_data = (struct data_thread_game *) malloc(sizeof(struct data_thread_game));
        //data is descriptor of players
        game_data->first_socket_descriptor = th_data->players_queue.front();
        th_data->players_queue.pop();
        game_data->second_socket_descriptor = th_data->players_queue.front();
        th_data->players_queue.pop();
        pthread_mutex_unlock(&queue_mutex);
        cout<<"["<<game_data->first_socket_descriptor<<"-"<<game_data->second_socket_descriptor<<"]  Nowa gra"<<endl;
        int create_result = 0;
        create_result = pthread_create(&thread1, NULL, ThreadBehavior, (void *) game_data);
        if (create_result) {
            printf("Błąd przy próbie utworzenia wątku, kod błędu: %d\n", create_result);
        }
    }
}


//thread - conect players in pair and create new game
void createThreadJoin(queue<int> &new_players_sockets) {
    //result of creating thread 
    int create_result = 0;
    pthread_t thread1;
    //data transfered to thread
    //player whose waiting on game
    struct data_thread_join *t_data = new data_thread_join(new_players_sockets);
    create_result = pthread_create(&thread1, NULL, ThreadJoin, (void *) t_data);
    if (create_result) {
        printf("Błąd przy próbie utworzenia wątku, kod błędu: %d\n", create_result);
        exit(-1);
    }
}


int main(int argc, char *argv[]) {
    if (argc != 2)
    {
        fprintf(stderr, "Sposób uruchomienia: %s numer_portu\n", argv[0]);
        exit(1);
    }
    queue <int> new_players_sockets;
    int server_socket_descriptor;
    int connection_socket_descriptor;
    int bind_result;
    int listen_result;
    char reuse_addr_val = 1;
    struct sockaddr_in server_address;
    
    //init of socket server
    memset(&server_address, 0, sizeof(struct sockaddr));
    server_address.sin_family = AF_INET;
    server_address.sin_addr.s_addr = htonl(INADDR_ANY);
    server_address.sin_port = htons(atoi(argv[1]));
    server_socket_descriptor = socket(AF_INET, SOCK_STREAM, 0);
    if (server_socket_descriptor < 0) {
        fprintf(stderr, "%s: Błąd przy próbie utworzenia gniazda..\n", argv[0]);
        exit(1);
    }
    setsockopt(server_socket_descriptor, SOL_SOCKET, SO_REUSEADDR, (char *) &reuse_addr_val, sizeof(reuse_addr_val));
    printf("Socket serwera: %d\n", server_socket_descriptor);
    
    //bind attempt 
    bind_result = bind(server_socket_descriptor, (struct sockaddr *) &server_address, sizeof(struct sockaddr));
    if (bind_result < 0) {
        fprintf(stderr, "%s: Błąd przy próbie dowiązania adresu IP i numeru portu do gniazda.\n", argv[0]);
        exit(1);
    }
    
    // listening setting
    listen_result = listen(server_socket_descriptor, QUEUE_SIZE);
    if (listen_result < 0) {
        fprintf(stderr, "%s: Błąd przy próbie ustawienia wielkości kolejki.\n", argv[0]);
        exit(1);
    }
    
    createThreadJoin(new_players_sockets);
    while (1) {
        //add new players to queue
        connection_socket_descriptor = accept(server_socket_descriptor, NULL, NULL);
        if (connection_socket_descriptor < 0) {
            fprintf(stderr, "%s: Błąd przy próbie utworzenia gniazda dla połączenia.\n", argv[0]);
        } else {
            printf("Nastąpiło połączenie na sockecie: %d\n", connection_socket_descriptor);
            pthread_mutex_lock(&queue_mutex);
            new_players_sockets.push(connection_socket_descriptor);
            //if there is more than 2 player send mess 
            if (new_players_sockets.size() >= 2) pthread_cond_signal(&at_least_two_players);
            pthread_mutex_unlock(&queue_mutex);
        }
    }
}