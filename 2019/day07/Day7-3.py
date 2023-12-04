import sys
from itertools import permutations
from Computer import Computer

puzzle_input = list(map(int, open(sys.argv[1]).read().split(",")))

possible_outputs = list()
for phase_settings in permutations(range(5)):
    output = 0
    for i in range(5):
        computer = Computer(puzzle_input.copy(), [phase_settings[i], output])
        output = computer.run()

    possible_outputs.append(output)

print("Part 1:", max(possible_outputs))

possible_outputs = list()
for phase_settings in permutations(range(5, 10)):
    amplifiers = [Computer(puzzle_input.copy(), [phase_settings[i]]) for i in range(5)]
    output = 0
    done = False
    while not done:
        for i in range(5):
            output = amplifiers[i].resume([output])
        done = amplifiers[4].state == Computer.HALTED

    possible_outputs.append(output)

print("Part 2:", max(possible_outputs))
