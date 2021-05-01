#include <stdio.h>
#include <dirent.h>
#include <stdlib.h>
#include <errno.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <unistd.h>
#include <fcntl.h>

int main(int argc, char *argv[])
{
    struct dirent *pDirent;
    DIR *pDir;
    if(argv[1] != NULL){
        pDir = opendir(argv[1]);
    }else{
        pDir = opendir(".");
    }
    

    if (pDir == NULL) {
        int err = errno;
        perror("Nemorem odpreti direktorija '/etc/shadow'\n");
        exit(err);
    }

    while ((pDirent = readdir(pDir)) != NULL){
        printf("%s\n", pDirent->d_name);
    }

    closedir(pDir);
    return 0;
}