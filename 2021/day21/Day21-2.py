def move(player, steps):
    start, score = player
    end = (start - 1 + steps) % 10 + 1
    return end, score + end


def simulate(player1, player2, turn, universes_in_group):
    if player1[1] >= WINNING_SCORE:
        winner_count[1] += universes_in_group
    elif player2[1] >= WINNING_SCORE:
        winner_count[2] += universes_in_group
    elif turn == 1:
        for roll in range(3, 10):  # 3 rolls of a 3-sided dice -> sum ranges from 3 to 9
            simulate(move(player1, roll), player2, 2, universes_in_group * DICE_CHANCES[roll])
    elif turn == 2:
        for roll in range(3, 10):
            simulate(player1, move(player2, roll), 1, universes_in_group * DICE_CHANCES[roll])


DICE_CHANCES = [0, 0, 0, 1, 3, 6, 7, 6, 3, 1]  # there are DICE_CHANCES[i] ways of rolling 3 dice to get a sum of i
winner_count = [0, 0, 0]  # 1-based indexing, ignore index 0
WINNING_SCORE = 21
starting_positions = [4, 8]

simulate((starting_positions[0], 0), (starting_positions[1], 0), 1, 1)

print("Ending points:", winner_count[1:])
print("Part 2:", max(winner_count))
