/*H********************************************************************************
* Ime datoteke: serverLinux.cpp
*
* Opis:
*		Enostaven strežnik, ki zmore sprejeti le enega klienta naenkrat.
*		Strežnik sprejme klientove podatke in jih v nespremenjeni obliki pošlje
*		nazaj klientu - odmev.
*
*H*/

//Vkljuèimo ustrezna zaglavja
#include <netinet/in.h>
#include <pthread.h>
#include <stdio.h>
#include <sys/socket.h>
#include <unistd.h>
/*
Definiramo vrata (port) na katerem bo strežnik poslušal
in velikost medponilnika za sprejemanje in pošiljanje podatkov
*/

#define PORT 1234
#define BUFFER_SIZE 256

int connection_count = 0;
#define MAX_CONNS 3
pthread_mutex_t lock = PTHREAD_MUTEX_INITIALIZER;

// thread function
void* myThreadFun(void* vargp)
{
    int iResult;
    int clientSock = *(int*)vargp;
    char buff[BUFFER_SIZE];
    //Postrezi povezanemu klientu
    do {

        //Sprejmi podatke
        iResult = recv(clientSock, buff, BUFFER_SIZE, 0);
        if (iResult > 0) {
            printf("Bytes received: %d\n", iResult);

            //Vrni prejete podatke pošiljatelju
            iResult = send(clientSock, buff, iResult, 0);
            if (iResult == -1) {
                printf("send failed!\n");
                close(clientSock);
                break;
            }
            printf("Bytes sent: %d\n", iResult);
        } else if (iResult == 0)
            printf("Connection closing...\n");
        else {
            printf("recv failed!\n");
            close(clientSock);
            break;
        }
    } while (iResult > 0);
    pthread_mutex_lock(&lock);
    connection_count--;
    printf("client died: %d/%d\n", connection_count, MAX_CONNS);
    pthread_mutex_unlock(&lock);
    close(clientSock);

    return NULL;
}

int main(int argc, char** argv)
{

    //Spremenjlivka za preverjane izhodnega statusa funkcij
    int iResult;

    /*
	Ustvarimo nov vtiè, ki bo poslušal
	in sprejemal nove kliente preko TCP/IP protokola
	*/
    int listener = socket(AF_INET, SOCK_STREAM, 0);
    if (listener == -1) {
        printf("Error creating socket\n");
        return 1;
    }

    //Nastavimo vrata in mrežni naslov vtièa
    sockaddr_in listenerConf;
    listenerConf.sin_port = htons(PORT);
    listenerConf.sin_family = AF_INET;
    listenerConf.sin_addr.s_addr = INADDR_ANY;

    //Vtiè povežemo z ustreznimi vrati
    iResult = bind(listener, (sockaddr*)&listenerConf, sizeof(listenerConf));
    if (iResult == -1) {
        printf("Bind failed\n");
        close(listener);
        return 1;
    }

    //Zaènemo poslušati
    if (listen(listener, 5) == -1) {
        printf("Listen failed\n");
        close(listener);
        return 1;
    } else {
        printf("Listening on: %d\n", PORT);
    }

    //Definiramo nov vtiè in medpomnilik
    int clientSock;

    /*
	V zanki sprejemamo nove povezave
	in jih strežemo (najveè eno naenkrat)
	*/
    while (1) {
        //Sprejmi povezavo in ustvari nov vtich

        clientSock = accept(listener, NULL, NULL);
        if (clientSock == -1) {
            printf("Accept failed\n");
            close(listener);
            return 1;
        }

        // nared nov thread tle
        if (connection_count < MAX_CONNS) {
            pthread_mutex_lock(&lock);
            connection_count++;
            pthread_mutex_unlock(&lock);
            printf("client connected: %d/%d\n", connection_count, MAX_CONNS);
            pthread_t thread_id;
            pthread_create(&thread_id, NULL, myThreadFun, (void*)&clientSock);
        } else {
            close(clientSock);
            printf("queue full\n");
        }
    }

    //Poèistimo vse vtièe
    close(listener);
    return 0;
}
