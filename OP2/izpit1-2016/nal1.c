#include <stdio.h>
#include <stdlib.h>

int main () {
	int p, q, k;
	scanf("%d %d %d", &p, &q, &k);
	
	int num = p * q;
	
	int sestevek = 0;
	
	for (int i = 0; i < k; i++){
		sestevek = 0;
		while (num != 0){
			sestevek = sestevek + (num % 10);
			num = num / 10;
		}
		num = sestevek;
	}
	printf("%d\n", sestevek);
	
	
	return 0;
}
