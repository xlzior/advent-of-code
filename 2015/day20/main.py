from functools import cache
import math


target = int(open("puzzle.in").read())


@cache
def get_factors(n):
    factors = list()
    i = 1
    while i <= math.sqrt(n):
        if n % i == 0:
            factors.append(i)
            if i != (n // i):
                factors.append(n // i)
        i += 1
    return factors


def count_presents_1(n):
    return 10 * sum(get_factors(n))


def count_presents_2(n):
    factors = get_factors(n)
    return 11 * sum(n // e for e in factors if e < 50)


assert (x := count_presents_1(1)) == 10, x
assert (x := count_presents_1(2)) == 30, x
assert (x := count_presents_1(3)) == 40, x
assert (x := count_presents_1(4)) == 70, x
assert (x := count_presents_1(5)) == 60, x
assert (x := count_presents_1(6)) == 120, x
assert (x := count_presents_1(7)) == 80, x
assert (x := count_presents_1(8)) == 150, x
assert (x := count_presents_1(9)) == 130, x


parts_done = 0
i = 1

while parts_done < 2:
    p1 = count_presents_1(i)
    p2 = count_presents_2(i)
    if p1 >= target:
        print("Part 1:", i)
        parts_done += 1
    if p2 >= target:
        print("Part 2:", i)
        parts_done += 1
    i += 1
