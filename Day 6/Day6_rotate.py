initial_population = list(map(int, input().split(",")))
fish_counts = [initial_population.count(i) for i in range(9)]  # 9 possible timer values from 0 to 8

for _ in range(256):
    num_pregnant_adults = fish_counts[0]
    fish_counts = fish_counts[1:] + fish_counts[:1]  # rotate the fish_counts to create babies with a timer of 8
    fish_counts[6] += num_pregnant_adults            # reset the timer of pregnant adults based on cycle of 7

print(sum(fish_counts))
