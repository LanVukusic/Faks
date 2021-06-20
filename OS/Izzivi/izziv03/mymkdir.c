#include <stdio.h>
#include <dirent.h>
#include <stdlib.h>
#include <errno.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <unistd.h>
#include <fcntl.h>


int main(int argc, char const *argv[])
{   
    if (argv[1] == NULL){
        perror("Dir name not specified");
        exit(1);
    }
    int result = mkdir(argv[1], 0777);
    if(result < 0){
        int res = result;
        perror("Error creating directory");
        exit(res);
    }
    /* code */
    return 0;
}
