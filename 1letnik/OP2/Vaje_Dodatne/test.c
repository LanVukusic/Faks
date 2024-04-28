#include <stdio.h>
int sest = 0;
int* ptr = &sest;

int fib (int k, int n, int* sest){
    if (n<k){
        return 1;
    }else{
        int vsota = 0;
        for(int i = 1; i <= k; i++){
            vsota += fib(k, n-i, sest);
            *sest += 1;
        }
    }
}

int main (){
    fib(4, 20, ptr);
    printf("%d\n", *ptr);
    return (1);
}