#include <stdio.h>
#include <stdlib.h>
#include "def.h"

#define nbEtapes 4
#define nbGuichets 0
#define nbClients 2

int main(int argc, char ** argv) {
    int tabJetonsGuichet[nbGuichets];
    int * resultat = start_simulation(nbEtapes, nbGuichets, nbClients, tabJetonsGuichet);
    nettoyage();
    return 0;
}