# include <stdio.h>
# include <stdlib.h>

int jenot (int not, int n){
	while (not != 0){
		if (not % 10 == n){
			return 1;
		}
		not /= 10;
	}
	return 0;
}

int main () {
	int a, b;
	scanf("%d %d", &a, &b);
	int num = 0;
	for (int i = 0; i < 10; i++){
		//pogledaš če je i v katerikoli od števk
		if(jenot(a, i)){
		num++;
		continue;
		}
		if(jenot(b, i)){
		num++;
		continue;
		}
	}
	
	printf("%d\n", num);

	return 0;
}
