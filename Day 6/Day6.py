initial_population = list(map(int, input().split(",")))
fish_counts = [0] * 9  # 9 possible timer values from 0 to 8
index = 0

for fish in initial_population:
    fish_counts[fish] += 1

for i in range(80):
    babies_index = (index - 1 + 8) % 9  # -1 to go to the previous group, (...+8)%9 is to wraparound
    fish_counts[babies_index] += fish_counts[index]
    index = (index + 1) % 9

print(sum(fish_counts))
