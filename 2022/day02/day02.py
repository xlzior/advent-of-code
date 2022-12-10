import sys

LETTER_TO_INDEX = {"A": 0, "B": 1, "C": 2, "X": 0, "Y": 1, "Z": 2}
MY_MOVE_MATRIX = [[2, 0, 1], [0, 1, 2], [1, 2, 0]]
OUTCOME_SCORE_MATRIX = [[3, 6, 0], [0, 3, 6], [6, 0, 3]]

puzzle_input = open(sys.argv[1]).read().strip().split("\n")
part_1_score = 0
part_2_score = 0

for line in puzzle_input:
    opponent_move, mystery = (LETTER_TO_INDEX[letter] for letter in line.split(" "))
    my_move = mystery
    part_1_score += my_move + 1
    part_1_score += OUTCOME_SCORE_MATRIX[opponent_move][my_move]

    my_move = MY_MOVE_MATRIX[opponent_move][mystery]
    part_2_score += my_move + 1
    part_2_score += OUTCOME_SCORE_MATRIX[opponent_move][my_move]

print("Part 1:", part_1_score)
print("Part 2:", part_2_score)
