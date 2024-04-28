#include <stdio.h>
#include <stdlib.h>
#include <math.h>

void rek (int index, int *arr, int level, int levels, int povrsti){
	printf("%d ", arr[index]);
	
	// ce gledas ta magic, in neves zaki se gre mi napis na fb
	if(level != levels){
		rek (index + pow(2,(double)level -1) + povrsti    , arr, level + 1, levels, povrsti * 2);
		rek (index + pow(2,(double)level -1) + povrsti + 1, arr, level + 1, levels, povrsti * 2 + 1);
	}
	return;
	
}

int main() {
	int levels;
	scanf("%d", &levels);
	
	double reads = pow(2, (double)levels) - 1;
	printf("%lf\n", reads);
	
	int *arr = malloc (reads * sizeof(int));
	
	for(int i = 0; i < reads; i++){
		scanf("%d", &arr[i]);
	}
	
	rek(0, arr, 1, levels, 0);
	printf("\n");
	
	return 0;
}
