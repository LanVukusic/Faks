# Ransac

Mamo samo basic RANSAC, ful več so ga nadgradil.

## Intuition:

- Ideja je da dober fit prepoznaš, kljub slabi dati.
- zračunaš threshold in ignoriras tocke vn iz thresholda
- algo
  - uzemi 2 random sampla iz modela in sampli
  - to narediš n-krat, in enkrat pač najdeš dve točki ka sta skozi NE outlierje
  - sproti si zapomneš najboljši fit (kjer je najbolši support, aka največ točk na premici)

## parameter choice:

Predvidevamo da ima noise gausovo distribucijo, za to lahko uzamemo error velik
$$2 \sigma : t = 2 \sigma$$
in nardimo tolko velik **error boundr**  
Rabimo pa samplat inlyer set VSAJ ENKRAT. To moremo zagotovit čene ne konvergiramo. Da se to zgodi pa nocem prevec iteracij.  
To izračunamo:

Naslednje vrjetnosti so za 1 TOČKO:

- $$ e \implies P(outlier)$$
- $$ N \implies P(inlier) = 0.99$$
- $$ \text{prob of failure}, P(bad) \implies 1 -P(outlier)$$
- Mi hočemo met vrjetnost teg p(bad) čim manjše oziroma 0
- $$ P(bad) = 1-(1-e)^s $$
- kjer je S Number of parameters
- $$ 1 - P = [1-(1-e)^s]^N $$
- $$ N = \frac{\log(1-p)}{\log(1-(1-e)^s)}$$

tabela primerjav parametrov, iteracij in performansa
[Tabela](https://www.researchgate.net/profile/Philip-Torr/publication/227004520/figure/tbl2/AS:668282281537557@1536342320566/The-number-m-of-subsamples-required-to-ensure-Y-095-for-given-p-and-where-Y-is-the.png)

## po konvergenci

Ko konvergiraš z RANSACom, si se ubistvu znebiu outlierjev. Dost bolš je zdej uporabit še nek best fit (LSE) na svoji dati.

Sam po sebi perormance ni dober, ker je dokaj noisy, model je biu namreč sestavljen samo na random selekciji, dobro pa se je znebiu outlierjeu.

Advanced RANSACi dejansko optimizirajo vrjetnosti za outlierje in inlierje

poglej tudi

- hugh transform
- iterative closest point
  - za point cloude in te fore
  - ubistvu svoj model vlečeš k pravilnemu modelu iterativno z uporabo LSE

q:
a nemoreš samo weightat stvari da so majn pomembne dlje odlinije ka so?
