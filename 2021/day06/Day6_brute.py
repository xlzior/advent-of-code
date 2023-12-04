class Lanternfish:
    def __init__(self, timer):
        self.timer = timer

    def next_day(self):
        self.timer -= 1
        if self.timer < 0:
            self.timer = 6
            return True

    def __str__(self):
        return str(self.timer)


timers = list(map(int, input().split(",")))
fishes = [Lanternfish(timer) for timer in timers]

num_days = 80
for i in range(num_days):
    for fish in fishes:
        if fish.next_day():
            fishes.append(Lanternfish(9))
    # print(i, ":", " ".join(list(map(str, fishes))))
print(len(fishes))

