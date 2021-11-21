# sistemi skupin pomnilnikom

## UMA

??

## NUMA

**arhitektura**:  
Proc1 -> Cache -> Mem -> Directory  
Proc2 -> Cache -> Mem -> Directory  
Proc3 -> Cache -> Mem -> Directory

Vsak procesor lahko dostopa do svojga memorya al pa da tujega. V primeru da dostopa do tujega bo dostop daljsi  
V primeru da proc1 dostopa do mem 2 si directory 2 zapiše da hrani blok od proc 1

Če dva procesorja bereta isti memory block, potem en drugemo invalidirata cache. Veliko brezveznih prenosov.

To je problem **FALSE SHARING**-a. Ubistvu delata procesorj z drugima spremenljivkama, ampak ker sta v istem bloku misli procesor da dela z eno in isto. Če sta dve spremenljivki preblizu v memoryu je lahko stvar bolj počasna.

---

## Medsebojno izključevanje

**Kaj pa če 2 procesorja pišeta v isti var?**  
Pazit moramo na "kritična stanja", kjer prvi procesor gre skozi faze "read, modify, write", drug procesor pa je prepozen, ter napiše write po tem ko 1. konča ter s tem uniči zračunano vrednost 1.

**Pregrada (Barier)**  
Pregrada forsira to, da vsi procesi pridejo do pregrade ter šele potem ko so vsi na določeni točko, lahko nadaljujejo.  
Vse niti se nekje počakajo, ter grejo šele potem naprej.

### Kako deluje preklop procesa

- procesi
- niti

```
                                ┌─────►  Join
                                │
                                │
  Različne niti    ┌────────────┤
                   │            │
─────────┬────┬────┼──┬─────────┴──┬──────
         │    │    │  │            │
         └────┘    │  └────────────┘
                   │
                   │
                   └────►Fork
```

Pomnilnik

- sklad
- koda
- spremenljivke

Proc

- registri

Vse niti imajo svoj sklad in svoje registre  
Vse ostalo je deljeno. (koda, ...)

- Registri

  - čene drugega ima vsaka svoj program counter
    - programski števci iz več razlogov ne bo na istem mestu
  - stack pointer je drug, ker klicemo druge reci, tudi ce izvajata iste stvari

- Med preklapljanjem procesov je tako veliko lazje, kopira se dost malo stvari

### Niti v C-ju

knjiznica pThread **POSIX**

- Portable
- Operating
- System
- Interface

```c
# include pthread.h
```

```c
link nagithubu
```
