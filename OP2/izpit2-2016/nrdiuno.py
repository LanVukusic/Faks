import numpy as np

p = 1000  # int(input("visina: "))
q = 1000  # int(input("sirina: "))
d = 15000 # int(input("gorivo: "))

with open("in2.txt", "w") as f:
	nums = np.arange(p*q)
	np.random.shuffle(nums)
	count = 0
	f.write("{} {} {}\n".format(p, q, d))
	print(len(nums))
	for x in range (p):
		for y in range (q):
			f.write("{} ".format(nums[count]))
			count += 1
		f.write("\n")
