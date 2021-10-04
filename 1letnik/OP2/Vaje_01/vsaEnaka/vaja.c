#include <stdio.h>

int main (){
    int a = getchar() - 48;
    getchar();  // spustimo kr to je presledek

    int sample = getchar();
    getchar();

    for (int i = 0; i < a*2 -2; i++)
    {
        if(i % 2 == 0){  // vsak drug je presledek
            if(sample != getchar()){
                putchar('0');
                putchar('\n');
                return 0;
            }
            
        }else{
            getchar();
        }
        
    }
    putchar('1');
    putchar('\n');
    return 0;
    
}