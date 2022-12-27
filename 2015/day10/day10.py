def look_and_say(seq):
    digit = ""
    count = 0
    result = []
    for c in seq:
        if digit == c:
            count += 1
        else:
            result.append(f"{count}{digit}")
            digit = c
            count = 1
    result.append(f"{count}{digit}")
    return "".join(result).lstrip("0")


seq = open("puzzle.txt").read()

for _ in range(40):
    seq = look_and_say(seq)
print("Part 1:", len(seq))

for _ in range(10):
    seq = look_and_say(seq)
print("Part 2:", len(seq))
