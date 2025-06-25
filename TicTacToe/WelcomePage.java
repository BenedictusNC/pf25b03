package TicTacToe;

import javax.swing.*;
import java.awt.*;

public class WelcomePage extends JFrame {
    public WelcomePage() {
        setTitle("Minecraft Tic Tac Toe");
        setSize(420, 320);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Membuat panel dengan background custom (gambar welcome)
        JPanel panel = new JPanel() {
            private final Image bgImage = new ImageIcon(getClass().getClassLoader().getResource("image/welcome.png")).getImage();
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bgImage != null) {
                    g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(Box.createVerticalStrut(60));

        // Label judul utama
        JLabel titleLabel = new JLabel("Tic Tac Toe");
        titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 36));
        titleLabel.setForeground(new Color(216, 216, 216));
        titleLabel.setBorder(null);
        titleLabel.setOpaque(false);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);

        panel.add(Box.createVerticalStrut(40));

        // Tombol untuk memulai permainan
        JButton startButton = new JButton("Start Game");
        startButton.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        startButton.setBackground(new Color(94, 124, 22));
        startButton.setForeground(new Color(221, 221, 221));
        startButton.setBorder(BorderFactory.createLineBorder(new Color(60, 68, 68), 3));
        startButton.setFocusPainted(false);
        startButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        // Event klik tombol start: buka dialog input nama, mode, dan karakter
        startButton.addActionListener(e -> {
            // Tutup welcome page sebelum dialog mode muncul
            dispose();
            // Dialog input nama player 1
            UsernameInputDialog usernameDialog = new UsernameInputDialog(null);
            usernameDialog.setVisible(true);
            String player1 = usernameDialog.getUsername();
            if (!usernameDialog.isSubmitted()) return;

            // Dialog pemilihan mode (vs AI atau PvP)
            ModeSelectionDialog modeDialog = new ModeSelectionDialog(null);
            modeDialog.setVisible(true);
            if (!modeDialog.isSubmitted()) return;
            boolean vsAI = modeDialog.isVsAI();
            String player2 = "AI";
            boolean playerIsDiamond = true;

            if (!vsAI) {
                // Jika PvP, input nama player 2
                UsernameInputDialog usernameDialog2 = new UsernameInputDialog(null);
                usernameDialog2.setTitleLabel("Masukkan Nama Player 2");
                usernameDialog2.setVisible(true);
                if (!usernameDialog2.isSubmitted()) return;
                player2 = usernameDialog2.getUsername();
                if (player2.isEmpty()) player2 = "Player 2";
            } else {
                // Jika lawan AI, pilih karakter (Diamond/Emerald)
                ChooseSeedDialog chooseSeedDialog = new ChooseSeedDialog(null);
                chooseSeedDialog.setVisible(true);
                if (!chooseSeedDialog.isSubmitted()) return;
                playerIsDiamond = chooseSeedDialog.isDiamond();
            }
            // Menampilkan window utama game
            GameMain.showGameWindow(vsAI, player1, player2, playerIsDiamond);
        });
        panel.add(startButton);

        setContentPane(panel);
    }

    // Menampilkan halaman welcome secara thread-safe
    public static void showWelcome() {
        SwingUtilities.invokeLater(() -> {
            WelcomePage welcome = new WelcomePage();
            welcome.setVisible(true);
        });
    }

    public static void main(String[] args) {
        showWelcome();
    }
}
