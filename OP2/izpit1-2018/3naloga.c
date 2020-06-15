// Podani sta celi števili a ě 2 in b ě 2. Funkcija f : N0 Ñ N0 je definirana takole:
// Zapis tru označuje celi del realnega števila r (npr. t2,8u “ 2).
// Napišite program, ki prebere števila a, b in n in izpiše število klicev funkcije f, če
// vrednost fpnq izračunamo strogo po definiciji. Večkratne klice z istim parametrom
// štejemo samo po enkrat.
// Vhod:
// Na vhodu so podana cela števila a P r2, 100s, b P r2, 100s in n P r0, 106
// s, ločena s
// presledkom.
// V testnih primerih J1–J3 in S1–S15 velja a “ b.
// Izhod:
// Izpišite število različnih klicev funkcije.
// Testni primer J4 (vhod/izhod):
// 2 3 10
// 6




//lahko mi ga spuštie usi po vrsti jst tega u lajfu neom delu vč lahko fuknem bmk
// častim pivo če pogruntaš zaki ta kurac od kode enkrat vrže 0. drugič pa 5342234

# include<stdio.h>
# include<stdlib.h>

int isIn (int i, int * arr){
    
    // int offset = 0;
    // do{
    //     if(*(arr + offset) == i){
    //         return 1;
    //     }
    //     offset ++;
    //     //arr ++;
    // } while (*(arr + offset) != 0);
    return 0;
}

void getCalls (int a, int b, int in, int *num, int *scn){
    printf("%d \n", *num);

    if(in == 0){
        return;
    }

    if (! isIn(in, scn)){
        //*(scn + *num) = in;
        *num ++;
        printf("%d \n", *num);
        //getCalls(a,b,in/a, num, scn);
        //getCalls(a,b,in/b, num, scn);
    }else{
        return;
    }
}

int main () {
    int a, b, c;
    scanf("%i %i %i", &a, &b, &c);

    int *scans = calloc(30, sizeof(int));
    int num = 0;

    getCalls(a, b, c, &num, scans);

    /*
    *(scans + 0) = 1;
    *(scans + 1) = 2;
    *(scans + 2) = 3;

    printf("%i\n", isIn(4, scans));
    */

    printf("\nValue:%i\n", (num) +1);
    return 1 ;
}