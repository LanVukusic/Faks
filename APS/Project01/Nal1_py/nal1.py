def solve ():
    with open("test.txt") as t:
        data = t.readline().split(",")
        road_len = int(data[0])
        max_tank = int(data[1])
        NumStops = int(data[2])

        stops = []
        options = []

        for i in range(NumStops):
            data = t.readline().strip().split(":")[1]
            stops.append(map(int, data.split(",")))

    # solving the problem
    curr_dist = 0
    # append starting option
    options.append({"price":0, "dist":0, "gas":max_tank, "stops":[]})
    for index, curr_stop in enumerate(stops):
        curr_dist += curr_stop[0] #this is distance
        delete = []
        news = []
        for ind, curr_option in enumerate(options):
            if(curr_option["dist"]+curr_option["gas"] >= curr_dist):  # if this path can reach this place
                # we either continue without refill
                curr_option["dist"] += curr_stop[0]
                curr_option["gas"] -= curr_stop[0]

                # or we refill the tank
                new_price = curr_option["price"]+curr_stop[1]*(max_tank - curr_option["gas"])
                my_stops = curr_option["stops"][:]
                my_stops.append(index + 1)
                news.append({"price":new_price, "dist":curr_option["dist"], "gas":max_tank, "stops":my_stops[:]})
            else:
                delete.append(ind)
                
        for i in delete:
            del options[i]
        options += news
    legit = []
    for opt in options:
        # check if it has enough to reach the end
        if(opt["dist"] + opt["gas"] >= road_len):
            legit.append(opt)


    print(min(legit, key=lambda x: (x["price"], len(x["stops"])))["stops"])

if __name__ == "__main__":
    solve()