# STICHING

- Find and detect keypoints
  - per image, no other image considered
- Find corresponding pairs

## Keypoint selection

1. **Random**  
   To je samo bad ka je premajhna šansa

2. **Uporabimo nek keypoint detector**
   Uporabimo detector ki zazna podobne keypointe.  
   Znat mormo tud nardit nek deskriptor točke ki bo pomagal pri matchingu točk na druge točke iz slike.

# (SINGLE SCALE) KEYPOINT DETECTION

## Harris corner detector

[**Harris corner detector**](https://en.wikipedia.org/wiki/Harris_corner_detector)

Rabimo detecotr ki nardi tole:

- majhna vrednost za "ne kanton"
- visoka vrednost za "ja kanton"

Potem pa nardimo sam sweep čez sliko s tem kernelom in potegnes maksimume.

**Kaj je sploh corner**  
Primerjaj kaj je v regiji proti temu kaj je znotraj regije.

Primerjaš "self simillarity" kar ti pove "kok se slika spremeni če se premiknem levo desno gor dol. Na cornerju se bo ful premikalo intenziteta, na plane-u pa skor nič.

Funkcija je ubistvu (intenziteta - intenziteta*premaknjena)² \* W
$$ E_R = \sum*{\text{pixle wise}} w(x,y) -(I(x,y)-I(x-u,y-v))^2$$
W je samo gausova utež za to so stvari na sredini regije bol weighted kokr na robovih.

delta **u** in **v** lahko lineariziramo za majhne spremembe in dobimo bolj simple zadevo.

$$ I(x+\Delta x,y+\Delta y)\approx I(x,y)+I*{x}(x,y)\Delta x+I*{y}(x,y)\Delta y $$

source: [wiki](https://en.wikipedia.org/wiki/Harris_corner_detector)

Iz tega dobimo covariance matrix, ki nam pove kako so gradienti razpotegnejni.
Če imamo corner bodo gradienti v vse strani veliki saj je sprememba v x in y. če imamo horizontalen rob bo samo grad_X precej velik, pri vertikalnem robu pa bo Y velik.  
Problem je da računamo lastne vrednosti matrike ki je zelo počasno.
Ker pa nas zanima samo priblizna magnituda le teh, ter razmerje med njima, lahko to naredimo z fancy linearno algebro in dobimo dovolj dober estimate.

$$ \det(M) - \alpha \text{trace}^2(M) > t $$

1. source images
2. image derivatives
3. squared derivative
4. blur with gausian filter
5. vertikal

## Hession corner detection

[wiki](https://en.wikipedia.org/wiki/Hessian_affine_region_detector)  
Second most popular detector
primerja svetlost točke z svetlsotjo takojšnjih sosedov ( uteženo ).

Če to plottamo, dobimo graf kjer lahko vidimo vrhove na mestih kjer se pixle razlikuje od sosedov.

**Hessian matrix** je matrika v x,y,... drugih odvodov f(x,y) kar ti pove kolko se v določeni točke sosedi hitro spreminjajo esentially. Če se ful spreminjajo si vrjetno v neki kotni točki.

# scale variance / invariance

Če zoomiraš v korner preveč, lahko hitro stvar zgleda kokr edge. Hence single scale detectors. Rabimo neki bl splošnega ki je scale invariant

## scale invariant solutions

Keypointi morejo detektirat tudi scale že sami po sebi, kar ful ni tok enostvavno. Rabi bit namreč per point

Lokalno zgledajo te točke kokr **blobi**, sepravi neki tmnega na svetlem ali obratno.

**Laplacian gausian filter** je kernel ki računa razliko med noter in okrog. Je koncentričen filter (rotacijska invarianca!). gleda tko da je okrog meh, pol je ring pozitivnih uteži in sredina je negativen krog.
[wiki](https://homepages.inf.ed.ac.uk/rbf/HIPR2/log.htm)  
Naredimo parameter sweep skozi sigmo filtra in ugotovimo pri katerih velikostih imamo "peak" sepravi pri katerem scalu dobimo najbolj "edge looking" blob

LapOfGaus is **really expensive** ker moremo delat več konvolucij za več velikosti. Na srečo lahko LoG nadomestimo z razliko dveh gausovih kernelov.

**big brain** poglej si laplacian gausian aproximation pyramid.  
nardiš N nivojev gausovih blurov, odšteješ dva sosednja nivoja in dobiš edge blobe.
