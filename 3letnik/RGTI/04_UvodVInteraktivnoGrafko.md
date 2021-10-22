## kako to dela?

- Specifikacija oglišč
  - Grafičnemu cevovodu podamo tabelo točk (x,y,z)
- Zračunamo transformacije
  - Kako? tema za dans.

# Transformacije in koordninatni sistemi

[Slides](https://ucilnica.fri.uni-lj.si/pluginfile.php/182263/mod_resource/content/0/12%20Transformacije%20in%20koordinatni%20sistemi.pdf)

## Osnovne količine

- Skalarji
- Vektorji
  - Nima položaja v prostoru
  - Linearne kombinacije
  - linearna odvisnost
- Točke

  - Majo plac v prostoru
  - rabmo kordinatni sistem

- Kordinatni sistem
  - Evklidski sistem
    - pravokotni bazni vektorji
    - vsi dolzine 1
  - desnosučni in levosučni sistemi
    - x in y sta fiksna, Z pa pravokoten v + al pa -
    - desnosučnega nardiš z desno roko

** Ponovi use operacije med vektorji **

Skalarni produkt je ful uporabn. Moreš vedet.

Vektorski produkt:

- rezultat je vektor, pravokoten na ostala dva v DESNOSUČNEM sistemu

## Transformacije

Afine transformacije

- osnovne
  - translacija
  - rotacija
  - skaliranje
- predstavimo z matričnim množenjem

Sestavljanje transformacij

- P je vektor točk;
- T . R . S . P
  - Ni treba mnoziti vsake matrike posebej z točkami P, lahko pomnozimo vse transformacije skupaj, na to pa vse to pomnozimo z tockami.
  - najprej se izvede Skaliranje, na to Rotacija, na to Translacija

Planarne projekcije:

- ohranijo črte
- delimo na
  - Vzporedne / Ortographic
  - Perspektivne / Isometric
