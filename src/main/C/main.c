#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include "def.h"

#define nbEtapes 3
#define nbGuichets 0
#define nbClients 6

int main(int argc, char ** argv) {
    int tabJetonsGuichet[nbGuichets];
    int * resultat = start_simulation(nbEtapes, nbGuichets, nbClients, tabJetonsGuichet);
    printf("les clients : ");
    for(int i=0; i<nbClients; i++)
    {
        printf("%d ", resultat[i]);
    }
    printf("\n");

    int * where_clients = ou_sont_les_clients(nbEtapes, nbClients);
    do {
       int * where_clients = ou_sont_les_clients(nbEtapes, nbClients);
       printf("%d\n", where_clients[(nbClients+1) * (nbEtapes - 1)]);
       printf("%b\n", where_clients[(nbClients+1) * (nbEtapes - 1)] != nbClients);
       for(int i=1; i<=nbClients*nbEtapes; i=i+nbClients+1) {
          printf("étape %d : %d clients : ", i / nbClients, where_clients[i-1]);
          for(int j=i; j < i+where_clients[i-1]; j++) {
               printf("%d ", where_clients[j]);
          }
          printf("\n");
       }
       sleep(3);
       if (where_clients[(nbClients+1) * (nbEtapes - 1)] == nbClients) break;
    } while(where_clients[(nbClients+1) * (nbEtapes - 1)] != nbClients);  // why doesn't work??
    nettoyage();
    return 0;
}