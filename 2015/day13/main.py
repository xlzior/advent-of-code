from itertools import permutations
import re
import sys


lines = [
    re.match(
        r"(\w+) would (gain|lose) (\d+) happiness units by sitting next to (\w+).", line
    ).groups()
    for line in open(sys.argv[1]).read().split("\n")
]
deltas = {
    (first, second): int(amount) * (1 if direction == "gain" else -1)
    for first, direction, amount, second in lines
}
names = {line[0] for line in lines}


def get_happiness_score(perm):
    score = 0
    perm.append(perm[0])
    for i in range(len(perm) - 1):
        a, b = perm[i : i + 2]
        score += deltas.get((a, b), 0)
        score += deltas.get((b, a), 0)
    return score


part1 = max(get_happiness_score(list(perm)) for perm in permutations(names))
print("Part 1:", part1)

names.add("me")
part2 = max(get_happiness_score(list(perm)) for perm in permutations(names))
print("Part 2:", part2)
