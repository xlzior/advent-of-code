import collections
import sys

with open(sys.argv[1]) as file:
    lines = [line.strip() for line in file.readlines()]

complete = collections.Counter("abcdefg")


def find_missing(s):
    return list(complete - collections.Counter(s))


def decipher_one_line(line):
    by_length = collections.defaultdict(list)
    dictionary = dict()

    [ten_unique, outputs] = line.split(" | ")
    for pattern in ten_unique.split(" "):
        by_length[len(pattern)].append("".join(sorted(pattern)))

    # easy, unique ones
    dictionary[by_length[2][0]] = 1
    dictionary[by_length[4][0]] = 4
    dictionary[by_length[3][0]] = 7
    dictionary[by_length[7][0]] = 8

    all_missing_counter = collections.Counter()
    [all_missing_counter.update(find_missing(s)) for s in by_length[5] + by_length[6]]

    # 0, 6, 9 have 6 segments
    candidates_for_6 = by_length[6].copy()
    for pattern in by_length[6]:
        if pattern not in dictionary:
            dictionary[pattern] = 6
        for letter in find_missing(pattern):
            if all_missing_counter[letter] == 1:  # 0 is missing middle, which no other numbers are missing
                candidates_for_6.remove(pattern)
                dictionary[pattern] = 0
            if all_missing_counter[letter] == 3:  # 9 is missing bottom left, and so are 2 and 5 for a total of 3
                candidates_for_6.remove(pattern)
                dictionary[pattern] = 9
    top_right_segment = find_missing(candidates_for_6[0])[0]  # use 6 to identify the top right segment

    # 2, 3, 5 have 5 segments
    for pattern in by_length[5]:
        if pattern not in dictionary:
            dictionary[pattern] = 3
        for letter in find_missing(pattern):
            if all_missing_counter[letter] == 1:  # 2 is missing bottom right, which no other numbers are missing
                dictionary[pattern] = 2
            if letter == top_right_segment:  # 5 is the top right segment which was identified previously using 6
                dictionary[pattern] = 5

    # map to a 4-digit number
    result = 0
    for output in outputs.split(" "):
        result = result * 10 + dictionary["".join(sorted(output))]
    return result


print(sum(decipher_one_line(line) for line in lines))
