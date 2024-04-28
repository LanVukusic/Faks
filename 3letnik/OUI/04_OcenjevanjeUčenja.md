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

## Predstavitev modela naivnega bayesa naredimo z NOMOGRAMI

**Nomogram**:
[primer grafa](https://upload.wikimedia.org/wikipedia/commons/thumb/c/c4/Components_of_a_Nomogram.png/640px-Components_of_a_Nomogram.png)

Druga opcija kako narisat je taka: [primer 2. grafa](https://www.researchgate.net/profile/Michael-Kattan/publication/220699038/figure/fig3/AS:670356629094405@1536836883198/A-nomogram-for-prediction-of-survival-probability-of-a-passenger-on-HMS-Titanic.png)

- Zbereš **en končni class** in za tistga računaš
- Vsaka daljica je svoja skala
- od leve proti desni so vrednosti, ki bolj pripomorejo k izhodu izbranega classa
- če pri "male - female" daš na pou dobiš nek garbage. Don't do that.

## logistična funkcija

$$logit(P) = \log(\frac{P}{1-P}) $$

`logit_h(C| x1 x2 x3) = logitP(C) + log(P(x_i|C) / P(x_i | !C))`
$$\frac{P(X_i | C)}{P(X_i | \overline{C})} \implies \text{ODDS RATIO}$$

Za vse atribute nardiš vsoto točk  
`SUM_i(log(OR(X_i)))`

$$\sum_i \log (OR(X_i)) $$

Kar dejansko rabiš je ta formula:  
računamo za _crew_  
Iščemo doprinos classa _Yes_  
`(P(yes | crew) / P(no | crew) ) / (P(yes) / P(no))`

$$ \frac{P(X_i | C)}{P(X_i | \overline{C})} = \frac{\frac{P(C|X_i)}{P(\overline{C} | X_i)}}{\frac{P(C)}{P(\overline{C})}}$$
