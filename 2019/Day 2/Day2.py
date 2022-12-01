import sys
from Computer import Computer

puzzle_input = list(map(int, open(sys.argv[1]).read().split(",")))

# Part 1
part_1_program = puzzle_input.copy()
part_1_program[1] = 12
part_1_program[2] = 2

computer = Computer(part_1_program)
computer.run()
print("Part 1:", computer.program[0])

# Part 2
for noun in range(0, 100):
    for verb in range(0, 100):
        part_2_program = puzzle_input.copy()
        part_2_program[1] = noun
        part_2_program[2] = verb
        computer = Computer(part_2_program)
        computer.run()
        if computer.program[0] == 19690720:
            print("Part 2:", 100 * noun + verb)
