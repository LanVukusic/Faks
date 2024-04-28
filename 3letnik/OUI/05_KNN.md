# K najbližjih burazov

[When in doubtt poglej wiki](https://en.wikipedia.org/wiki/K-nearest_neighbors_algorithm)

- **neparametrična**, ne ocenjuje parametrov modela.
- učenje na podlagi prmerov
- Leno včenje, z učenjem odlaša vse do povpraševanja

Ideja:

- Izberi `K` najbližjih parametrov. (glede na neko mero razdalje)
- med temi K dobimo večinski razred.

Vizualizacija  
[Na tem linku](https://towardsdatascience.com/knn-visualization-in-just-13-lines-of-code-32820d72c6b6)

Pri izbiri / klasifikaciji je zelo pomembno kak K izberemo.

- **premajhen k** ponavadi overfita
- **prevelik k** underfita
- Ponavadi je `K=5`

## Minkowski distance

$$L^p(X_i,X_j)=(\sum_k|X_{i,k} - X_{j,k}|^p)^{\frac{1}{p}}$$
ima parameter `p`. Upliv parametra je lepo opisan na [Wikiju](https://en.wikipedia.org/wiki/Minkowski_distance)  
Basically, p=2, pa p=1 je kr usefull.

- p = 1
  - Manhattan distance
- p = 2
  - Euclidian

# KNN pri regresiji

Podobno kot pri klasifikaciji.

- Izberemo `k` primerov ki so najblizji.
- Izračunamo neko povprečje teh vrednosti (distance weighted sum ponavad)
  - locality weighted regression

Kakšen je upliv razdalje od točke na vrednost računamo po gausovi krivulji z parametrom širine kernela `Tau`  
Če je ta parameter majhen, je jedro zelo ozko (špičasto), in bo zelo močno upoštevalo lokalnost. Vrjetno bo overfitalo.  
Velik `Tau`, bo naredil široko jedro (kernel) ki bo vzelo več točk za povprečje. prileganje bo zato zelo "smooth" lahko pa celo underfitted.

# Regresijska drevesa

Lahko zgradimo drevo, kjer uporabimo posebne `linear regression leaves`  
Ideja za tem je, da mi skozi drevo reduciramo dimenzije do nečesa manjšega (recimo 2), za končno klasifikacijo pa uporabimo enostaven klasifikator / regresor (KNN, LinReg, ...)

Navadni node-i so klasično odločitveno drevo, listi pa so klasifyerji.

## Gradnja regresijskih dreves

Spoznamo nov loss : **MSE** - **Mean square error**  
$$ \text{MSE}(v) = \frac{1}{n} \sum\_{i=1}^{n}(y_i - \overline{y})^2$$

Dobimo nečistost pri levem in desnem drevesu
Dobimo mero podobno entropiji, **Pričakovana rezidualna nečistost**
