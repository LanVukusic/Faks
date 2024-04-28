# Predstavitev predmetov
## Mesh
Vse je sestavljeno iz poligonov, zmeri.  

Zakaj zmeraj trikotniki?
- Najmajn oglisc, najbl splosni, lahko sestaviš vse n-kotnike
- Zmeri so ravni, **PLANARNI**
  - veš da so vse tocke v njegovi ravnini, poenostavi poljenje, barvanje...
- Omogoča hitro računanje
  - iterpolacija tock med tremi oglisci
  - preseki

**Iterpolacija** je process ki na bazi točk okrog zračunaš poljubno točko notr

- So **konveksni**, nekonveksni liki so lahko extra f*cked za računanje (dej konkavn lik v kak js library and cry)
- Večina grafičnih cevovodov uporablja izključno 3kotnike.

## pregled OBJ-a
Obj je simple, tekstonvi filetype za objekte. Vsi so podobni, OBJ gledamo samo zato ker je human readable code.
- Najprej so naštete točke ( v - vertex)
- Texturne kordinate (UV cords)
- Normale
- Ploskve (f - faces)

V grafični cevovod ponavadi podamo tudi reči kot so transformacije, kamero, barve texture,.... standard unity / blender shit pač.  
Transformacije so maths hell in ga bomo obravnaval posebi.



q:
- zaki je shranjeno kokr 4 kotnik in ne kokr 2 trikotnika v OBJu
- kako shrani faking konveksne like