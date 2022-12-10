import sys
from collections import Counter


def find_marker(packet, n):
    counter = Counter()
    num_unique = 0
    left = 0

    for i, c in enumerate(packet):
        if i >= n:
            # pop the leftmost character
            counter[packet[left]] -= 1
            if counter[packet[left]] == 0:
                num_unique -= 1
            left += 1

        # push the rightmost character
        counter[c] += 1
        if counter[c] == 1:
            num_unique += 1
        if num_unique == n:
            return left + n


test_cases = [
    ("abcdefghijklmnopqrstuvwxyz", 4, 14),
    ("mjqjpqmgbljsphdztnvjfqwrcgsmlb", 7, 19),
    ("bvwbjplbgvbhsrlpgdmjqwftvncz", 5, 23),
    ("nppdvjthqldpwncqszvftbrmjlhg", 6, 23),
    ("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg", 10, 29),
    ("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw", 11, 26),
]

for test_case, part_1, part_2 in test_cases:
    assert find_marker(test_case, 4) == part_1
    assert find_marker(test_case, 14) == part_2

puzzle_input = open(sys.argv[1]).read()
print("Part 1:", find_marker(puzzle_input, 4))
print("Part 2:", find_marker(puzzle_input, 14))
