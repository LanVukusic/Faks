l = int(input("Vnesite število levkocitov / µl: "))
n = float(input("Vnesite število nevtrofilcev [%]: "))
t = int(input("Vnesite število trombocitov / µl: "))
AST = float(input("Vnesite nivo AST [UI/L]: "))
s = float(input ("Vnesite starost pacienta [leta]: "))

if l>13700:
    print("nizko tveganje (N5)")

if l<=13700 and AST<=35.0 and n<=68.0:
    print("nizko tveganje (N1)")

else:
    if l<=13700 and AST<=35.0 and n>68.0 and t<=291.00:
        print("visoko tveganje (V1)")
elif: l<=13700 and AST<=35.0 and n>68.0 and t>291.00:
        print("nizko tveganje (N2)")

else:
    if l<=13700 and 35.0<AST<50.0  and t<=28200 and s<=6.75:
        print("nizko tveganje (N3)")

else:
    if l<=13.700 and 35.0<AST<50.0 and t<=28200 and s>6.75 :
        print("visoko tveganje (V2)")

else:
    if l<=13.700 and 35.00<AST<50.0 and t>28200:
        print("nizko tveganje (N4)")

else:
    if AST>50.0:
    print("visoko tveganje (V3)")
