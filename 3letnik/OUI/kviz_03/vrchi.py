# vrci
# Definicija naloge
# Na voljo imamo n vrčev različnih velikosti (njihove velikosti so podatek)
# in ciljno količino vode. Poišči tako zaporedje prelivanja med vrči,
# da bo v eni od posod na koncu izbrana ciljna količina vode.
# Na vsakem koraku imamo na voljo akcije: a) iz pipe doliješ vodo do roba poljubne posode, b)
# izliješ vso vodo iz poljubne posode in c) preliješ vodo iz ene posode v drugo;
# če je v prvi posodi več vode kot v drugi, vodo naliješ do roba druge posode,
# preostanek pa ostane v prvi posodi.


class Vrci:

    def __init__(self, volumni, cilj):
        self.volumni = volumni
        self.cilj = cilj
        self.kolicine = [0] * len(volumni)

    def __str__(self):
        return "kolicine vode = {}, volumni = {}, cilj = {}".format(self.kolicine, self.volumni, self.cilj)

    def move(self, move):
        m, cost = (move, 0)
        v1, v2, kol = m
        if v2 > -1:
            self.kolicine[v2] += kol
        if v1 > -1:
            self.kolicine[v1] -= kol

    def undo_move(self, move):
        m, cost = (move, 0)
        v1, v2, kol = m
        self.move(((v2, v1, kol), cost))

    def generate_moves(self):
        moves = []
        for i, f in enumerate(self.kolicine):
            # napolni
            if f < self.volumni[i]:
                moves.append(((-1, i, self.volumni[i]-self.kolicine[i]), 1))
            # sprazni
            if f > 0:
                moves.append(((i, -1, self.kolicine[i]), 1))
            # pretoki
            for j, fj in enumerate(self.kolicine):
                if j == i or not fj or f == self.volumni[i]:
                    continue
                moves.append(
                    ((j, i, min(self.volumni[i] - self.kolicine[i], self.kolicine[j])), 1))
        return moves

    def solved(self):
        return self.cilj in self.kolicine


if __name__ == "__main__":
    import search
    a = Vrci([3, 5], 4)
    print(a.generate_moves())
    resitev = search.ID(a)
    print(resitev)
