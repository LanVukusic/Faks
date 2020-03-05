#include <stdio.h>

int main (){

    int a = 0;

    char in = 0;
    int mod1 = 1;


    in = getchar();
    if (in == '-'){
        mod1 = -1;
        in = getchar();
    }
    while(in != ' '){  // read for a
        a = a*10 + (in -'0');
        in = getchar();
    }

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
}