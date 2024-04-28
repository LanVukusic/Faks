# Cevovod

**... tle sm jst spau in malo nevem ki se dogaja ...**

## Postprocesiranje oglišč

- Rezanje / clipping
- perspektivno deljenje

### Rezanje / clipping
Naloga rezanja je da odstrani in odreže vse iz FOVa. To je pač performance boost.  
Odstrani vse trikotnike izven pogleda, med near in far clipping planom, ter poreze trikotnike na meji.  

Kdaj režemo?  
Za rezanje uporabimo posebni kordinatni sistem: **Koordinatni sistem rezanja**. Tam je precej lažje. Vse stvari znotraj tega sistema so preslikane na koordinate med -1 do 1. To so **Normalized Device Coordinates - NDC**. Enostavno je določiti kaj je treba rezat ker samo filtriraš vse ki ni na intervalu [-1 1].  
Za to skonstruiramo matriko ki nam vse preslika v kocko med -1 in 1. To je dej prava perspektivna projekcija.  
Koordinate rezanja so samo pomnožene točke, kjer ne delamo še perspektivnega deljenja.


### Culling
- Izločanje / **Culling**  

izločanje zadnjih ploskev **Backface culling**  
dela se na GPUju. To lahko delamo če vemo, da so predmeti zmeraj gledani iz ene strani. Sepravi da kocke ne gledaš od notr vn.  
To se dela z normalami.  

Tehnik za izločanje je več:
 - delitev prostora (to so chunksi)
   - to sedela na CPUju
 - ... drugo lol

Izločanje zakritih ploskev **Occlusion culling**  
To dela tko, da določiš neke objekte (poligone) ki so **occluderji**. Te objekti lahko potem zakrivajo vse ostale objekte. Je kr zahtevn postopek ker moreš pre-bake-at globinske slike v vseh točkah (neki točk glede na tvoj subdivision scene) kjer računaš kje je najbližji occluder. Potem nardiš black magic in **puff** [occlusion culling](https://docs.unity3d.com/Manual/OcclusionCulling.html)


## Rasterizacija

To se dela na dva načina:
- old school
- moderno

### tradicionalna rasterizacija
- Zračuna se enačba premice za vse stranice trikotnika (črte med oglišči).
- Po vrsticah iteriramo skozi fragmente in vemo začetek in konec s tem da vzamemo premice za naše boundse.
- ker gremo po vrsticah se temu reče **scan line rasterizacija**
- PROBLEMI:
  - tezavno rezanje
  - paralelizacija je tezka
### Nova tehnika
- Zračunamo bounding box iz teh oglišč
- bruteforcaš vsak fragment a leži v trikotniku al ne
- to se nardi tko da prevedemo vse v težiščne kordinate:
  - _alfa, beta, gama_ in računamo če so med -1 in 1 
  - je **PARARELJZIJZLJIVO** (se da paralelizirat)
  - 