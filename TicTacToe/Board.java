package TicTacToe;
import java.awt.*;
import javax.swing.*;
/**
 * Kelas Board mewakili papan permainan Tic Tac Toe (3x3).
 * Menyimpan array Cell dan logika pengecekan kemenangan.
 */
public class Board {
    // Konstanta ukuran dan tampilan
    public static final int ROWS = 3;
    public static final int COLS = 3;
    public static final int CANVAS_WIDTH = Cell.SIZE * COLS;
    public static final int CANVAS_HEIGHT = Cell.SIZE * ROWS;
    public static final int GRID_WIDTH = 8;
    public static final int GRID_WIDTH_HALF = GRID_WIDTH / 2;
    public static final Color COLOR_GRID = new Color(41, 25, 13);
    public static final int Y_OFFSET = 1;

    private Image bgImage;
    // Array 2D Cell
    Cell[][] cells;
    private int winType = -1; // Menyimpan tipe kemenangan (baris/kolom/diagonal)

    /**
     * Konstruktor Board, inisialisasi papan dan gambar background
     */
    public Board() {
        initGame();
        bgImage = new ImageIcon(getClass().getResource("/image/crafting22.png")).getImage();
    }

    /**
     * Inisialisasi array Cell
     */
    public void initGame() {
        cells = new Cell[ROWS][COLS];
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                cells[row][col] = new Cell(row, col);
            }
        }
    }

    /**
     * Reset isi papan untuk game baru
     */
    public void newGame() {
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                cells[row][col].newGame();
            }
        }
        winType = -1;
    }

    /**
     * Proses langkah pemain dan cek status game
     */
    public State stepGame(Seed player, int selectedRow, int selectedCol) {
        cells[selectedRow][selectedCol].content = player;
        // Cek baris
        for (int row = 0; row < ROWS; row++) {
            if (cells[row][0].content == player && cells[row][1].content == player && cells[row][2].content == player) {
                winType = row;
                SoundEffect.DIE.play();
                return (player == Seed.CROSS) ? State.CROSS_WON : State.NOUGHT_WON;
            }
        }
        // Cek kolom
        for (int col = 0; col < COLS; col++) {
            if (cells[0][col].content == player && cells[1][col].content == player && cells[2][col].content == player) {
                winType = 3 + col;
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
        // Cek cell kosong (jika masih ada, lanjut main)
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

    /**
     * Cek apakah player menang
     */
    public boolean hasWon(Seed player) {
        // Cek baris
        for (int row = 0; row < ROWS; ++row) {
            if (cells[row][0].content == player && cells[row][1].content == player && cells[row][2].content == player) {
                return true;
            }
        }
        // Cek kolom
        for (int col = 0; col < COLS; ++col) {
            if (cells[0][col].content == player && cells[1][col].content == player && cells[2][col].content == player) {
                return true;
            }
        }
        // Cek diagonal
        if (cells[0][0].content == player && cells[1][1].content == player && cells[2][2].content == player) {
            return true;
        }
        // Cek diagonal sebaliknya
        if (cells[0][2].content == player && cells[1][1].content == player && cells[2][0].content == player) {
            return true;
        }
        return false;
    }

    /**
     * Gambar papan, grid, dan highlight kemenangan
     */
    public void paint(Graphics g) {
        if (bgImage != null) {
            g.drawImage(bgImage, 0, 0, CANVAS_WIDTH, CANVAS_HEIGHT, null);
        }
        // Gambar grid
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
        // Gambar isi cell
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                cells[row][col].paint(g);
            }
        }
        // Highlight garis kemenangan
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