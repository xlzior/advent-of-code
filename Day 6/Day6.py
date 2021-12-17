initial_population = list(map(int, input().split(",")))
fish_counts = [initial_population.count(i) for i in range(9)]  # 9 possible timer values from 0 to 8
index = 0

for _ in range(256):
    adults_index = (index + 7) % 9  # move the adults based on a cycle of 7
    fish_counts[adults_index] += fish_counts[index]
    index = (index + 1) % 9

print(sum(fish_counts))
