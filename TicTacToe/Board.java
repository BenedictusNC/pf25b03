package TicTacToe;
import java.awt.*;
import javax.swing.*;

public class Board {
    public static final int ROWS = 3;
    public static final int COLS = 3;
    public static final int CANVAS_WIDTH = Cell.SIZE * COLS;
    public static final int CANVAS_HEIGHT = Cell.SIZE * ROWS;
    public static final int GRID_WIDTH = 8;
    public static final int GRID_WIDTH_HALF = GRID_WIDTH / 2;
    public static final Color COLOR_GRID = new Color(41, 25, 13);
    public static final int Y_OFFSET = 1;

    private Image bgImage;
    Cell[][] cells;
    private int winType = -1;
    public Board() {
        initGame();
        bgImage = new ImageIcon(getClass().getResource("/image/crafting22.png")).getImage();
    }

    public void initGame() {
        cells = new Cell[ROWS][COLS];
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                cells[row][col] = new Cell(row, col);
            }
        }
    }

    public void newGame() {
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                cells[row][col].newGame();
            }
        }
        winType = -1;
    }

    public State stepGame(Seed player, int selectedRow, int selectedCol) {
        cells[selectedRow][selectedCol].content = player;
        for (int row = 0; row < ROWS; row++) {
            if (cells[row][0].content == player && cells[row][1].content == player && cells[row][2].content == player) {
                winType = row;
                SoundEffect.DIE.play();
                return (player == Seed.CROSS) ? State.CROSS_WON : State.NOUGHT_WON;
            }
        }
        for (int col = 0; col < COLS; col++) {
            if (cells[0][col].content == player && cells[1][col].content == player && cells[2][col].content == player) {
                winType = 3 + col;
                SoundEffect.DIE.play();
                return (player == Seed.CROSS) ? State.CROSS_WON : State.NOUGHT_WON;
            }
        }
        if (cells[0][0].content == player && cells[1][1].content == player && cells[2][2].content == player) {
            winType = 6;
            SoundEffect.DIE.play();
            return (player == Seed.CROSS) ? State.CROSS_WON : State.NOUGHT_WON;
        }
        if (cells[0][2].content == player && cells[1][1].content == player && cells[2][0].content == player) {
            winType = 7;
            SoundEffect.DIE.play();
            return (player == Seed.CROSS) ? State.CROSS_WON : State.NOUGHT_WON;
        }
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                if (cells[row][col].content == Seed.NO_SEED) {
                    return State.PLAYING;
                }
            }
        }
        winType = -1;
        return State.DRAW;
    }

    public boolean hasWon(Seed player) {
        for (int row = 0; row < ROWS; ++row) {
            if (cells[row][0].content == player && cells[row][1].content == player && cells[row][2].content == player) {
                return true;
            }
        }
        for (int col = 0; col < COLS; ++col) {
            if (cells[0][col].content == player && cells[1][col].content == player && cells[2][col].content == player) {
                return true;
            }
        }
        if (cells[0][0].content == player && cells[1][1].content == player && cells[2][2].content == player) {
            return true;
        }
        if (cells[0][2].content == player && cells[1][1].content == player && cells[2][0].content == player) {
            return true;
        }
        return false;
    }

    public void paint(Graphics g) {
        if (bgImage != null) {
            g.drawImage(bgImage, 0, 0, CANVAS_WIDTH, CANVAS_HEIGHT, null);
        }
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
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                cells[row][col].paint(g);
            }
        }
        if (winType != -1) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.GREEN);
            g2.setStroke(new BasicStroke(6));
            int padding = Cell.SIZE / 2;
            int x1 = 0, y1 = 0, x2 = 0, y2 = 0;

            switch (winType) {
                case 0: case 1: case 2:
                    y1 = y2 = winType * Cell.SIZE + padding;
                    x1 = 0;
                    x2 = CANVAS_WIDTH;
                    break;
                case 3: case 4: case 5:
                    x1 = x2 = (winType - 3) * Cell.SIZE + padding;
                    y1 = 0;
                    y2 = CANVAS_HEIGHT;
                    break;
                case 6:
                    x1 = y1 = 0;
                    x2 = CANVAS_WIDTH;
                    y2 = CANVAS_HEIGHT;
                    break;
                case 7:
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