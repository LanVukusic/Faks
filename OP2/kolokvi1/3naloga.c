#include <stdio.h>

int ma4del (int n){
    int counts = 0;
    for(int i = 2; i < n; i++){
        if (n % i == 0){
            //kul mamo delitelja
            counts ++;
            if (counts > 4)
            {
                return 0;
            }
        }
    }
    if(counts == 4){
        return 1;
    }
    return 0;
}


int main () {
    int num = 0;
    int* nump = &num;
    int top[3] = {0};

    do{
        scanf("%d", nump);

        if(ma4del(num)){
            printf("legit: %d", num);
            if(num >= top[2]){
                if(num >= top[1]){
                    if(num >= top[0]){
                        top[2] = top[1];
                        top[1] = top[0];
                        top[0] = num; 

                    }else{
                        top[2] = top[1];
                        top[1] = num; 
                    }
                }else{
                    top[2] = num;
                }

            }
        }
        
        // pol dzabe je majhna cifra
    } while (num != 0);
    
    printf("%d", top[2]);
}