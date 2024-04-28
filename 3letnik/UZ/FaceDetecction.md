# Face detection

## feature extraction

Ker iskanje objekta na sliki zahteva veliko konvolucij z različnimi filtri, rabimo nek speedup.  
Najbolj big brain rrešitev z pohitritev enostavnih filtrov so INTEGRAL IMAGE [wiki](https://computersciencesource.wordpress.com/2010/09/03/computer-vision-the-integral-image/)  
Deluje tako da delamo kululativno sumo nad piksli. Vsk pixel predstavlja vsoto rect-a ki ga bounda z točko 0,0.  

Narediš miljon različnih random filtrov, potem pa jih daš v AdaBoost, ki bo uzel samo najjače fitlre. Končau boš z samo majhnim številom ful hitrih filtrov.

## hard negative mining

Ko treniramo image classifier, vzamemo ogromen dataset in preklasificiramo vse. Dobili bomo par reči ki so klasificirani kot face, ampak niso face. to so **HARD NEGATIVES**. Trivialne primere lahko vržeš proč in se fokusiraš bolj na te problematične

## Non maxima supresson
poglej si tole 

# End to end feature learning
