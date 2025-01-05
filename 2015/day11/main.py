import sys


password = [ord(c) - ord("a") for c in open(sys.argv[1]).read()]


def to_text(codes):
    return "".join(chr(c + ord("a")) for c in codes)


def increment(codes):
    i = len(codes) - 1
    codes[i] = (codes[i] + 1) % 26
    while codes[i] == 0:
        i -= 1
        codes[i] = (codes[i] + 1) % 26
    return codes


def contains_straight(codes):
    for i in range(len(codes) - 2):
        if codes[i] + 1 == codes[i + 1] and codes[i + 1] + 1 == codes[i + 2]:
            return True
    return False


def contains_non_overlapping_pair(codes):
    pairs = set()
    i = 0
    while i < len(codes) - 1:
        if codes[i] == codes[i + 1]:
            pairs.add(codes[i])
            i += 1
        i += 1
    return len(pairs) >= 2


def contains_illegal(codes):
    return any(c in illegal for c in codes)


def is_valid(codes):
    if contains_illegal(codes):
        return False

    if not contains_straight(codes):
        return False

    if not contains_non_overlapping_pair(codes):
        return False

    return True


illegal = [ord(c) - ord("a") for c in "iol"]

parts_done = 0
while parts_done < 2:
    increment(password)
    if is_valid(password):
        parts_done += 1
        print("Part %d:" % parts_done, to_text(password))
