#include <stdio.h>
#include <stdlib.h>

int includes (int* arr, int i){
    for(int j = 0; j < sizeof(arr); j ++){
        if(arr[j] == i){
            return 1;
        }
    }
    return 0;
}

int recur (int* used, int n, int a, int b, int curr) {
    // pogledas ce si na zadnjem kamnu
    if (sizeof(used) == n){
        return 1;
    }else{
        // če nisi na zadnjem kamnu poltem pogledaš a si se zataknu
        int stuck = 1;
        int options = 0;
        for(int i = a; a <= b; a++){
            // probas skocit na usak mozn kamn ka je u dosegu
            int kamen = curr + a;
            if( kamen> 0 && kamen < n && !includes(used, kamen)){
                stuck = 1; //nekam smo lahk skočil tko da je gucci
                //updejtas kere kamne si ze porabu (sepravi dodas curr +a -tega)
                int *temparr  = used;
                for(int i=0; i < n; i++){
                    if(temparr[i] == -1){
                        temparr[i] = kamen
                    }
                }
                options += recur(temparr, n, a, b, kamen)
            }
        }
        // ponoviš samo da daš minus...to lahko bl smooth nardis kokr da copy pastas pou kode ma se mi mudi
        for(int i = a; a <= b; a++){
            // probas skocit na usak mozn kamn ka je u dosegu
            int kamen = curr - a
            if( kamen> 0 && kamen < n && !includes(used, kamen)){
                stuck = 1; //nekam smo lahk skočil tko da je gucci
                //updejtas kere kamne si ze porabu (sepravi dodas curr +a -tega)
                int *temparr  = used;
                for(int i=0; i < n; i++){
                    if(temparr[i] == -1){
                        temparr[i] = kamen
                    }
                }
                options += recur(temparr, n, a, b, kamen)
            }
        }

        if(stuck){
            return 0;
        }else{
            return options;
        }
        
    }
}

int main () {
    int n, a, b;
    scanf("%d",&n);
    scanf("%d",&a);
    scanf("%d",&b);

    int* used = calloc(n, sizeof(int))
    for(int i=0; i < n; i++){
        used[i] = -1;
    }

    printf("%d",recur(used, n, a, b, 0));
}