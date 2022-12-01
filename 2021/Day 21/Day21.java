class Player {
    int position;
    int score;

    Player(int position, int score) {
        this.position = position;
        this.score = score;
    }

    public Player move(int steps) {
        int newPosition = (position - 1 + steps) % 10 + 1;
        return new Player(newPosition, score + newPosition);
    }
}

public class Day21 {
    private final int[] DICE_CHANCES = {0, 0, 0, 1, 3, 6, 7, 6, 3, 1};
    private final long[] winnerCount = {0, 0, 0};

    private void simulate(Player player1, Player player2, int turn, long universes) {
        int WINNING_SCORE = 21;
        if (player1.score >= WINNING_SCORE) {
            winnerCount[1] += universes;
        } else if (player2.score >= WINNING_SCORE) {
            winnerCount[2] += universes;
        } else if (turn == 1) {
            for (int i = 3; i < 10; i++) {
                simulate(player1.move(i), player2, 2, universes * DICE_CHANCES[i]);
            }
        } else if (turn == 2) {
            for (int i = 3; i < 10; i++) {
                simulate(player1, player2.move(i), 1, universes * DICE_CHANCES[i]);
            }
        }
    }

    private void run() {
        simulate(new Player(4, 0), new Player(8, 0), 1, 1);
        System.out.printf("[%d, %d]%n", winnerCount[1], winnerCount[2]);
        System.out.println(Math.max(winnerCount[1], winnerCount[2]));
    }

    public static void main(String[] args) {
        Day21 runner = new Day21();
        runner.run();
    }
}
