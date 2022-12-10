import sys


def find_marker(packet, n):
    return (
        next(
            filter(
                lambda x: len(set(x[1])) == len(x[1]),
                enumerate(packet[i : i + n] for i in range(len(packet) - n + 1)),
            )
        )[0]
        + n
    )


test_cases = [
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
