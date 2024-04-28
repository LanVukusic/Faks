#include <stdio.h>
#include <stdlib.h>

typedef struct _Vozlisce { // vozlišče povezanega seznama
    int podatek; // podatek v vozlišču
    struct _Vozlisce* n; // kazalec na naslednika ( NULL, če ga ni)
    struct _Vozlisce* nn; // kazalec na naslednika naslednika ( NULL, če ga ni)
} Vozlisce;


Vozlisce* vstaviUrejeno(Vozlisce* zacetek, int element){
    rek(NULL, zacetek, element);
}

void rek (Vozlisce* gparent, Vozlisce* parent, int element){
    // precekeri ce si na pravem mestu;
    if(parent->n->podatek > element){
        //pol smo tu :D
        Vozlisce* temp;
        temp ->podatek = element;
        temp ->n = parent->n;
        temp ->nn = parent->nn;

        //sredimo tata
        parent ->n = temp;

        //in nonča
        if(gparent != NULL){
            gparent -> nn = temp;
        }
        return;
        
    }else{
        rek(parent, parent->n, element);
        return;
    }

}


int main (){

    return 1;
}