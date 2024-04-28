#include <stdio.h>
#include <stdlib.h>

#define SIZE			(1024)

int main(void) 
{
	int i;

	int vectorSize = SIZE;

	// Rezervacija pomnilnika
	int *A = (int*)malloc(vectorSize*sizeof(int));
    int *B = (int*)malloc(vectorSize*sizeof(int));
    int *C = (int*)malloc(vectorSize*sizeof(int));

    // Inicializacija vektorjev
	for(i = 0; i < vectorSize; i++) 
	{
        A[i] = i;
        B[i] = vectorSize - i;
    }

	// Ra"cunanje
    for(i = 0; i < vectorSize; i++)
        C[i] = A[i] + B[i];

    // Prikaz rezultatov
    for(i = 0; i < vectorSize; i++)
        printf("%d + %d = %d\n", A[i], B[i], C[i]);
 
    free(A);
    free(B);
    free(C);

    return 0;
}
