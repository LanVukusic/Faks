#include <stdio.h>

int main (){
    char a = getchar();
    char b = getchar();
    printf("%d",((a + b - 48*2) % 10));
    putchar('\n');
    return 0;
}