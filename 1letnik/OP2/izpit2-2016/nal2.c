#include <stdio.h>
#include <stdlib.h>

int main () {
	int p, q, d;
	scanf("%d %d %d", &p, &q, &d);
	
	int posx, posy;
	int next = 1;
	
	int **arr = malloc(p * sizeof(int*));
	for(int x=0; x < p; x++){
		int *line = calloc(q, sizeof(int));
		for(int y=0; y<q; y++){
			int in;
			scanf("%d", &in);
			line[y] = in;
			if(in == 0){
				posx = x;
				posy = y;
			}
		}
		arr[x] = line;
	}
	
	for (int i = 0; i < p*q-1; i++){
		// najdeš naslednjega
		for(int x=0; x < p; x++){
			for(int y=0; y<q; y++){
				if(arr[x][y] == next){
					// najdu si naslednjega
					d = d - (abs(posx - x) + abs(posy - y));
					if(d > 0){
						posx = x;
						posy = y;
						next ++;
					}else if(d == 0){
						printf("%d\n", next);
						return 0;
					}else{
						printf("%d\n", next-1);
						return 0;
					}
				}
			}
		}
	}
	
	
	return 0;
}
