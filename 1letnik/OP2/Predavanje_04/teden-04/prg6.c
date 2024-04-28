#include <stdio.h>
#include <stdlib.h>

void f (int *t, int tlen)
{
    for (int i = 0; i < tlen; i++) printf ("%d ", t[i]); printf ("\n");
}

int main ()
{
    //int a[1000]; // <- na skladu
    int *a; // kazalec je na skladu
    a = (int*)malloc (1000 * sizeof (int)); // podatki (inti) so na kopici

    for (int i = 0; i < 1000; i++) a[i] = i;
    f(&(a[0]), 1000);

    free (a);

    return 0;
}