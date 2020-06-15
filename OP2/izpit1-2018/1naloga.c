// Napišite program, ki prebere število n in besedilo dolžine n znakov, izpiše pa enako
// besedilo, le da vsako besedo zapiše z veliko začetnico.
// Vhod:
// Vhod je sestavljen iz ene same vrstice, ta pa vsebuje celo število n P r1, 1000s, presledek
// in zaporedje n črk angleške abecede in podčrtajev. Besedilo se prične in konča s črko,
// besede pa so med seboj ločene z enim ali več podčrtaji.
// Izhod:
// Izpišite popravljeno kopijo vhodnega besedila.
// Testni primer J1 (vhod/izhod):
// 36 o___Vrba__srecna_draGA___VAS__dOmACa
// O___Vrba__Srecna_DraGA___VAS__DOmACa

#include <stdio.h>
#include <stdlib.h>

char toupper2(char a){
    if (a >= 'a'&& 'z'>= a){  // je legit majhna crka
        return (a + ('A'-'a'));
    }else{
        return a;
    }
}

int main () {
    int len = 0;
    scanf("%i ", &len);

    int uperfy = 1;

    for (int i = 0; i < len; i++){
        char lol = getchar();
        if(uperfy){
            putchar(toupper2(lol));
            
        }else{
            putchar(lol); 
        }

       
        if (lol == ' '){
            uperfy = 1;
        }else{
            uperfy = 0;
        }
        
    }
    putchar('\n');
    return 0 ;
}