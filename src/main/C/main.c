#include <stdio.h>
#include <stdlib.h>
#include "def.h"

#define nbEtapes 4
#define nbGuichets 0
#define nbClients 6

int main(int argc, char ** argv) {
    int tabJetonsGuichet[nbGuichets];
    int * resultat = start_simulation(nbEtapes, nbGuichets, nbClients, tabJetonsGuichet);
    printf("les clients : ");
    for(int i = 0; i < nbClients; i++)
    {
        printf("%d ", resultat[i]);
    }
    printf("\n");
    nettoyage();
    return 0;
}