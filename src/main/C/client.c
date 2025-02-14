#include <stdio.h>
#include <stdlib.h>
#include "def.h"

#define SASENTREE 0
#define GUICHET1 1
#define ACTIVITE1 2
#define GUICHET2 3
#define ACTIVITE2 4
#define GUICHET3 5
#define ACTIVITE_RES 6
#define SASSORTIE 7

void simulation(int ids) {
    entrer(SASENTREE);
    delai(6, 3);
    transfert(SASENTREE,GUICHET1);
    sleep(1);
    P(ids, 1);
    transfert(GUICHET1, ACTIVITE1);
    delai(3, 1);
    V(ids, 1);
    transfert(ACTIVITE1,GUICHET2);
    sleep(2);
    P(ids, 2);
    transfert(GUICHET2, ACTIVITE2);
    delai(5, 3);
    V(ids, 2);
    transfert(ACTIVITE2, GUICHET3);
    sleep(2);
    P(ids, 3);
    transfert(GUICHET3, ACTIVITE_RES);
    delai(5, 3);
    V(ids, 3);
    transfert(ACTIVITE_RES, SASSORTIE);
}

// export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:.