#include <stdio.h>

int main();

int a1;

int f(int i) {
    int b1;
    static int b2;

    if (i == 0) return 0;
    else {
        printf ("i=%d a1 ... %18p\n", i, &a1);
        printf ("i=%d b1 ... %18p\n", i, &b1);
        printf ("i=%d b2 ... %18p\n", i, &b2);
        printf ("i=%d i  ... %18p\n", i, &i);
        printf ("i=%d f  ... %18p\n", i, &f);
        printf ("i=%d m  ... %18p\n", i, &main);
        return f(i-1);
    }
}

int main ()
{
    f(2);

    return 0;
}