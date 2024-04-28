// Napišite program, ki prebere število n in zaporedje n znakov + in - in izpiše navzdol
// zaokroženo povprečje razdalj med zaporednima znakoma +.
// Vhod:
// Vhod je sestavljen iz ene same vrstice, ta pa vsebuje celo število n P r1, 1000s, presledek
// in zaporedje n znakov + in -. Zaporedje vsebuje najmanj dva znaka +.
// Izhod:
// Izpišite iskano povprečje.
// Testni primer J1 (vhod/izhod):
// 28 ---------+--+-----+----++---
// 3
// Razdalja med prvim in drugim znakom + znaša 3, med drugim in tretjim 6, med tretjim
// in četrtim 5, med četrtim in petim pa 1; iskano povprečje je potemtakem t15 / 4u “ 3.

#include <stdio.h>
#include <stdlib.h>

int main () {
    int num = 0, sum = 0, pad = 0, flag = 0, xs = 0;
    scanf("%i ", &num);

    for (int i = 0; i < num; i++){
        char a = getchar();
        if(a == '+'){
            if (flag){
                sum += (pad +1);
                pad = 0;
                xs++;
            }
            flag = 1;
        }else if( a == '-'){  // sepravi ce je -
            if (flag){
                pad ++;
            }
        }
    }

    printf("rezultat: %i", sum/(pad+1));
    return 1 ;
}