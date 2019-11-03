# a ... levkociti
#b ... nevtrofili
#c ... trombociti
#d ... AST
#e ... starost

a = float(input('Vnesite število levkocitov / µl: '))
b = float(input('Vnesite delež nevtrofilcev [%]: '))
c = float(input('Vnesite število trombocitov / µl: '))
d = float(input('Vnesite nivo AST [IU/L]: '))
e = float(input('Vnesite starost pacienta [leta]: '))
if a > 13700 :
    print('nizko tveganje (N5)')
else :
    if d > 50.0 :
        print('visoko tveganje (V3)')
    elif d > 35.0 and d > 50.0 :
        if c > 282000 :
            print('nizko tveganje (N4)')
        else :
            if e <= 6.75 :
                print('nizko tveganje (N3)')
            else :
                print('visoko tveganje (V2)')
    else :
        if b <= 68.0 :
            print('nizko tveganje (N1)')
        else :
            if c <= 291000 :
                print('visoko tveganje (V1)')
            else :
                print('nizko tveganje (N2)')
        
        
