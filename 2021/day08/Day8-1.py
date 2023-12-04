import collections
import sys

with open(sys.argv[1]) as file:
    lines = [line.strip() for line in file.readlines()]


def find_1478(line):
    notable_values = [2, 4, 3, 7]  # 1 4 7 8 respectively
    outputs = line.split(" | ")[1].split(" ")
    output_lengths = list(map(len, outputs))
    counter = collections.Counter(output_lengths)
    return sum(counter[i] for i in notable_values)


print(sum(find_1478(line) for line in lines))
