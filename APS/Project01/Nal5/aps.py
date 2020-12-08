class Stanje:
  stolpci = []
  sirina = 0
  instr = None

  def __init__(self, sirina):
    #print("delam stolpce dolzine {}".format(sirina))
    self.sirina = sirina
    self.stolpci = self.stolpci[:]
    self.instr = []
    for _ in range(sirina):
      self.stolpci.append([])
  
  def describe(self):
    print()
    print("-------------------")
    print(hash(self))
    for i in self.stolpci:
      print("-",end="")
      for j in i:
        print(j,end="")
      print()
    print("-------------------")
    print()

  def copy (self):
    temp = Stanje(self.sirina)
    for i in range(self.sirina):
      temp.stolpci[i] = self.stolpci[i][:]
    temp.instr = self.instr[:]
    return temp

  def __hash__(self):
    agr = ""
    for i in self.stolpci:
      agr += str(len(i))
      for j in i:
        agr += str(j)
    return hash(agr)
  
  def __eq__(self, other):
    if not isinstance(other, Stanje):
      return False

    if len(self.stolpci) != len(other.stolpci):
      return False
    
    for i in range(len(self.stolpci)):
      if len(self.stolpci[i]) != len(other.stolpci[i]):
        return
      for j in range(len(self.stolpci[i])):
        if self.stolpci[i][j] != other.stolpci[i][j]:
          return False
    return True
  
  def __ne__(self, other):
    return not self.__eq__(other)

def solve():
  zacetek = None
  konec = None
  import sys

  print(sys.argv[1])
  with open(sys.argv[1], "r") as f:
    sirina, visina = [int(i) for i in f.readline().split(",")]
    zacetek = Stanje(sirina)
    konec = Stanje(sirina)

    for i in range(sirina):  # gre skoz za vsak stolpec
      for crka in f.readline().split(":")[1].split(","): # za vsako kocko v stolpcu
        crka = crka.strip()
        if crka == "":
          continue
        zacetek.stolpci[i].append(crka)
      

    for i in range(sirina):  # gre skoz za vsak stolpec
      for crka in f.readline().split(":")[1].split(","): # za vsako kocko v stolpcu
        crka = crka.strip()
        if crka == "":
          continue
        konec.stolpci[i].append(crka)


  zacetne_slike = set(tuple([zacetek]))  # oo kaj je to? neka shit janky koda.... ja je deal with it...aveš ku sm žiuc
  koncne_slike = set(tuple([konec]))

  flipflop = True  # če je true gledamo začetek, čene gledamo konec
  while True:

    # generiraj nove faking opcije
    temp_nivo = set()
    if flipflop:
      b = zacetne_slike
    else:
      b = koncne_slike

    for a in b:
      for stolpci_iz in range(len(a.stolpci)):
        for stolpci_v in range(len(a.stolpci)):
          
          # če vrstica ni faking prazna
          if len(a.stolpci[stolpci_iz]) == 0 or len(a.stolpci[stolpci_v]) == visina:
            continue

          # če "vrstica iz" ni "vrstica v"
          if stolpci_iz == stolpci_v:
            continue
          
          # nardimo shallow copy kar se izkaže da  - sm either jst retard - je to ful tezje kokr bi moglo bit
          temp = a.copy()
          temp.instr.append((stolpci_iz+1, stolpci_v+1))
          temp.stolpci[stolpci_v].append(temp.stolpci[stolpci_iz].pop())
          temp_nivo.add(temp)
    

    # updejtaj nivoje  
    if flipflop:
      zacetne_slike = temp_nivo #- zacetne_slike
    else:
      #koncne_slike = koncne_slike
      koncne_slike = temp_nivo #- koncne_slike
    
    flipflop = not flipflop
    
    #precekiraj ujemanja med faking opcijami
    inter = zacetne_slike.intersection(koncne_slike)
    #print(inter)
    print(len(koncne_slike), len(zacetne_slike))
    if len(inter) > 0:
      for i in zacetne_slike:
        for j in koncne_slike:
          if i == j:
            print("REŠITEV :")
            for ins in i.instr:
              print("Vzemi: {}".format(ins[0]))
              print("Izpusti: {}".format(ins[1]))
            for ins in j.instr[::-1]:
              print("Vzemi: {}".format(ins[1]))
              print("Izpusti: {}".format(ins[0]))
            
            print("\n")
            

            
      # ce najdemo ujemanje smo gucci... lahko samo sprintamo
      break

    
    
   


if __name__ == "__main__":
    solve()