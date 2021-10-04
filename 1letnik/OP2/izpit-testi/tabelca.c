# include <stdio.h>
# include <stdlib.h>

int main(){
    // preberemo kolk nizov bomo vpisal
    int len = 0;
    scanf("%i ",&len);

    // alociramo tolk placa v spominu da lahk shranmo use to notr
    char *str1 = calloc(len,  sizeof(char));

    // zaloopamo tolkrat in beremo naše črke
    for (int i = 0; i < len; i++){
        // pointer zamikamo za en naslov naprej, ter na to mesto pišemo vrednost
        *(str1 + i) = getchar();
    }

    printf("\nVALS: ");

    // če VEŠ doužino
    // for (int i = 0; i < len; i++){
    //     putchar(*(str1 + i));
    // }

    //če NEVEŠ doužine
    int count = 0;
    // Basically beremo in potem pogledamo če smo že pršli do ničelnega elementa
    // problem je samo da mamo težko "0" v tej tabeli
    do{
       putchar(*(str1 + count));
       count++;
    } while (*(str1 + count) != 0);
    

    // čeprou nismo definiral "klasične" tabele lahko tko dostopaš do elementou
    char lol = str1[2];
} 