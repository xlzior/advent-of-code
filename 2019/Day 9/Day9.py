import sys
from Computer import Computer

puzzle_input = list(map(int, open(sys.argv[1]).read().split(",")))

print("Part 1:", Computer(puzzle_input, [1]).run())
print("Part 2:", Computer(puzzle_input, [2]).run())
