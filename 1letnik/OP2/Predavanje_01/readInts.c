#include <stdio.h>
int main(){
    int n = 0;
    int c;
    while (1){
        c = getchar();
        if (('0' <= c) && (c <= '9')){
            n = n * 10 + (c -'0');
            continue;
        }
        if (c == '\n') break;
        
    }
    printf("%d\n",n);
    return 0;
}