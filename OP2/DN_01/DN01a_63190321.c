#include <stdio.h>

int main (){

    int a = 0;
    int b = 0;

    void printme(int);

    char in = 0;
    int mod1 = 1;
    int mod2 = 1;

    in = getchar();
    if (in == '-'){
        mod1 = -1;
        in = getchar();
    }
    while(in != ' '){  // read for a
        a = a*10 + (in -'0');
        in = getchar();
    }

    in = getchar();
    if (in == '-'){
        mod2 = -1;
        in = getchar();
    }

    while(in != ' ' && in != '\n'){  // read for b
        b = b*10 + (in -'0');
        in = getchar();
    }

    printme(a*mod1 + b*mod2);
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