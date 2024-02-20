#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include "def.h"

#define nbEtapes 8
#define nbGuichets 2
#define nbClients 6

int main(int argc, char ** argv) {
    int tabJetonsGuichet[nbGuichets];
    for(int i=0;i<nbGuichets;i++)
    {
        tabJetonsGuichet[i] = i+3;
    }

    int * resultat = start_simulation(nbEtapes, nbGuichets, nbClients, tabJetonsGuichet);
    printf("les clients : ");
    for(int i=0; i<nbClients; i++)
    {
        printf("%d ", resultat[i]);
    }
    printf("\n");

    int * where_clients = ou_sont_les_clients(nbEtapes, nbClients);
    do {
       where_clients = ou_sont_les_clients(nbEtapes, nbClients);
       printf("\n");
       for(int i=1; i<=(nbClients+1)*nbEtapes; i=i+nbClients+1) {
          printf("étape %d : %d clients : ", i / (nbClients+1), where_clients[i-1]);
          for(int j=i; j < i+where_clients[i-1]; j++) {
               printf("%d ", where_clients[j]);
          }
          printf("\n");
       }
       sleep(3);
    } while(where_clients[(nbClients+1) * (nbEtapes - 1)] != nbClients);
    nettoyage();
    return 0;
}