def f(x, y, z, n):
    return 4/n == 1/x + 1/y + 1/z


n = 17
for x in range(1, 10000):
    for y in range(1, 10000):
        for z in range(1, 10000):
            if(f(x, y, z, n)):
                print(x, y, z)
