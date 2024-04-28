import time
N = 30000


def deli(n):
    sum = 0
    lol = int(n // 2) + 2
    for i in range(1, lol):
        if n % i == 0:
            sum += i
    return sum


start = time.time()
sumParov = 0
for i in range(1, N):
    vsota = deli(deli(i))
    if vsota == i:
        sumParov += vsota + i

stop = time.time()
print(sumParov)
print(f"time: {stop-start}")

# print(sum(([lambda x:  sum([i if i % x == 0 else 0 for i in range(x)] + sum([i if i % x == 0 else 0 for i in range(x)])(i) if i == lambda x:  sum([i if i % x == 0 else 0 for i in range(x)])(lambda x:  sum([i if i % x == 0 else 0 for i in range(x)])(i)) else 0 for i in range(x)])
