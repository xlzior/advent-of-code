import re
import sys


def create_row(row):
    return list(map(lambda x: BingoCell(int(x)), re.split('\s+', row.strip())))


class BingoCell:
    def __init__(self, number):
        self.number = number
        self.marked = False

    def mark_number(self, number):
        if self.number == number:
            self.marked = True


class BingoBoard:
    def __init__(self, raw_board):
        self.won = False
        self.board = list(map(create_row, raw_board))
        assert len(self.board) == 5
        assert len(self.board[0]) == 5

    def is_bingo(self):
        for row_index in range(5):
            if all([cell.marked for cell in self.board[row_index]]):
                return True

        for col_index in range(5):
            if all([self.board[i][col_index].marked for i in range(5)]):
                return True

        return False

    def mark_number(self, number):
        if self.won:  # ignore new numbers if already cleared
            return

        [cell.mark_number(number) for row in self.board for cell in row]
        if self.is_bingo():
            self.won = True
            all_sum = sum(0 if cell.marked else cell.number for row in self.board for cell in row)
            print(f"{number} * {all_sum} = {number * all_sum}")


with open(sys.argv[1]) as file:
    lines = file.readlines()

numbers = list(map(int, lines[0].split(',')))
print(f"{len(numbers)} numbers")

num_of_bingo_boards = (len(lines) - 1) // 6
print(f"{num_of_bingo_boards} bingo boards")

boards = [BingoBoard(lines[6 * i + 2:6 * i + 7]) for i in range(num_of_bingo_boards)]
[board.mark_number(num) for num in numbers for board in boards]
