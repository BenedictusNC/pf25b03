package Main;

import java.util.Scanner;

import static Main.Board.COLS;
import static Main.Board.ROWS;
import static Main.Seed.*;
import static Main.State.PLAYING;

/**
 * The main class for the Tic-Tac-Toe (Console-OO, non-graphics version)
 * It acts as the overall controller of the game.
 */
public class GameMain {
    // Define properties
    /**
     * The game board
     */
    private static Board board;
    /**
     * The current state of the game (of enum State)
     */
    private static State currentState;
    /**
     * The current player (of enum Seed)
     */
    private static Seed currentPlayer;

    private static Scanner in = new Scanner(System.in);

    /**
     * Constructor to setup the game
     */
    public GameMain() {
        // Perform one-time initialization tasks
        initGame();
        do {
            // Reset the board, currentStatus and currentPlayer
            newGame();

            // Play the game once
            do {
                // The currentPlayer makes a move.
                // Update cells[][] and currentState
                stepGame();
                // Refresh the display
                board.paint();
                // Print message if game over
                if (currentState == State.CROSS_WON) {
                    System.out.println("'X' won!\nBye!");
                } else if (currentState == State.NOUGHT_WON) {
                    System.out.println("'O' won!\nBye!");
                } else if (currentState == State.DRAW) {
                    System.out.println("It's Draw!\nBye!");
                }
                // Switch currentPlayer
                currentPlayer = (currentPlayer == CROSS) ? NOUGHT : CROSS;
            } while (currentState == PLAYING);  // repeat until game over
            boolean invalid = false;
            do {
                System.out.print("Play again (y/n)? ");
                char ans = in.next().charAt(0);
                if (ans == 'n' || ans == 'N') {
                    System.out.println("Bye!");
                    System.exit(0);  // terminate the program
                } else if (ans == 'y' || ans == 'Y') {
                    invalid = true;
                } else {
                    System.out.println("Invalid input, try again!");
                }
            } while (!invalid);
        } while (true);
    }

    public static void initGame() {
        board = new Board();
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                board [row][col] = NO_SEED;  // all cells empty
            }
        }
        // Roulette system: randomly choose CROSS or NOUGHT as the first player
        currentPlayer = (Math.random() < 0.5) ? CROSS : NOUGHT;

        currentState = PLAYING; // ready to play

        // Display who starts first
        if (currentPlayer == CROSS) {
            System.out.println("Roulette decided: Player 'X' starts first!");
        } else {
            System.out.println("Roulette decided: Player 'O' starts first!");
        }
    }

    /** Reset the game-board contents and the current states, ready for new game */
    public void newGame() {
        board.newGame();  // clear the board contents
        currentPlayer = CROSS;   // CROSS plays first
        currentState = PLAYING; // ready to play
    }

    /** The currentPlayer makes one move.
     Update cells[][] and currentState. */
    public void stepGame() {
        boolean validInput = false;  // for validating input
        do {
            String icon = currentPlayer.getIcon();
            System.out.print("Player '" + icon + "', enter your move (row[1-3] column[1-3]): ");
            int row = in.nextInt() - 1;   // [0-2]
            int col = in.nextInt() - 1;
            if (row >= 0 && row < ROWS && col >= 0 && col < COLS
                    && board.cells[row][col].content == NO_SEED) {
                // Update cells[][] and return the new game state after the move
                currentState = board.stepGame(currentPlayer, row, col);
                validInput = true; // input okay, exit loop
            } else {
                System.out.println("This move at (" + (row + 1) + "," + (col + 1)
                        + ") is not valid. Try again...");
            }
        } while (!validInput);   // repeat until input is valid
    }

    /** The entry main() method */
    public static void main(String[] args) {
        new GameMain();  // Let the constructor do the job
    }
}