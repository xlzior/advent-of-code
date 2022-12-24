bad_strings = ["ab", "cd", "pq", "xy"]
vowels = {"a", "e", "i", "o", "u"}
is_double_letter = lambda s: s[0] == s[1]
is_wing_letter = lambda s: s[0] == s[2]


def overlapping_groups(string, n):
    return [string[i : i + n] for i in range(len(string) - n + 1)]


def is_nice_1(string):
    if len(list(filter(lambda c: c in vowels, string))) < 3:
        return False
    if not any(map(is_double_letter, overlapping_groups(string, 2))):
        return False
    if any(s in string for s in bad_strings):
        return False
    return True


def is_nice_2(string):
    if not any(map(is_wing_letter, overlapping_groups(string, 3))):
        return False

    pairs = overlapping_groups(string, 2)
    return any(p in pairs[i + 2 :] for i, p in enumerate(pairs))


strings = open("puzzle.txt").read().split("\n")

print("Part 1:", len(list(filter(is_nice_1, strings))))
print("Part 2:", len(list(filter(is_nice_2, strings))))
