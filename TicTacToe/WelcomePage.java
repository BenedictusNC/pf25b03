package TicTacToe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WelcomePage extends JFrame {
    public WelcomePage() {
        setTitle("Minecraft Tic Tac Toe");
        setSize(420, 320);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // === Minecraft-style background panel ===
        JPanel panel = new JPanel() {
            private Image bgImage = new ImageIcon(getClass().getClassLoader().getResource("image/welcome.png")).getImage();
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
        JLabel titleLabel = new JLabel("Tic Tac Toe");
        titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 36));
        titleLabel.setForeground(new Color(60, 68, 68)); // Minecraft dark text
        titleLabel.setBorder(null); // No border
        titleLabel.setOpaque(false); // No background
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);

        panel.add(Box.createVerticalStrut(40));
        JButton startButton = new JButton("Start Game");
        startButton.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        startButton.setBackground(new Color(94, 124, 22));
        startButton.setForeground(new Color(221, 221, 221));
        startButton.setBorder(BorderFactory.createLineBorder(new Color(60, 68, 68), 3));
        startButton.setFocusPainted(false);
        startButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Tutup welcome page sebelum dialog mode muncul
                dispose();
                UsernameInputDialog usernameDialog = new UsernameInputDialog(null);
                usernameDialog.setVisible(true);
                String player1 = usernameDialog.getUsername();
                if (!usernameDialog.isSubmitted()) return;
                ModeSelectionDialog dialog = new ModeSelectionDialog(null);
                dialog.setVisible(true);
                boolean vsAI = dialog.isVsAI();
                String player2 = "AI";
                if (!vsAI) {
                    UsernameInputDialog usernameDialog2 = new UsernameInputDialog(null);
                    usernameDialog2.setTitleLabel("Masukkan Nama Player 2");
                    usernameDialog2.setVisible(true);
                    player2 = usernameDialog2.getUsername();
                    if (!usernameDialog2.isSubmitted() || player2.isEmpty()) player2 = "Player 2";
                }
                GameMain.showGameWindow(vsAI, player1, player2);
            }
        });
        panel.add(startButton);

        setContentPane(panel);
    }

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
