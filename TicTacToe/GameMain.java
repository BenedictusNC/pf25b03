package TicTacToe;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
/**
 * Tic-Tac-Toe: Two-player Graphic version with better OO design.
 * The Board and Cell classes are separated in their own classes.
 */
public class GameMain extends JPanel {
    private static final long serialVersionUID = 1L; // to prevent serializable warning

    // Define named constants for the drawing graphics
    public static final String TITLE = "Tic Tac Toe";
    public static final Color COLOR_BG = new Color(247, 202, 139);
    public static final Color COLOR_BG_STATUS = new Color(247, 202, 139);
    public static final Font FONT_STATUS = new Font("Comic Sans MS", Font.PLAIN, 14);

    // Define game objects
    private Board board;         // the game board
    private State currentState;  // the current state of the game
    private Seed currentPlayer;  // the current player
    private JLabel statusBar;    // for displaying status message
    private int scoreCross;
    private int scoreNought;
    private AIPlayer aiPlayer;
    private boolean vsAI = false;
    private boolean isAITurn = false;

    /** Constructor to setup the UI and game components */
    public GameMain() {

        // This JPanel fires MouseEvent
        super.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {  // mouse-clicked handler
                if (isAITurn) return; // Ignore clicks during AI's turn

                int mouseX = e.getX();
                int mouseY = e.getY();
                // Get the row and column clicked
                int row = mouseY / Cell.SIZE;
                int col = mouseX / Cell.SIZE;

                if (currentState == State.PLAYING) {
                    if (row >= 0 && row < Board.ROWS && col >= 0 && col < Board.COLS
                            && board.cells[row][col].content == Seed.NO_SEED) {
                        if (currentPlayer == Seed.CROSS) {
                            SoundEffect.EAT_FOOD.play();
                        } else if (!vsAI) {
                            SoundEffect.EXPLODE.play();
                        }
                        // Update cells[][] and return the new game state after the move
                        currentState = board.stepGame(currentPlayer, row, col);
                        if (currentState == State.CROSS_WON) {
                            scoreCross++;
                        } else if (currentState == State.NOUGHT_WON) {
                            scoreNought++;
                        }
                        // Switch player
                        currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
                        repaint();

                        if (vsAI && currentPlayer == Seed.NOUGHT && currentState == State.PLAYING) {
                            isAITurn = true;
                            Timer timer = new Timer(500, new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    SoundEffect.EXPLODE.play();
                                    int[] move = aiPlayer.move(board);
                                    currentState = board.stepGame(Seed.NOUGHT, move[0], move[1]);
                                    if (currentState == State.NOUGHT_WON) {
                                        scoreNought++;
                                    }
                                    currentPlayer = Seed.CROSS;
                                    isAITurn = false;
                                    repaint();
                                }
                            });
                            timer.setRepeats(false);
                            timer.start();
                        }
                    }
                } else {
                     // game over
                    newGame();  // restart the game
                }
                repaint();
            }
        });

        // Setup the status bar (JLabel) to display status message
        statusBar = new JLabel();
        statusBar.setFont(FONT_STATUS);
        statusBar.setBackground(COLOR_BG_STATUS);
        statusBar.setOpaque(true);
        statusBar.setPreferredSize(new Dimension(300, 30));
        statusBar.setHorizontalAlignment(JLabel.LEFT);
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 12));

        super.setLayout(new BorderLayout());
        super.add(statusBar, BorderLayout.PAGE_END); // same as SOUTH
        super.setPreferredSize(new Dimension(Board.CANVAS_WIDTH, Board.CANVAS_HEIGHT + 30));
        // account for statusBar in height
        super.setBorder(BorderFactory.createLineBorder(COLOR_BG_STATUS, 2, false));

        // Set up Game
        initGame();
        newGame();
    }

    /** Initialize the game (run once) */
    public void initGame() {
        board = new Board();  // allocate the game-board
        aiPlayer = new AIPlayer(Seed.NOUGHT);
        scoreCross = 0;
        scoreNought = 0;
    }



    /** Reset the game-board contents and the current-state, ready for new game */
    public void newGame() {
        board.newGame();
        currentPlayer = Seed.CROSS;    // cross plays first
        currentState = State.PLAYING;  // ready to play
    }

    /** Custom painting codes on this JPanel */
    @Override
    public void paintComponent(Graphics g) {  // Callback via repaint()
        super.paintComponent(g);
        setBackground(COLOR_BG); // set its background color

        board.paint(g);  // ask the game board to paint itself

        // Print status-bar message
        // Print status-bar message
        String scoreStr = "  |  Score: D =" + scoreCross + ", E =" + scoreNought;
        if (currentState == State.PLAYING) {
            statusBar.setForeground(Color.BLACK);
            statusBar.setText(((currentPlayer == Seed.CROSS) ? "D's Turn" : "E's Turn") + scoreStr);
        } else if (currentState == State.DRAW) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("It's a Draw! Click to play again." + scoreStr);
        } else if (currentState == State.CROSS_WON) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("Diamond Won! Click to play again." + scoreStr);
        } else if (currentState == State.NOUGHT_WON) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("Emerald Won! Click to play again." + scoreStr);
        }
    }

    /** Create the menu bar */
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
                        "Tic-Tac-Toe game implemented by Len, Nico, and Erpan",
                        "About",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        menu.add(menuItem);

        return menuBar;
    }

    /** Show a dialog to choose the game mode */
    public void showModeDialog() {
        ModeSelectionDialog dialog = new ModeSelectionDialog(null);
        dialog.setVisible(true);
        vsAI = dialog.isVsAI();
        SoundEffect.MENU.play();
    }

    /**
     * Show the main game window
     */
    public static void showGameWindow(boolean vsAI) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame(TITLE);
                GameMain gameMain = new GameMain();
                gameMain.vsAI = vsAI;
                frame.setContentPane(gameMain);
                frame.setJMenuBar(gameMain.createMenuBar());
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setLocationRelativeTo(null); // center the application window
                frame.setVisible(true);            // show it
            }
        });
    }

    // Overload lama untuk kompatibilitas
    public static void showGameWindow() {
        showGameWindow(false);
    }

    /** The entry "main" method */
    public static void main(String[] args) {
        WelcomePage.showWelcome();
    }
}