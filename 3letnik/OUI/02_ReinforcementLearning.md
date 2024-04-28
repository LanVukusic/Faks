# Supervised learning

Ideja je da iščemo neko funkcijo `H(X)` - hipoteza, ki se približa najboljši dejanski funkciji `H_legit(X)`  

## tipi ločevanja
- Regresija
  - `Y` je zvezna.
  - Ordinalne spremenljivke

- Klasifikacija
  - `Y` ima definiran nabor vrednosti - RAZRED.
  - Nominalne spremenljivke
  - če mamo 2 razreda je binarna klasifikacija
  - Različne hipoteze
    - problem overfittinga
    - prave funkcije ne vemo.
    - lahko je več **konsistentnih z učno množico**
    - __splošnost__ hipoteze pove kako dobro prepozna **Testing set**
    - __interpretabilnost__ nakratko pove kolko fucked je model. a je to nek polinom al je sam kvadrat.
  - Podatki so lahko ocenjeni na različno narobe:
    - **FP** Napačno __pozitivno__ klasificiran point
    - **FN** Napačno __negativno__ klasificiran point

- Podatke delimo na
  - nevidene
  - učni podatki `X`
  - testni podatki `X'`