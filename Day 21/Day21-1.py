class Player:
    def __init__(self, position):
        self.position = position
        self.score = 0
        self.won = False

    def move(self, steps):
        self.position = (self.position - 1 + steps) % 10 + 1
        self.score += self.position
        self.won = self.score >= 1000

class Dice:
    def __init__(self):
        self.number = 0
        self.count = 0

    def roll(self):
        self.number = (self.number % 10) + 1
        self.count += 1
        return self.number

    def roll3(self):
        return self.roll() + self.roll() + self.roll()


starting_positions = [4, 8]
players = [Player(position) for position in starting_positions]
dice = Dice()

game_over = False

while not game_over:
    for player in players:
        player.move(dice.roll3())
        if player.won:
            game_over = True
            break

losing_player = list(filter(lambda player: not player.won, players))[0]
print(f"Part 1: {losing_player.score} * {dice.count} = {losing_player.score * dice.count}")
