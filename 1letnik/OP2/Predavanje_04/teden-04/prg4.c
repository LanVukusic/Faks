#include <stdio.h>

void f (int n, int *m)
{
    printf ("n=%d m=%d\n", n, *m);
    n = 2 * n; *m = 2 * *m;
    printf ("n=%d m=%d\n", n, *m);
}

int main ()
{
    int a=5;
    int b=7;

    printf ("%d\n", a);
    scanf ("%d", &a);
    printf ("%d\n", a);

    printf ("a=%d b=%d\n", a, b);
    f (a, &b);
    printf ("a=%d b=%d\n", a, b);
    return 0;
}