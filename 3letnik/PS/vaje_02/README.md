# Prijateljska števila

Napišite vzporeden program s pomočjo pthreads, ki bo izračunal
vsoto vseh parov prijateljskih števil na intervalu [1, N], kjer je N
poljubno število.

Delo enakomerno porazdelite med niti. Uporabite oba statična in
dinamični pristop.
Statična enakomerna delitev N iteracij zanke med T niti

- Število iteracij, ki jih naredni nit
- $$ t_i = (i + 1) \frac{N}{T} − 𝑖 \frac{N}{T} $$
- Ni dobra rešitev za ta problem
  Statična krožna delitev N iteracij zanke med T niti
- Določimo velikost paketa Np
- Nitim pakete dodelimo izmenično (round-robin).
  Dinamična delitev N iteracij zanke med T niti
- Določimo velikost paketa Np
- Ko je nit prosta pogleda kateri je naslednji paket v
  vrsti, ki še ni bil sprocesiran in ga prevzame.

# Ugotovitve

## prvo, brez threadinga

test vzame:

- **30000** iteracij, **1187396** mikrosekund

## primerjava z pythonom

test vzame:

- **30000** iteracij, **17839078** mikrosekund

## naivni threading

- **30000** iteracij, **480841** mikrosekund
- **30000** iteracij, optimiziran compile s -Ofast **se ne izvede**
  - ne izvedejo se tudi ostali optimizirani buildi

## cache plus thread

- - **30000** iteracij, **338298** mikrosekund
