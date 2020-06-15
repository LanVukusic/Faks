// Napišite funkcijo
// Vozlisce* odstrani(Vozlisce* osnova, Vozlisce* indeksi)
// ki iz nepraznega povezanega seznama z začetnim vozliščem na naslovu osnova odstrani
// elemente na indeksih, podanih v seznamu z začetnim vozliščem na naslovu indeksi, in
// vrne kazalec na začetno vozlišče osiromašenega seznama. Seznam indeksov je neprazen
// in naraščajoče urejen ter ne vsebuje podvojitev. Vsi indeksi so veljavni (tj. med 0 in
// številom elementov izhodiščnega seznama minus 1).

#include <stdio.h>
#include <stdlib.h>

typedef struct _Vozlisce{
    int podatek;
    struct _Vozlisce *naslednje; // kazalec na naslednika ( NULL, če ga ni)
} Vozlisce;

Vozlisce *odstrani(Vozlisce *osnova, Vozlisce *indeksi){
    Vozlisce *next = indeksi;

    do{
        int index = next->podatek;

        // če umikamo zadnjega v vrsti
        if ((osnova + index)->naslednje == NULL){
            (osnova + index - 1)->naslednje = NULL;
        }
        if(index == 0){  // če umikamo vodilni element
            // nastavimo samo da prvotni kazalec kaže na drugi element
            // osnova  = (osnova + 1);  // aka povečamo pointer na naslednji element
            osnova ++;
        }else{  // čene rečemo da prejšnji pointer izpusti tega ki ga brišemo in kaže na naslednjega
            (osnova + index - 1)->naslednje = (osnova + index + 1);
        }

        // nastavi naslednje vozlišče
        next = (next)->naslednje;

    } while (next != NULL); // delaj dokler ne prideš do konca
}

int main(){

    return 0;
}