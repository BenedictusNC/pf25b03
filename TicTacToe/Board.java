package TicTacToe;
import java.awt.*;
import javax.swing.*;
/**
 * The Board class models the ROWS-by-COLS game board.
 */
public class Board {
    // Define named constants
    public static final int ROWS = 3;  // ROWS x COLS cells
    public static final int COLS = 3;
    // Define named constants for drawing
    public static final int CANVAS_WIDTH = Cell.SIZE * COLS;  // the drawing canvas
    public static final int CANVAS_HEIGHT = Cell.SIZE * ROWS;
    public static final int GRID_WIDTH = 8;  // Grid-line's width
    public static final int GRID_WIDTH_HALF = GRID_WIDTH / 2; // Grid-line's half-width
    public static final Color COLOR_GRID = new Color(41, 25, 13);;  // grid lines
    public static final int Y_OFFSET = 1;  // Fine tune for better display

    private Image bgImage;
    // Define properties (package-visible)
    /** Composes of 2D array of ROWS-by-COLS Cell instances */
    Cell[][] cells;

    /** Constructor to initialize the game board */
    public Board() {
        initGame();
        bgImage = new ImageIcon(getClass().getResource("/image/crafting22.png")).getImage();
    }

    private int winType = -1;

    /** Initialize the game objects (run once) */
    public void initGame() {
        cells = new Cell[ROWS][COLS]; // allocate the array
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                // Allocate element of the array
                cells[row][col] = new Cell(row, col);
                // Cells are initialized in the constructor
            }
        }
    }

    /** Reset the game board, ready for new game */
    public void newGame() {
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                cells[row][col].newGame(); // clear the cell content
            }
        }
        winType = -1;
    }

    /**
     *  The given player makes a move on (selectedRow, selectedCol).
     *  Update cells[selectedRow][selectedCol]. Compute and return the
     *  new game state (PLAYING, DRAW, CROSS_WON, NOUGHT_WON).
     */
    public State stepGame(Seed player, int selectedRow, int selectedCol) {
        cells[selectedRow][selectedCol].content = player;

        // Cek baris
        for (int row = 0; row < ROWS; row++) {
            if (cells[row][0].content == player && cells[row][1].content == player && cells[row][2].content == player) {
                winType = row;  // 0–2 untuk baris
                SoundEffect.DIE.play();
                return (player == Seed.CROSS) ? State.CROSS_WON : State.NOUGHT_WON;
            }
        }

        // Cek kolom
        for (int col = 0; col < COLS; col++) {
            if (cells[0][col].content == player && cells[1][col].content == player && cells[2][col].content == player) {
                winType = 3 + col;  // 3–5 untuk kolom
                SoundEffect.DIE.play();
                return (player == Seed.CROSS) ? State.CROSS_WON : State.NOUGHT_WON;
            }
        }

        // Cek diagonal ↘
        if (cells[0][0].content == player && cells[1][1].content == player && cells[2][2].content == player) {
            winType = 6;
            SoundEffect.DIE.play();
            return (player == Seed.CROSS) ? State.CROSS_WON : State.NOUGHT_WON;
        }

        // Cek diagonal ↙
        if (cells[0][2].content == player && cells[1][1].content == player && cells[2][0].content == player) {
            winType = 7;
            SoundEffect.DIE.play();
            return (player == Seed.CROSS) ? State.CROSS_WON : State.NOUGHT_WON;
        }

        // Cek apakah masih ada cell kosong
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                if (cells[row][col].content == Seed.NO_SEED) {
                    return State.PLAYING;
                }
            }
        }

        winType = -1;  // Tidak ada pemenang
        return State.DRAW;
    }

    /** Check if the given player has won */
    public boolean hasWon(Seed player) {
        // Check for 3-in-a-row
        for (int row = 0; row < ROWS; ++row) {
            if (cells[row][0].content == player && cells[row][1].content == player && cells[row][2].content == player) {
                return true;
            }
        }

        // Check for 3-in-a-column
        for (int col = 0; col < COLS; ++col) {
            if (cells[0][col].content == player && cells[1][col].content == player && cells[2][col].content == player) {
                return true;
            }
        }

        // Check for 3-in-a-diagonal
        if (cells[0][0].content == player && cells[1][1].content == player && cells[2][2].content == player) {
            return true;
        }

        // Check for 3-in-a-opposite-diagonal
        if (cells[0][2].content == player && cells[1][1].content == player && cells[2][0].content == player) {
            return true;
        }

        return false;
    }

    /** Paint itself on the graphics canvas, given the Graphics context */
    public void paint(Graphics g) {
        if (bgImage != null) {
            g.drawImage(bgImage, 0, 0, CANVAS_WIDTH, CANVAS_HEIGHT, null);
        }
        // Draw the grid-lines
        g.setColor(COLOR_GRID);
        for (int row = 1; row < ROWS; ++row) {
            g.fillRoundRect(0, Cell.SIZE * row - GRID_WIDTH_HALF,
                    CANVAS_WIDTH - 1, GRID_WIDTH,
                    GRID_WIDTH, GRID_WIDTH);
        }
        for (int col = 1; col < COLS; ++col) {
            g.fillRoundRect(Cell.SIZE * col - GRID_WIDTH_HALF, 0 + Y_OFFSET,
                    GRID_WIDTH, CANVAS_HEIGHT - 1,
                    GRID_WIDTH, GRID_WIDTH);
        }

        // Draw all the cells
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                cells[row][col].paint(g);  // ask the cell to paint itself
            }
        }
        if (winType != -1) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.GREEN);
            g2.setStroke(new BasicStroke(6));

            int padding = Cell.SIZE / 2;
            int x1 = 0, y1 = 0, x2 = 0, y2 = 0;

            switch (winType) {
                case 0: case 1: case 2: // baris
                    y1 = y2 = winType * Cell.SIZE + padding;
                    x1 = 0;
                    x2 = CANVAS_WIDTH;
                    break;
                case 3: case 4: case 5: // kolom
                    x1 = x2 = (winType - 3) * Cell.SIZE + padding;
                    y1 = 0;
                    y2 = CANVAS_HEIGHT;
                    break;
                case 6: // diagonal kiri atas ke kanan bawah
                    x1 = y1 = 0;
                    x2 = CANVAS_WIDTH;
                    y2 = CANVAS_HEIGHT;
                    break;
                case 7: // diagonal kanan atas ke kiri bawah
                    x1 = CANVAS_WIDTH;
                    y1 = 0;
                    x2 = 0;
                    y2 = CANVAS_HEIGHT;
                    break;
            }
            g2.drawLine(x1, y1, x2, y2);
        }
    }
}