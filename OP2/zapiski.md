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
```c
char str1[12] = "Hello";
char greeting[6] = {'H', 'e', 'l', 'l', 'o', '\0'};

// char arraye lahko normalno printamo in nam izpiše "Hello"
printf("Greeting message: %s\n", greeting );
```
>Greeting message: Hello

Če so stringi Null terminated ('\0' nakonc), lahok na jih zmetamo en kup funkcij kot so:  
|Funkcija|Opis|
|---|---|
|strcpy(s1, s2);|Kopira S2 v S1|
|strcat(s1, s2);|Concata (prilepi) S2 na konc S1|
|strlen(s1);| Vrne dolžino stringa|
|strcmp(s1, s2);| Vrne: __0__ ko sta S1 in S2 enaka; __<0__ ko S1<S2; __>0__ ko S1>S2|
|strchr(s1, ch);|Vrne pointer na mesto kjer se prvič nahaja ch v S1|
|strstr(s1, s2);|Vrne pointer na mesto kjer prvič najde S2 v S1


# STRUCTI
To je najbližji JSON-a ka boš v Cj pršu  
```c
struct Person {
    char name[50];
    int citNo;
    float salary;
};

// dostopamo z . kot pri normalnih jezikih
float letePare = person2.salary; 

// tko passaš te structe v funkcijo
float dvojniDenarci (struct Person osebica){
    return osebica.salary * 2;
}

int main(){
    // tko lahko definiraš nove osebe
    struct Person person1, person2, p[20];
    return 0;
}
```
Ker hočemo pisat bl hitro in smooth obstaja še ta notacija:  
```c
//agh bad no
struct Distance{
    int feet;
    float inch;
};

int main() {
    struct Distance d1, d2;
}


//mmm gooood yes
typedef struct Distance{
    int feet;
    float inch;
} distances;

int main() {
    distances d1, d2;
}
```
Lahko jih nestaš obviously...  

Tuki pridemo do naslednjega bolečega spoznanja...in sicer __malloc__ in __sizeof__.  
Primer uporabne na structih:  

```c
struct person {
   int age;
   float weight;
   char name[30];
};

int main(){
  struct person *ptr;
   int i, n;

   printf("Enter the number of persons: ");
   scanf("%d", &n);

   // allocating memory for n numbers of struct person
   ptr = (struct person*) malloc(n * sizeof(struct person));

   for(i = 0; i < n; ++i)
   {
       printf("Enter first name and age respectively: ");

       // To access members of 1st struct person,
       // ptr->name and ptr->age is used

       // To access members of 2nd struct person,
       // (ptr+1)->name and (ptr+1)->age is used

       // tuki prebere in zapiše direkt na memory naslove.
       scanf("%s %d", (ptr+i)->name, &(ptr+i)->age);
   }

   printf("Displaying Information:\n");
   for(i = 0; i < n; ++i)
       printf("Name: %s\tAge: %d\n", (ptr+i)->name, (ptr+i)->age);

   return 0;
}
```
__malloc__ ti memory alocata (bruh) neko velikost na disku  
__sizeof()__ ti vrne kok je ena stvar velika. Dostkrat rabiš v uporabi z malloc da veš kolko prostora rabiš.
Ta vrstica je usefull:  

```ptr = (struct person*) malloc(n * sizeof(struct person));```

Če imamo nek struct lahko dostopamo do njegovih komponent z ".", če smo fancy af in mamo pointer "*ptr" na struct pa uporabljamo tole :  
__(ptr) -> name__; ekvivalentno __oseba.name__ če bi meli direkt osebo

# UNIONS
Okay zdej sm zvedu da je tole glih tolk JSON kokr struct.  
Fora je da __UNION__ rezervira tok memorya da lahko notr spraviš največčjega memberja, __STRUCT__ pa rezervira plac za use memberje.  



