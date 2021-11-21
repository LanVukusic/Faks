/*H********************************************************************************
* Ime datoteke: serverLinux.cpp
*
* Opis:
*		Enostaven strenik, ki zmore sprejeti le enega klienta naenkrat.
*		Strenik sprejme klientove podatke in jih v nespremenjeni obliki polje
*		nazaj klientu - odmev.
*
*H*/

//Vkljuèimo ustrezna zaglavja
#include<stdio.h>
#include<sys/socket.h>
#include<netinet/in.h>
#include<unistd.h>
/*
Definiramo vrata (port) na katerem bo strenik poslual
in velikost medponilnika za sprejemanje in poiljanje podatkov
*/
#define PORT 1053
#define BUFFER_SIZE 256
#define MAX_USERS 5

// handling max users
int userCount = 0;

int main(int argc, char **argv){

	//Spremenjlivka za preverjane izhodnega statusa funkcij
	int iResult;

	/*
	Ustvarimo nov vtiè, ki bo poslual
	in sprejemal nove kliente preko TCP/IP protokola
	*/
	int listener=socket(AF_INET, SOCK_STREAM, 0);
	if (listener == -1) {
		printf("Error creating socket\n");
		return 1;
	}

	//Nastavimo vrata in mreni naslov vtièa
	sockaddr_in  listenerConf;
	listenerConf.sin_port=htons(PORT);
	listenerConf.sin_family=AF_INET;
	listenerConf.sin_addr.s_addr=INADDR_ANY;

	//Vtiè poveemo z ustreznimi vrati
	iResult = bind( listener, (sockaddr *)&listenerConf, sizeof(listenerConf));
	if (iResult == -1) {
		printf("Bind failed\n");
		close(listener);
		return 1;
    }

	//Zacnemo poslusati
	if ( listen( listener, 5 ) == -1 ) {
		printf( "Listen failed\n");
		close(listener);
		return 1;
	}

	//Definiramo nov vtic in medpomnilik
	int clientSock;
	char buff[BUFFER_SIZE];
	
	/*
	V zanki sprejemamo nove povezave
	in jih streemo (najveè eno naenkrat)
	*/
	while (1)
	{
		//Sprejmi povezavo in ustvari nov vtiè
        // accept je blocking call
		clientSock = accept(listener,NULL,NULL);
		if (clientSock == -1) {
			printf("Accept failed\n");
			close(listener);
			return 1;
		}

		//Postrezi povezanemu klientu
        
		do{
			//Sprejmi podatke
			iResult = recv(clientSock, buff, BUFFER_SIZE, 0);
			if (iResult > 0) {
				printf("Bytes received: %d\n", iResult);

				//Vrni prejete podatke poiljatelju
				iResult = send(clientSock, buff, iResult, 0 );
				if (iResult == -1) {
					printf("send failed!\n");
					close(clientSock);
					break;
				}
				printf("Bytes sent: %d\n", iResult);
			}
			else if (iResult == 0)
				printf("Connection closing...\n");
			else{
				printf("recv failed!\n");
				close(clientSock);
				break;
			}
		} while (iResult > 0);

		close(clientSock);
        // povezave --
	}

	//Poèistimo vse vtièe
	close(listener);

	return 0;
}