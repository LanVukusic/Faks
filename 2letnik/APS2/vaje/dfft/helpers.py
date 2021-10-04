import numpy as np

omega = 3
mod = 7
n = 6


def f(x): return omega ** x % mod


# vandermorde za dft
a = [[0 for i in range(0, n)] for i in range(0, n)]
print(a)

for i in range(0, n):
    for j in range(0, n):
        #print(f(j*i), " ", end="")
        a[i][j] = f(i*j)

np_a = np.array(a)
print(np_a)


# pomnozi s polinomom

poly = np.array([1, 3, 6, 0, 2, 0, ]).T
print(poly)

res = np.dot(np_a, poly)
print(res % 7, "polinom v vrednostnem zapisu")

b = [[0 for i in range(0, n)] for i in range(0, n)]
for i in range(0, n):
    for j in range(0, n):
        #print(f(j*i), " ", end="")
        b[i][j] = f(-1*i*j)

np_b = np.array(b)
np_inv = (1/n) * (np_b)

print(np_b)
