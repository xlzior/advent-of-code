import collections
import sys


standardised_map = {  # map from standardised format to number
    '467889': 0, '89': 1, '47788': 2, '77889': 3, '6789': 4,
    '67789': 5, '467789': 6, '889': 7, '4677889': 8, '677889': 9
}


def sort_string(s):
    return "".join(list(map(str, sorted(s))))


def standardise(pattern, segment_frequencies):  # replace each letter with its frequency and sort
    key = sort_string([segment_frequencies[letter] for letter in pattern])
    return standardised_map[key]


def decipher(line):
    [patterns, outputs] = map(lambda x: x.split(" "), line.strip().split(" | "))
    sorted_patterns = map(sort_string, patterns)
    segment_frequencies = collections.Counter("".join(patterns))
    corresponding_numbers = [standardise(pattern, segment_frequencies) for pattern in patterns]
    pattern_map = dict(zip(sorted_patterns, corresponding_numbers))

    return int("".join([str(pattern_map[sort_string(output)]) for output in outputs]))


with open(sys.argv[1]) as file:
    print(sum(decipher(line) for line in file.readlines()))
