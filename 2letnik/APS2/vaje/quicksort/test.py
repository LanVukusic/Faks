lst = [3, 1, 4, 1, 5, 9, 2, 6]
i, j = 0, len(lst) - 1
pivot = 3

while i <= j:
    while lst[i] < pivot:
        i = i + 1

    while lst[j] > pivot:
        j = j - 1

    if i <= j:
        lst[i], lst[j] = lst[j], lst[i]
        i = i + 1
        j = j - 1

print(lst)
