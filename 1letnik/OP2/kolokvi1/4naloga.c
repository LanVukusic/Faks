#include <stdio.h>

typedef struct _heap { int value; struct _heap *left, *right; } heap;


// to je dejansko una naloga ka mors oddat
void sums (char *f, heap *h){
    int summm = h->left->value + h->right->value;
    if(!f){
        printf("%d",summm);
    }else{
        // izpisi na faking fajl
        FILE *fptr;
        fptr = fopen(f,"w");
        fprintf(fptr,"%d",summm);
        fclose(fptr);
    }
}

int main (){

    // test ce dela zapis u fajl brez da delam ceu heap
    FILE *fptr;
    fptr = fopen("testtxt.txt","w");
    fprintf(fptr,"%d\n",2);
    fclose(fptr);
    return 0;
}