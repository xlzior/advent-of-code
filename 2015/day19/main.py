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
