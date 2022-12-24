def parse(line):
    equals_to, output_wire = line.split(" -> ", 1)
    equals_to = equals_to.split(" ")
    return output_wire, equals_to


def get(key):
    if key in cache:
        return cache[key]

    if key not in wires:
        value = int(key)
    else:
        equals_to = wires[key]
        if len(equals_to) == 1:
            value = get(equals_to[0])
        elif len(equals_to) == 2:
            if "NOT" in equals_to:
                value = ~get(equals_to[1])
        elif len(equals_to) == 3:
            if "AND" in equals_to:
                value = get(equals_to[0]) & get(equals_to[2])
            elif "OR" in equals_to:
                value = get(equals_to[0]) | get(equals_to[2])
            elif "LSHIFT" in equals_to:
                value = get(equals_to[0]) << get(equals_to[2])
            elif "RSHIFT" in equals_to:
                value = get(equals_to[0]) >> get(equals_to[2])

    cache[key] = value % 2**16
    return cache[key]


puzzle_input = open("puzzle.txt").read().split("\n")
wires = dict(map(parse, puzzle_input))
cache = dict()
part_1 = get("a")
print("Part 1:", part_1)

cache = {"b": part_1}
print("Part 2:", get("a"))
