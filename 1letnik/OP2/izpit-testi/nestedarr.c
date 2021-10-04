# include <stdio.h>
# include <stdlib.h>

int main () {
    char ** arr = malloc (2 * 20 * sizeof(char));

    for (int i = 0; i < 2; i++){
        printf(" . ");
        char * ljna = calloc(20, sizeof(char));
        do{
            // scanf("%c",&a);
            *ljna = getchar();
            ljna ++;
        } while (*(ljna - 1) != '\n');
        arr[i] = ljna; 
    }
    for(int i = 0; i<2; i++){
        printf("%c", *arr[i]);
    }

    

    return (1);

}