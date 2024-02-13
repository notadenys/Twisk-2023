#include <stdio.h>
#include <stdlib.h>
#include "def.h"

#define SASENTREE 0
#define ACTIVITE 1
#define SASSORTIE 2

void simulation(int ids) {
    entrer(SASENTREE);
    delai(6, 3);
    transfert(SASENTREE, ACTIVITE);
    delai(3, 1);
    transfert(ACTIVITE, SASSORTIE);
}
