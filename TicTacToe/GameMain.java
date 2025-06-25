package TicTacToe;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Tic-Tac-Toe: Versi grafis dua pemain dengan desain OOP yang lebih baik.
 * Kelas Board dan Cell dipisahkan dalam file terpisah.
 */
public class GameMain extends JPanel {
    private static final long serialVersionUID = 1L; // Untuk mencegah warning serializable

    // === Konstanta untuk tampilan grafis ===
    public static final String TITLE = "Tic Tac Toe";
    public static final Color COLOR_BG = new Color(247, 202, 139);
    public static final Color COLOR_BG_STATUS = new Color(247, 202, 139);
    public static final Font FONT_STATUS = new Font("Comic Sans MS", Font.PLAIN, 14);

    // === Variabel utama game ===
    private Board board;         // Objek papan permainan
    private State currentState;  // Status permainan saat ini
    private Seed currentPlayer;  // Pemain yang sedang jalan
    private JLabel statusBar;    // Label status di bawah
    private int scoreCross;
    private int scoreNought;
    private AIPlayer aiPlayer;
    private boolean vsAI = false;
    private boolean isAITurn = false;
    private String player1Name = "Player 1";
    private String player2Name = "Player 2";
    private boolean playerIsDiamond = true;
    private Seed playerSeed = Seed.CROSS;
    private Seed aiSeed = Seed.NOUGHT;

    /**
     * Konstruktor utama untuk setup UI dan komponen game
     */
    public GameMain(boolean vsAI, String player1Name, String player2Name, boolean playerIsDiamond) {
        this.vsAI = vsAI;
        this.player1Name = player1Name;
        this.player2Name = player2Name;
        this.playerIsDiamond = playerIsDiamond;
        this.playerSeed = playerIsDiamond ? Seed.CROSS : Seed.NOUGHT;
        this.aiSeed = playerIsDiamond ? Seed.NOUGHT : Seed.CROSS;

        // Listener mouse untuk klik pada papan
        super.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isAITurn) return; // Abaikan klik saat giliran AI

                int mouseX = e.getX();
                int mouseY = e.getY();
                int row = mouseY / Cell.SIZE;
                int col = mouseX / Cell.SIZE;

                if (currentState == State.PLAYING) {
                    if (row >= 0 && row < Board.ROWS && col >= 0 && col < Board.COLS
                            && board.cells[row][col].content == Seed.NO_SEED) {
                        // Mainkan suara sesuai pemain
                        if (currentPlayer == Seed.CROSS) {
                            SoundEffect.EAT_FOOD.play();
                        } else if (!vsAI) {
                            SoundEffect.EXPLODE.play();
                        }
                        // Update papan dan cek status game
                        currentState = board.stepGame(currentPlayer, row, col);
                        if (currentState == State.CROSS_WON) {
                            scoreCross++;
                        } else if (currentState == State.NOUGHT_WON) {
                            scoreNought++;
                        }
                        // Ganti giliran pemain
                        currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
                        repaint();

                        // Jika mode AI dan giliran AI
                        if (vsAI && currentPlayer == aiSeed && currentState == State.PLAYING) {
                            isAITurn = true;
                            Timer timer = new Timer(500, new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    SoundEffect.EXPLODE.play();
                                    int[] move = aiPlayer.move(board);
                                    currentState = board.stepGame(aiSeed, move[0], move[1]);
                                    if (currentState == State.NOUGHT_WON || currentState == State.CROSS_WON) {
                                        if (aiSeed == Seed.CROSS) scoreCross++;
                                        else scoreNought++;
                                    }
                                    currentPlayer = playerSeed;
                                    isAITurn = false;
                                    repaint();
                                }
                            });
                            timer.setRepeats(false);
                            timer.start();
                        }
                    }
                } else {
                    // Jika game selesai, klik untuk mulai ulang
                    newGame();
                }
                repaint();
            }
        });

        // === Status bar di bawah papan ===
        statusBar = new JLabel();
        statusBar.setFont(FONT_STATUS);
        statusBar.setBackground(COLOR_BG_STATUS);
        statusBar.setOpaque(true);
        statusBar.setPreferredSize(new Dimension(300, 30));
        statusBar.setHorizontalAlignment(JLabel.LEFT);
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 12));

        super.setLayout(new BorderLayout());
        super.add(statusBar, BorderLayout.PAGE_END);
        super.setPreferredSize(new Dimension(Board.CANVAS_WIDTH, Board.CANVAS_HEIGHT + 30));
        super.setBorder(BorderFactory.createLineBorder(COLOR_BG_STATUS, 2, false));

        // Inisialisasi game
        initGame();
        newGame();
    }

    /**
     * Konstruktor lama untuk kompatibilitas (default: PvP, Diamond)
     */
    public GameMain() {
        this(false, "Player 1", "Player 2", true);
    }

    /**
     * Inisialisasi objek game (hanya sekali saat awal)
     * Membuat papan, AI, dan reset skor.
     */
    public void initGame() {
        board = new Board();  // Membuat papan baru
        aiPlayer = new AIPlayer(aiSeed); // Membuat AI
        scoreCross = 0;
        scoreNought = 0;
    }

    /**
     * Reset papan dan status, siap untuk game baru
     * Mengatur giliran, status, dan jika perlu AI jalan duluan.
     */
    public void newGame() {
        board.newGame();
        currentPlayer = playerSeed;
        currentState = State.PLAYING;
        // Jika AI jalan duluan (player pilih Emerald)
        if (vsAI && !playerIsDiamond) {
            isAITurn = true;
            Timer timer = new Timer(500, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    SoundEffect.EXPLODE.play();
                    int[] move = aiPlayer.move(board);
                    currentState = board.stepGame(aiSeed, move[0], move[1]);
                    if (currentState == State.NOUGHT_WON || currentState == State.CROSS_WON) {
                        if (aiSeed == Seed.CROSS) scoreCross++;
                        else scoreNought++;
                    }
                    currentPlayer = playerSeed;
                    isAITurn = false;
                    repaint();
                }
            });
            timer.setRepeats(false);
            timer.start();
        }
    }

    /**
     * Custom painting untuk panel utama
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(COLOR_BG);
        board.paint(g); // Gambar papan

        // Tampilkan status di status bar
        String scoreStr = "  |  Score: " + player1Name + " :" + scoreCross + ", " + player2Name + " :" + scoreNought;
        if (currentState == State.PLAYING) {
            statusBar.setForeground(Color.BLACK);
            String turnName = (currentPlayer == Seed.CROSS) ? player1Name : player2Name;
            statusBar.setText(turnName + "'s Turn" + scoreStr);
        } else if (currentState == State.DRAW) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("It's a Draw! Click to play again." + scoreStr);
        } else if (currentState == State.CROSS_WON) {
            statusBar.setForeground(Color.RED);
            statusBar.setText(player1Name + " Won! Click to play again." + scoreStr);
        } else if (currentState == State.NOUGHT_WON) {
            statusBar.setForeground(Color.RED);
            statusBar.setText(player2Name + " Won! Click to play again." + scoreStr);
        }
    }

    /**
     * Membuat menu bar utama
     */
    public JMenuBar createMenuBar() {
        JMenuBar menuBar  = new JMenuBar();
        JMenu menu = new JMenu("Game");
        menuBar.add(menu);

        JMenuItem menuItem = new JMenuItem("New Game");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newGame();
                repaint();
            }
        });
        menu.add(menuItem);

        menu.addSeparator();

        JMenuItem backToModeMenuItem = new JMenuItem("Back to Mode Selection");
        backToModeMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(GameMain.this);
                if (topFrame != null) topFrame.dispose();
                SwingUtilities.invokeLater(() -> {
                    ModeSelectionDialog modeDialog = new ModeSelectionDialog(null);
                    modeDialog.setVisible(true);
                    if (!modeDialog.isSubmitted()) {
                        return;
                    }
                    boolean newVsAI = modeDialog.isVsAI();
                    String player1 = player1Name;
                    String player2 = player2Name;
                    boolean playerIsDiamond = GameMain.this.playerIsDiamond;
                    if (!newVsAI) {
                        if (player2.equals("AI")) player2 = "Player 2";
                        UsernameInputDialog usernameDialog2 = new UsernameInputDialog(null);
                        usernameDialog2.setTitleLabel("Masukkan Nama Player 2");
                        usernameDialog2.setVisible(true);
                        if (!usernameDialog2.isSubmitted()) return; // <-- Tambah pengecekan close dialog
                        String inputPlayer2 = usernameDialog2.getUsername();
                        if (!inputPlayer2.isEmpty()) {
                            player2 = inputPlayer2;
                        }
                    } else {
                        player2 = "AI";
                        ChooseSeedDialog chooseSeedDialog = new ChooseSeedDialog(null);
                        chooseSeedDialog.setVisible(true);
                        if (!chooseSeedDialog.isSubmitted()) return; // <-- Tambah pengecekan close dialog
                        playerIsDiamond = chooseSeedDialog.isDiamond();
                    }
                    GameMain.showGameWindow(newVsAI, player1, player2, playerIsDiamond);
                });
            }
        });
        menu.add(backToModeMenuItem);

        menu.addSeparator();

        menuItem = new JMenuItem("Exit");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        menu.add(menuItem);

        menu = new JMenu("Help");
        menuBar.add(menu);

        menuItem = new JMenuItem("About");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(GameMain.this,
                        "Tic-Tac-Toe game implemented by Len, Nico, dan Erpan",
                        "About",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        menu.add(menuItem);

        return menuBar;
    }

    /**
     * Menampilkan dialog pemilihan mode game
     */
    public void showModeDialog() {
        ModeSelectionDialog dialog = new ModeSelectionDialog(null);
        dialog.setVisible(true);
        vsAI = dialog.isVsAI();
        SoundEffect.MENU.play();
    }

    /**
     * Menampilkan window utama game
     */
    public static void showGameWindow(boolean vsAI, String player1Name, String player2Name) {
        showGameWindow(vsAI, player1Name, player2Name, true);
    }

    /**
     * Overload: menerima parameter playerIsDiamond
     */
    public static void showGameWindow(boolean vsAI, String player1Name, String player2Name, boolean playerIsDiamond) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame(TITLE);
                GameMain gameMain = new GameMain(vsAI, player1Name, player2Name, playerIsDiamond);
                frame.setContentPane(gameMain);
                frame.setJMenuBar(gameMain.createMenuBar());
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

    /**
     * Overload lama untuk kompatibilitas
     */
    public static void showGameWindow() {
        showGameWindow(false, "Player 1", "Player 2");
    }

    /**
     * Entry point utama aplikasi
     */
    public static void main(String[] args) {
        WelcomePage.showWelcome();
    }
}
