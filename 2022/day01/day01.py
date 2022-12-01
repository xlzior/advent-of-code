import sys

puzzle_input = open(sys.argv[1]).read().strip().split("\n\n")
elves = [list(map(int, elf.split("\n"))) for elf in puzzle_input]
elf_sums = list(map(sum, elves))

print("Part 1:", max(elf_sums))
print("Part 2:", sum(sorted(elf_sums, reverse=True)[:3]))
