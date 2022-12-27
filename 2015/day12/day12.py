import re, json


def elf_sum(obj):
    if isinstance(obj, int):
        return obj
    if isinstance(obj, str):
        return 0
    if isinstance(obj, list):
        return sum(elf_sum(v) for v in obj)
    if "red" in obj.values():
        return 0
    return sum(elf_sum(v) for v in obj.values())


puzzle_input = open("puzzle.txt").read()
print("Part 1:", sum(map(int, re.findall(r"-?\d+", puzzle_input))))
print("Part 2:", elf_sum(json.loads(puzzle_input)))
