#include <stdio.h>
#include <stdlib.h>
#include "def.h"

#define nbEtapes 4
#define nbGuichets 0
#define nbClients 6

int main(int argc, char ** argv) {
    int k=0;
    int tabJetonsGuichet[nbGuichets];
    int * resultat = start_simulation(nbEtapes, nbGuichets, nbClients, tabJetonsGuichet);
    printf("les clients : ");
    for(int i=0; i<nbClients; i++)
    {
        printf("%d ", resultat[i]);
    }
    printf("\n");

    int * where_clients = ou_sont_les_clients(nbEtapes, nbClients);
    for(int i=1; i<(nbClients+1)*nbEtapes; i=i+nbClients+1) {
        printf("étape %d : %d clients : ", k, where_clients[i-1]);
        k++;
        for(int j=i; j < i+nbClients; j++) {
           if(where_clients[j] != 0) {
                printf("%d ", where_clients[j]);
           }
        }
        printf("\n");
    }
    nettoyage();
    return 0;
}