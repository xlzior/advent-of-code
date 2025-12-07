from functools import cache
import math
import sys
import re


def replace(medicine, match, new):
    return medicine[: match.start()] + new + medicine[match.end() :]


raw_input = "".join(open(sys.argv[1]).readlines())
replacements, medicine = raw_input.split("\n\n")
replacements = sorted([tuple(r.split(" => ")) for r in replacements.split("\n")])

molecules = set()
for before, after in replacements:
    matches = re.finditer(f"({before})", medicine)
    for m in matches:
        molecules.add(replace(medicine, m, after))

print("Part 1:", len(molecules))


seen = set()


@cache
def get_num_steps(medicine, target):
    seen.add(medicine)
    if medicine == target:
        return 0

    num_steps = []
    for before, after in replacements:
        matches = re.finditer(f"({after})", medicine)
        for m in matches:
            molecule = replace(medicine, m, before)
            num_steps.append(get_num_steps(molecule, target) + 1)

    result = min(num_steps) if num_steps else math.inf
    return result


def count_atoms(medicine):
    return len([c for c in medicine if c.upper() == c])


def split_atoms(medicine):
    atom_starts = [i for (i, c) in enumerate(medicine) if c.upper() == c]
    atom_starts.append(len(medicine))
    return [
        medicine[atom_starts[i] : atom_starts[i + 1]]
        for i in range(len(atom_starts) - 1)
    ]


# print("Part 2:", get_num_steps(medicine, "e"))


@cache
def eliminate_argon(medicine):
    shrinks = set()
    if count_atoms(medicine) == 1:
        return medicine
    for before, after in replacements:
        matches = re.finditer(f"({after})", medicine)
        for m in matches:
            molecule = replace(medicine, m, before)
            if "Ar" in molecule:
                shrinks.update(
                    [(mol, cost + 1) for mol, cost in eliminate_argon(molecule)]
                )
            else:
                shrinks.add((molecule, 1))
    return shrinks


@cache
def algorithm(medicine):
    if "Ar" not in medicine:
        print("no argon found?", medicine)
        return [(medicine, 0)]

    segment, rest = medicine.split("Ar", 1)
    segment += "Ar"

    no_argons = eliminate_argon(segment)
    results = []
    for replacement, cost in no_argons:
        subresults = algorithm(replacement + rest)
        results.extend((result, cost + subcost) for result, subcost in subresults)
    return results


results = algorithm(medicine)
medicine, cost = results[0]
print(medicine, cost)
