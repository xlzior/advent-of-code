def check_double(password):
    string = str(password)
    for i in range(len(string) - 1):
        if string[i] == string[i + 1]:
            return True
    return False

def check_never_decreasing(password):
    right = password % 10
    password //= 10
    while password > 0:
        left = password % 10
        if left > right:
            return False
        password //= 10
        right = left
    return True

def check_requirements(password):
    return check_double(password) and check_never_decreasing(password)


assert not check_never_decreasing(223450)
assert not check_double(123789)
assert check_requirements(111111)

count = 0
for i in range(372304, 847060 + 1):
    valid = check_requirements(i)
    count += 1 if valid else 0

print(count)
