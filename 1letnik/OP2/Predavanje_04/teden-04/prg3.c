#include <stdio.h>
#include <stdlib.h>

int main ()
{
    int *ptr1;
    int *ptr2;

    ptr1 = NULL;
    ptr2 = NULL;
    printf ("ptr1 = %p\n", ptr1);
    printf ("ptr2 = %p\n", ptr2);

    ptr1 = (int*)malloc(sizeof (int));
    if (ptr1 == NULL) { /* NI VE"C DOVOLJ POMNILNIKA */ }
    printf ("ptr1 = %p\n", ptr1);
    printf ("ptr2 = %p\n", ptr2);
    printf ("*ptr1 = %d\n", *ptr1);
    *ptr1 = 4;
    printf ("*ptr1 = %d\n", *ptr1);
    ptr2 = ptr1;
    printf ("ptr1 = %p\n", ptr1);
    printf ("ptr2 = %p\n", ptr2);
    printf ("*ptr1 = %d\n", *ptr1);
    printf ("*ptr2 = %d\n", *ptr2);
    *ptr2 = 5;
    printf ("*ptr1 = %d\n", *ptr1);
    printf ("*ptr2 = %d\n", *ptr2);

    free (ptr1);

    return 0;
}