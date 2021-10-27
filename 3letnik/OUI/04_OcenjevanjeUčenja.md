# Ocenjevanje učenja

Kriterij:

- **Točnosto** - accuracy
- **Razumljivost**, kok simple za razložit basically

Nasprotujoča si cilja:

- Potrebujemo **veliko podatkov** za uspešno učenje.
- Potrebujemo **veliko podatkov** za testno množico.

Rešitve:

- Holdout
  - izvzameš en procent in z njimi ne treniraš
- Večkratne delitve aka [cross-validation](<https://en.wikipedia.org/wiki/Cross-validation_(statistics)>)

Vzorčenje:

- naključno, nenaključno (prečno preverjanje)
- poljubno ali stratificirano
  - Dejansko zagotoviš da so podatki obeh datasetov statistično reprezentativni glede na originaln dataset

## (k-fold) Cross-validation

- Učno množico razbiješ na k množic
- učni podatki so U - U[k]

`k` iteracij, kjer dobiš nice povprečje  
Služi le **statistični oceni**. Ubistvu k-krat treniraš cel model na novo kar je fulll expensive. Končni model streniraš na celotni množici `U`

**Prečno preverjanje LOO**  
Leave one out.  
k = n; dataset razdeliš na n primerov. Dejansk testiraš za vsak primer posebi.  
Izredno zamudno

\\\_\_/ cry here, africa needs water `Žan, 2021`

## Diskretni atributi

Diskretni atributi:

- nominalni
  - true, false
  - m,f
  - Slo, hr, eng
- ordinalni
  - None, some, full
  - low,med,high
  - 10-20, 40-60, 70-80

Zvezni:

- čas
- intenziteta
- trajanje
- višina

**Diskretizacija podatkov**:  
Imamo dve opciji:

- Equal frequency
  - vsak stolpec bo imel natančno `n` primerov
- Equal width
  - Vsak stolpec bo zavzemal isti interval
- Intervali, ki maksimizirajo informacijski prispevek

Je **wack** metoda, ker zgubiš svobodo zveznosti.

## Mankajoči atributi

- Ali model lahko handla mankajoče podatke?
- Ali imamo dovolj podatkov, če o mankajoči?
- Ali je "replace none" primerno?

Fixes:

- Potencialno vržemo preveč "sparse" stolpce vn
- Ignoriramo vrstice z NONE
- Uporaba vrednosti None kot svoj bucket
- Nadomestiš
  - to je tko science and art of its own

## Naive bayes [wiki](https://en.wikipedia.org/wiki/Naive_Bayes_classifier)

Bayesova formula `P(B|A) = P(A|B) P(B) / P(A)`  
Za obratno pogojno vrjetnost

`P(C| x1 x2 ...) = P(C) P(x1 x ... | C) / P(x1 x2 ...)`  
Z povečanjem števila x-ov se eksponentno povečuje število faktorjev. Ker smo lazy, uporabimo **naivni bayesov klasifikator**.

Predpostavimo da so vsi x-i med sabo **neodvisnoi** (Kar je kr nerealno tbh)
