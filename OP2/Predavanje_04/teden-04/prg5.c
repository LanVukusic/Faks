#include <stdio.h>

int nlow = -1000;
int a[1000];
int nupp = +1000;

int main ()
{
    printf ("@(%p) %d\n", &nlow, nlow);
    for (int i = 0; i < 5; i++) {
        a[i] = 2 * i;
        printf ("@(%p) %d\n", &(a[i]), a[i]);
    }
    printf ("@(%p) %d\n", &nupp, nupp);

    int *p = &(a[0]);
    *p = 3;
    for (int i = 0; i < 5; i++) printf ("%d ", a[i]); printf ("\n");

    for (int i = 0; i < 5; i++) {
        *p = *p + 1;
        p++; // pove"caj p za sizeof (int)
    }
    for (int i = 0; i < 5; i++) printf ("%d ", a[i]); printf ("\n");

    p = &(a[0]);
        for (int i = 0; i < 5; i++) {
        p[i] = p[i] + 1;
    }
    for (int i = 0; i < 5; i++) printf ("%d ", a[i]); printf ("\n");

    p = &(a[2]);
    for (int i = 0; i < 5; i++) {
        p[i] = p[i] + 1;
    }
    for (int i = 0; i < 5; i++) printf ("%d ", a[i]); printf ("\n");

    /*

a         : a[0] a[1] a[2] a[3] a[4] a[5] ... a[999]
p=&(a[0]) : p[0] p[1] p[2] ...
p=&(a[2]) :           p[0] p[1] p[2] ...

    */

    p = &(a[2]);
    p[-1] = -1;
    p[-2] = -2;
    for (int i = 0; i < 5; i++) {
        p[i] = p[i] + 1;
    }
    for (int i = 0; i < 5; i++) printf ("%d ", a[i]); printf ("\n");
    printf ("nlow=%d nupp=%d\n", nlow, nupp);
    p[-3] = -3; // zunaj tabele !!! NE DELAJ TEGA
    p[-4] = -4; // zunaj tabele !!! NE DELAJ TEGA
    for (int i = 0; i < 5; i++) printf ("%d ", a[i]); printf ("\n");
    printf ("nlow=%d nupp=%d\n", nlow, nupp);

    unsigned long len;
    len = (unsigned long)sizeof (a);
    printf ("%lu\n", len / sizeof (int));

    return 0;
}