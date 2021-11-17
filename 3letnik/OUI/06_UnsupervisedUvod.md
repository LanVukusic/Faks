# Nenadzorovano učenje

- je bolj subjektivne narave
- nimamo objektivne metrike

Primeri uporabe:

- odkrivanje skupin rakavih bolnikov, grupirani glede na gene
- odkrivanje skupin kupcev (buyers / nonbuyers) grupirano na zgodovino brskanja
- ...

Glavna metoda pri nenadzorovanem učenju je **clustering**.
Grupirane podatke predstavimo z drevesom ki se kliče **dendrogram**.

Metodi za clustering:

- Hierarhical clustering
  - delilno - divisive
  - združevalno - agglomeritive
- k-means clustering
  - **EM** algoritm- expectation minimization

## Združevalni

**Algoritem**:

- Začnemo tako, da je vsaka točka v svoji gruči
- algoritem računa razdaljo med točkami in najde najmanjšo
- dve najbližji točki (oz gruči) poveže
- z vsako iteracijo zmanjšamo št gruč
- Ko povežemo vse v eno gručo zaključimo dendrogram
- Dendrogram režemo na neki točki ki nam določa število gruč

**Kako merimo razdalje med gručami?**
Problem je da razdalja med dvema gručama točk ni točno definirana. Imamo več možnosti.

- Med najbližjima primeroma
  - single linkage
- Dva najdaljša primera
  - complete linkage
- Povprečje razdalj
  - zračunaš vse razdalje in vzameš povprečje (O(M x N))
- računamo

**Time complexity**:

- O(n² log (n))
- n² za računanje razdalj in log n za urejanje razdalj
- delilni pristop ima **O(2^n)**

**Parametri:**

- Katero mero razdalje izbrati
- Kater pristop za merjenje razdalj med gručami
- Ciljno število gruč
