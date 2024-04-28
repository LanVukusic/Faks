# Modeli večnitnih sistemov

- Upravljalec delavcev
- Enakovrdne niti
- Cevovod

- Dinamično alocirane
  - Če kreiramo niti za vsa conenction lahko hitro ratamo napadeni z denial of service.
  - specializirane niti za job
- Bazen niti
  - niti znajo vse, odvisno kak job dobijo
  - vhod čaka
- Cevovod
  - načeloma želimo enakomerno težke taske v cevovodu
  - req-> [ job1 ] -> [ job2 ] -> [ job3 ] ->

## mere pohitritev

S - speedup  
čas merimo v **wall clock time-u**
n - velikost problema  
p - število niti

$$ S(n,p) = \frac{t_s(n)}{t_p(n,p)} = \frac{\sigma(n) + \phi(n)}{\sigma(n)+ \frac{\phi(n)}{p} + \kappa(n,p)} $$
$$ \sigma(n) \implies \text{neznamo pohitrit} $$
$$ \phi(n) \implies \text{znamo pohitrit} $$
$$ \kappa(n,p) \implies \text{stroški komunikacije, kreiranje, locki, bariere ...} $$

## Učinkovitost

Kako nardit izvajanje večjega problema enako dobro kot izvajanje manjšega problema

$$ E(n,p) = \frac{t_s(n)}{p \cdot t_p(n,p)} = \frac{S(n,p)}{p}$$
Učinkovitosti nej bi se gibale nekje med 0% in 100%. 100% bi pomenilo da dejansko vseh p strojev dela 100% hard.

Če učinkovitosti grafiramo dobimo neko kvdratno krivuljo. Povečevanje števila niti nam nardi preveč overheada, premalo niti pa ne računa dovolj paralelno.  
To je treba zračunat in upoštevat.

teoretično je možno met speedup > p, E > 1; ampak če se ti to zgodi, pol maš vrjetno neki narobe.

Poenostavitev zgornje enačbe  
$$ S(n,p) \leq \frac{\sigma(n) + \phi(n)}{\sigma(n)+ \frac{\phi(n)}{p} + \kappa(n,p)} \leq \frac{\sigma(n) + \phi(n)}{\sigma(n) + \frac{\phi(n)}{p}}$$
$$ \sigma(n) + \phi(n) = t_s(n)$$
$$ \leq \frac{\sigma(n) + \sigma(n) \cdot (\frac{1}{f} - 1) }{\sigma(n) + \frac{\sigma(n) (\frac{1}{f} - 1) }{p}} \leq \frac{1}{f+\frac{1-\alpha}{p} } \implies \text{Amdahlov zakon}$$

Delež operacij ki jih ne znamo pohitrit pada s tem ko večamo sisteme. Superračunalniki se obviously splačajo ker ih folk gradi.
