# Kako pobarvati poligone?

Vsakemu oglišču alhko damo neko barvo, face pa računajo barvo glede na oglišča ki jih definirajo.

Proces **rasteriacije** določi katere točko določajo kateri piksli.
Ko dobimo te piksle moramo zračunat kakšne vrednosti dobijo. Vsak pixel dobi vse informacije iz svojih točk. Glede na bližino do točk se različne vrednosti različno dodelijo. To je proces **Interpolacije**

Kaj je **Interpolacija** ?  
Primer linearne interpolacije:

```
A ---- x ---- ---- ---- B
```

A in B sta točki. Vsaka od nija ma neke lastnosti, nas pa zanima kakšne lastnosti ima interpoliranka `x`.  
ker je x oddaljena _4 enote (od 16ih) od A_ in _12(od 16ih) od B_, dobi:

- `4 / 16` **B** jeve vrednosti
- `12 / 16` **A** jeve vrednosti.

**Pazi!! ne obratno, think!**

Pri trikotnikih je stvar malo bol težka ker delamo v ravnini in ne na premici. Stvar gre po istem principu vendar delamo na ravnini.

delamo **Bilinearno interpolacijo**

```
        A

       x

  B          C
```

Najprej nardimo 2 interpolaciji:

- med A in B, dobimo `X_ab`
- med A in C, dobimo `X_ac`

Nrdimo še interpolacijo med `X_ab` in `X_ac`

To nardimo za vsak pixle s pomočjo matematične metode **Mstn for loop** - _Žan 2021_

Zares to nardimo z to formulo.  
alfa, beta in gama predstavimo z `a, b ,y`, dobimo pa jih z računanjem sistema enačb. Matematike za tem ne rabimo.
Če boš slučajnoooo kdaj to rabu, so to [Baricentrične koordinate](https://en.wikipedia.org/wiki/Barycentric_coordinate_system).  
Je kordinatni sistem znotraj trikotnika.

Točke interpoliramo s pomočjoteh točk preko teh funkcij:

- `P = a + b(B-A) + y(C-A)`
- `P = (1-b-y)A + bB + yC`
- `_alpha = (1-b-y)`
- `P = aA + bB + yC`

S to metodo dobimo tudi podatek, ali točka leži v trikotniku ABC:

- `0 <= a,b,y <= 1`, točka **leži** v trikotniku
- `čene`, točka **ne leži** v trikotniku

## Perspektivno pravilna interpolacija

Problem se zgodi, če nardimo črtasto teksturo, da zgleda kot spritesheet. Z drugimi besedami, barve, texture zmeraj gledajo v kamero perspektive pa ne upoštevajo
[primer](https://www.scratchapixel.com/lessons/3d-basic-rendering/rasterization-practical-implementation/perspective-correct-interpolation-vertex-attributes)

To nardimo tako, da vse barve oz atribute, delimo z **Perspektivnim faktorjem `W = z/d`**.  
`q = 1/W`  
vse interpoliranke mnozimo se z faktorjem `q`, na to pa še `q` interpoliramo.  
To dela za tebe lepo OpenGL, tko da je uredu.

**Terminologija**

- **smooth**, klasična pravilna interpolacija
- **flat**, vzame barvo enega oglišča in ne interpoliraj
- **nonperspective**, interpolira barve ampak ne uzame perspektive. (ni podprt v webGL)
