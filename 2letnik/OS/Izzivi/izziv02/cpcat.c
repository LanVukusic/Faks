#include<stdio.h>
#include<fcntl.h>
#include<errno.h>
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <fcntl.h>
#include <unistd.h>
#include <string.h>

int main(int argc, char const *argv[]){
    /* code */
    int err=0;
    char bufferIn[255];

    
    // handle inputs
    int ind = 0;
    if( !(argv[1] == NULL || !strcmp(argv[1], "-")) ){
        ind = open(argv[1], O_RDONLY);
        if(ind < 0){
            err= errno;
            perror("napaka pri vhodni datoteki");
            exit(err);
        }
    }else{
        //printf("Uporablamo stdin\n");
    }

    
    //handle outputs
    int outd = 1;
    if( argc > 2 && !(argv[2] == NULL || !strcmp(argv[2], "-")) ){
        outd = open(argv[2], O_RDWR);
        if(outd < 0){
            err = errno;
            perror("napaka pri izhodni datoteki");
            exit(err);
        }
    }else{
        //printf("Uporablamo stdout\n");
    }

    // prepisovanje (ne od sošolcev, dont worry)
    char *buffer = (char *)calloc(1024, sizeof(char));
    int readBits;
    while ((readBits = read(ind, buffer, 1024)) > 0){  // čaka na EOF, za stdin sepravi hitni CTRL+D
        buffer[readBits] = '\0'; // C ma te jadne stringe in zato moreš terminirat char array z \0
        //printf("read: %d bits\n", readBits);
        int w = write(outd, buffer, readBits);  // tolko ka si prebrau lahko zdej mirne duše zapišeš še u fajl / stdout

        // w pove kolko bitov smo uspešno zapisal. Če je negativen je err
        if (w < 0){
            err = errno;
            perror("napaka pri pisanju v datoteko\n");
            exit(err);
        }
    }

    if(close(ind) <0){
        err = errno;
        perror("napaka zapiranja vhodne datoteke\n");
        exit(err);
    }

    if(close(outd) <0){
        err = errno;
        perror("napaka zapiranja izhodne datoteke\n");
        exit(err);
    }


    return 0;
}
