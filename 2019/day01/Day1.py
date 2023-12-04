import sys

def calculate_fuel_1(mass):
    return mass // 3 - 2

def calculate_fuel_2(mass):
    fuel = calculate_fuel_1(mass)
    return fuel + calculate_fuel_2(fuel) if fuel > 0 else 0


puzzle_input = list(map(int, open(sys.argv[1]).read().strip().split("\n")))
print("Part 1:", sum(map(calculate_fuel_1, puzzle_input)))
print("Part 2:", sum(map(calculate_fuel_2, puzzle_input)))

# one-liner part 1
# print(sum(map(lambda x: int(x) // 3 - 2, open("input.txt").read().strip().split("\n"))))
