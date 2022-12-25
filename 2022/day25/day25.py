import sys


digit_to_decimal = {"2": 2, "1": 1, "0": 0, "-": -1, "=": -2}
decimal_to_digit = dict(map(reversed, digit_to_decimal.items()))


def snafu_to_decimal(snafu):
    result = 0
    for i, x in enumerate(snafu, -len(snafu) + 1):
        result += 5 ** (-i) * digit_to_decimal[x]
    return result


def decimal_to_snafu(decimal):
    result = []
    while decimal > 0:
        remainder, decimal = decimal % 5, decimal // 5
        result.append(remainder)
    result.append(0)
    result.reverse()

    while any(digit > 2 for digit in result):
        for i, d in filter(lambda x: x[1] > 2, enumerate(result)):
            result[i - 1] += 1
            result[i] = d - 5

    return "".join(decimal_to_digit[x] for x in result).lstrip("0")


puzzle_input = open(sys.argv[-1]).read().split("\n")
print("Part 1:", decimal_to_snafu(sum(map(snafu_to_decimal, puzzle_input))))
