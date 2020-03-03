#include <stdio.h>

int main (){

    int a = 0;
    int b = 0;

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

    printf("%d\n", mod1 * a + mod2 * b);

    return 0;
}