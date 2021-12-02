depth = 0
horizontal = 0
aim = 0

for i in range(1000):
    [command, amount] = input().split(" ")
    amount = int(amount)

    if command == "forward":
        depth += aim * amount
        horizontal += amount
    if command == "down":
        aim += amount
    if command == "up":
        aim -= amount

print(f"depth: {depth}; horizontal: {horizontal}")
print(depth * horizontal)