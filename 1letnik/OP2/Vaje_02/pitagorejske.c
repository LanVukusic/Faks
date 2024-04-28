#include <stdio.h>

int main (){
    int a = 0;
    int b = 0;

    char in = 0;
    int count = 0;

    in = getchar();

    while(in != ' '){  // read for a
        a = a*10 + (in -'0');
        in = getchar();
    }

    in = getchar();

    while(in != ' '){  // read for b
        b = b*10 + (in -'0');
        in = getchar();
    }

    for (int  i = a; i <= b; i++){
        // ali je to stevilo v trojicah
        for (int x = 0; x <= a; x++){
            for (int y = x; y <= b; y++){
                if (i*i == x*x + y*y){
                    count = count + 1;
                }
            }
            
        }
        
    }

    // printf("%d\n",a+1);
    // printf("%d\n",b+1);
    printf("%d\n",count);
    return 0;
}