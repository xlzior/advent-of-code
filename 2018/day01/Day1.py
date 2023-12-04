with open("input.txt") as file:
    sequence = [int(line) for line in file.readlines()]

value = 0
frequencies_reached = set()
frequencies_reached.add(value)

is_part2_done = False

for num in sequence:
    value += num
    if value in frequencies_reached:
        is_part2_done = True
    else:
        frequencies_reached.add(value)

print("Part 1:", value)

while not is_part2_done:
    for num in sequence:
        value += num
        if value in frequencies_reached:
            is_part2_done = True
            break
        else:
            frequencies_reached.add(value)

print("Part 2:", value)
