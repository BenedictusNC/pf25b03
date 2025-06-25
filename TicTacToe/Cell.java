package TicTacToe;
import java.awt.*;
/**
 * Kelas Cell mewakili satu kotak pada papan Tic Tac Toe.
 * Menyimpan posisi dan isi (CROSS, NOUGHT, atau kosong).
 */
public class Cell {
    // Konstanta ukuran dan padding
    public static final int SIZE = 120; // ukuran kotak
    public static final int PADDING = SIZE / 5;
    public static final int SEED_SIZE = SIZE - PADDING * 2;

    // Isi cell (CROSS, NOUGHT, atau NO_SEED)
    Seed content;
    int row, col;

    /**
     * Konstruktor Cell
     */
    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        this.content = Seed.NO_SEED;
    }

    /**
     * Reset isi cell menjadi kosong
     */
    public void newGame() {
        content = Seed.NO_SEED;
    }

    /**
     * Gambar isi cell (jika ada) pada canvas
     */
    public void paint(Graphics g) {
        int x1 = col * SIZE + PADDING;
        int y1 = row * SIZE + PADDING;
        if (content == Seed.CROSS || content == Seed.NOUGHT) {
            g.drawImage(content.getImage(), x1, y1, SEED_SIZE, SEED_SIZE, null);
        }
    }
}