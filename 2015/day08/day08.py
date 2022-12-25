def len_in_memory(string):
    string = string.strip('"')
    count = 0
    i = 0
    while i < len(string):
        if string[i] != "\\":
            i += 1
        elif i + 1 < len(string) and string[i + 1] == "x":
            i += 4
        else:
            i += 2
        count += 1
    return count


files = open("puzzle.txt").read().split("\n")
num_chars_in_string = sum(map(len, files))
num_chars_in_memory = sum(map(len_in_memory, files))
print("Part 1:", num_chars_in_string - num_chars_in_memory)
print("Part 2:", sum(file.count("\\") + file.count('"') + 2 for file in files))
