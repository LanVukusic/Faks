// vaja za datoteke. pač da prebereš en fajl
//in pol printaš v drug fajl


# include <stdio.h>
# include <stdlib.h>

int main () {
    char str1[] = "./inn.txt";
    char str2[] = "./out.txt";
    
    FILE *inptr, *outptr;
    inptr = fopen(str1, "r");
    outptr = fopen(str2, "w");

    char c;
    while (fscanf(inptr,"%c", &c) != EOF){
        fprintf(outptr, "%c", c);
    }
    fclose(inptr);
    fclose(outptr);
}