#include <stdio.h>
#include <stdlib.h>
#include "def.h"

#define SASENTREE 0
#define ACTIVITE 2
#define GUICHET 1
#define SASSORTIE 3

void simulation(int ids) {
    entrer(SASENTREE);
    delai(6, 3);
    transfert(SASENTREE,GUICHET);
    P(ids, 1);
    transfert(GUICHET, ACTIVITE);
    delai(3, 1);
    V(ids, 1);
    transfert(ACTIVITE, SASSORTIE);
}
