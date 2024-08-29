# Matematika teorija lastnosti

## tr() - trace / sled

- $tr(A+B) = tr(A) + tr(B)$
- PAZI $tr(AB) = tr(A)tr(B)$ !!!
- $tr(AB) = tr(BA)$
- $tr(ABC) = tr(CBA)$
- $tr(PAP^{-1}) = tr(A)$ Podobni mata isto sled

## Simetrična A

- $A = A^T$

## Shurov

Za kvadratne matrike

- $A = QDQ^T$ - schurov razcep
  - Q ortogonalna
  - D diagonalna - lastne vrednosti na diagonali
  - tr(A) = tr(D)
  - lahko nardis za vsako PD matriko

## Ortogonalna

- $QQ^T = I$
- $Q^{-1} = Q^T$
- Preslikava je izometrija
- $det(Q) = \plusmn 1$
- $\lambda_i = \plusmn 1$

## Zgornje trikotna

- lastne vrednosti po diagonali

## SVD

- $A = U \Sigma V^T$
  - **U** ortogonalna
  - $\Sigma$ diagonalna z (ne neg) singularnimi vrednostmi po diag
  - **V** ortogonalna - singularnimi vektorji

## Cholesky

za razcep **PD** matrike v $A=L L^T$

## cilindrični / sferični / polarni

- **Polarni**
  - $det_J = r$
  - $x = r \cos \phi$
  - $y = r \sin \phi$
- **Cilindrični**
  - $det_J = r$
  - $x = r \cos \phi$
  - $y = r \sin \phi$
  - $z = z$
  
## Faking odvodi

Velja:

- $\frac{\partial (x^T a)}{\partial x} = \frac{\partial (a^T x)}{\partial x}$

Odvodi:

- $x^T A x = x^T(A+A^T)$
  - $x^T A x = 2Ax$; če je A simetrična
- $x^TA = Ax = A$
- $x^Tx = 2x$
- $Ax = A^T$
- $x^T = I$
- $||x-a||^T = 2(x-a)^T$
- $||Ax||^2 = 2(Ax)^TA$
- $||x||^2 = 2x^T$
