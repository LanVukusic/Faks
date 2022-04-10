# Things to know

## Laplacian loss, rules of succession
- wiki page of the [Basis](https://en.wikipedia.org/wiki/Rule_of_succession)
- Esentially `m + 1` in `n + 2`
- Measures the probability that a positive metric will have a positive influx on the class 

Laplacian probability:
$$ P_{Laplace} = \frac{n+1}{N+k}$$
- where `k` is number of clases, `n` number of all wanted results and `N` number of all results.

Laplacian loss:
$$ e = 1 - (P(B=1) \cdot P_{Laplace} (B=1 | A=1) + P(B=0) \cdot P_{Laplace} (B=0 | A=0))$$

## m-loss function.
- Custom weights of apriory knowledge
$$ P_m = \frac{n + p_a m}{N + m} $$ 
- where `m` is weight, `n` number of all wanted results and `N` number of all results.

## Residual entropy 
- Remaining entropy of A knowing B.
- divide A between classes of B
$$  $$

## IG - Information gain 
- Amount of information gained from metric A, in regard to known metric B

- if metric B has entropy of `H(B)`
  $$ IG(A) = H(B) - H_{res}(A) $$ 

## IGR - infromation gain ratio
- Information gain / entropy of information
$$ IGR(X) = \frac{IG(X)}{H(X)} $$ 

## GINI index
- wiki page of the [GINI coefficient](https://en.wikipedia.org/wiki/Gini_coefficient)

$$ Gini = 1 - \sum_{i=1}^c (p_i)^2 $$
