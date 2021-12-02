depth = 0
horizontal = 0

for i in range(1000):
    [command, amount] = input().split(" ")
    amount = int(amount)

    if command == "forward":
        depth += amount
    if command == "down":
        horizontal += amount
    if command == "up":
        horizontal -= amount

print(f"depth: {depth}; horizontal: {horizontal}")
print(depth * horizontal)