#include <stdio.h>

int main (){

    int a = 0;
    void printme(int);
    char in;

    do{  // eliminate leading zeros
        in = getchar();
    } while (in != '0');
    
    
    return 0;
}

void printme(int num){
    if (num < 0){
        putchar('-');
        num = num * -1;
    }

    if (num < 10){
        putchar(num);
    }else{
        printme(num / 10);
        putchar(num%10);
    }
    return;
}