import sys
from Computer import Computer


puzzle_input = list(map(int, open(sys.argv[1]).read().split(",")))
computer = Computer(puzzle_input)
computer.run()
