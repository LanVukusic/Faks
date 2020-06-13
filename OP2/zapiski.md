# operator &  
__Fora tega "end"-a je da ti da memory naslov stvari. recimo:__ 
```c
#include <stdio.h>

int main(){
    int i = 3;
    printf("%18p",&i);  // sprinti naslov od i -ja
}
```
> 0x55810e567018

# POINTERJI *
__Tole ( * ) ti da pointer do neke vrednosti. To nam omogoča da delamo z njihovimi naslovi in ne z vrednostjo direkt. To je kul kr lahko potem to vrednostspreminjaš al pa jo pointaš drugam.  
Primeri:__  

Tko dekleriraš pointerje:  
```c
int    *ip = NULL;;    /* dobro je da ih initaš z null */
double *dp;    /* pointer to a double */
float  *fp;    /* pointer to a float */
char   *ch     /* pointer to a character */

if(ip)     /* succeeds if p is not null */
if(ip)    /* succeeds if p is null */
```

Lep primer uporabe:
```c
#include <stdio.h>

int main () {

   int  var = 20;   /* actual variable declaration */
   int  *ip;        /* pointer variable declaration */

   
   ip = &var;  /* store address of var in pointer variable*/

   /* NASLOV o vrednosti kot stevilka */
   printf("Address of var variable: %x\n", &var  );

   /* NASLOV od vrednosti */
   printf("Address stored in ip variable: %x\n", ip );

   /* VREDNOST na NASLOVU  */
   printf("Value of *ip variable: %d\n", *ip );

   return 0;
}
```
 vrne :  
> Address of var variable: bffd8b3c  
  Address stored in ip variable: bffd8b3c  
  Value of *ip variable: 20


__Pointer lahko tudi povečaš z recimo _ptr++_; Zabavno je, kr ti "visokonivjoski C" nardi u ozadju vs garbage iz ARS-a in ti naslov dejansko inkrementira za 4, tako da kaže na nov ukaz. Hvala C.  
Uporabna vrednost tega je recimo to da lahko tko na izi loopaš čez tabele:  
Primer:__  
```c
#include <stdio.h>

const int MAX = 2;

int main () {

   int  var[] = {10, 100};
   int  i, *ptr;

   ptr = var;  // damo v pointer lokacijo tabele
   i = 0;
	
   while ( ptr <= &var[MAX - 1] ) {  // ponterje lahko primerjamo z klasicnimi operatorji

      printf("Address of var[%d] = %x\n", i, ptr );
      printf("Value of var[%d] = %d\n", i, *ptr );

      /* point to the next location */
      ptr++;  // dejansko inkrementira na naslednji poravnan naslov. AKA +4
      i++;
   }
	
   return 0;
}

```
> Address of var[0] = bf882b30  
 Value of var[0] = 10  
 Address of var[1] = bf882b34  
 Value of var[1] = 100


__Seveda nam nihče ne preprečuje da se nebi malo pozabavali in nardili pointer fuckfest.  
Primer:__  

```c
#include <stdio.h>
 
int main () {

   int  var;  // nič hudega sluteči int
   int  *ptr;  // pointer → int
   int  **pptr;  // pointer → pointer → int

   var = 3000;

   /* take the address of var */
   ptr = &var;

   /* take the address of ptr using address of operator & */
   pptr = &ptr;

   /* take the value using pptr */
   printf("Value of var = %d\n", var );
   printf("Value available at *ptr = %d\n", *ptr );
   printf("Value available at **pptr = %d\n", **pptr);

   return 0;
}
```

>Value of var = 3000  
 Value available at *ptr = 3000  
 Value available at **pptr = 3000


__IMO je najbolj pomemben deu pointerjeu to da jih lahko metaš u funkcije in pol mutiraš zadeve iz funkcije vn.  
Primer:__ 


```c
 int A, B; 
 B = 5; 
 A = twoValues(B); 

 int twoValues(int *x){  // funkcija išče reference
  int y = x * 2;
  x = x + 10;  //lahko mutiramo tud podan element
  return y; 
 }

 printf("A:%d, B:%d",A,B)
```
>  A:10  B:15  

A drži vrednost ki jo zračuna funkcija  
B pa dobi novo vrednost, ki jo je funkcija spremenila

# TABELE  

```c
int data[100]; 
int mark[5] = {19, 10, 8, 17, 9};
int mark[] = {19, 10, 8, 17, 9};
int *ptr[5];  // yes pointers mmm.... to je prazn array pointrjeu

// branje in pisanje elementov
mark[2] = -1;

// to je usefull trick
// input shrani na i-to mesto arraya
scanf("%d", &mark[i-1]);
```
To je ubistvu to.  
Mamo sicer še 2D ka zgledajo tko:  

```c
int c[2][3] = {{1, 3, 0}, {-1, 5, 9}};
int c[][3] = {{1, 3, 0}, {-1, 5, 9}};
int c[2][3] = {1, 3, 0, -1, 5, 9};

float x[3][4];
```

Ker je C zelo gay, predstavimo _string_ e z tabelo 🙉🙊
