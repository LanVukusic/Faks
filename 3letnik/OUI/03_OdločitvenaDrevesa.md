# Odločitvena drevesa

... lots of content ...

## Učenje iz šumnih podatkov (noisy dataset)

## prostor hipotez odločitvenih dreves

Atribute delimo na:

- Zvezni
  - nemorem uporabit entropije, treba diskretizirat
- Diskretni

Privzeta točnost (default accuracy) :

- minimalna pričakovana točnost
- vrjetnost večinskega razreda
- če je performance modela manjši od privzete točnosti je neuporaben.

Relativna frekvenca:

- `m/n`
- št samplov / velikost nabora

Pristranost (bias):

- Cilj je maksimizirat točnost modela (na generalnih podatkih!)
  - Overfitting ?
  - če podatke preveč fittamo na training set, bo training loss zelo nizek, testing loss pa se poviša

**m_value** = ocena ki lahko vzame apriorno znanje.
