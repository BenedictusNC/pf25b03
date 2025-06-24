package TicTacToe;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.sound.sampled.*;
import java.net.URL;

public class ModeSelectionDialog extends JDialog {
    private boolean vsAI = false;
    private Clip bgmClip;

    public ModeSelectionDialog(JFrame parent) {
        super(parent, "Tic Tac Toe", true);
        setSize(400, 240);
        setResizable(false);
        setLocationRelativeTo(parent);

        // === Load & play background music ===
        try {
            URL soundURL = getClass().getClassLoader().getResource("audio/menu_bgm.wav");
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL);
            bgmClip = AudioSystem.getClip();
            bgmClip.open(ais);
            bgmClip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            System.err.println("BGM load error: " + e.getMessage());
        }

        // === Custom panel with background image ===
        JPanel bgPanel = new JPanel() {
            private Image bgImage = new ImageIcon(
                    getClass().getClassLoader().getResource("image/menu_bg.png")
            ).getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bgImage != null) {
                    g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        bgPanel.setLayout(new BorderLayout());
        setContentPane(bgPanel);

        // === Title label ===
        JLabel title = new JLabel("Tic Tac Toe", JLabel.CENTER);
        title.setFont(new Font("Comic Sans MS", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

        // === Button panel ===
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 20, 50));

        JButton pvpBtn = createTextButton("Player vs Player");
        JButton aiBtn = createTextButton("Player vs AI");

        pvpBtn.addActionListener(e -> {
            vsAI = false;
            SoundEffect.MENU.play();
            stopMusic();
            dispose();
        });

        aiBtn.addActionListener(e -> {
            vsAI = true;
            SoundEffect.MENU.play();
            stopMusic();
            dispose();
        });

        buttonPanel.add(pvpBtn);
        buttonPanel.add(aiBtn);

        bgPanel.add(title, BorderLayout.NORTH);
        bgPanel.add(buttonPanel, BorderLayout.CENTER);
    }

    private JButton createTextButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        button.setBackground(new Color(255, 255, 255, 220)); // transparan putih
        button.setForeground(new Color(51, 0, 0));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void stopMusic() {
        if (bgmClip != null && bgmClip.isRunning()) {
            bgmClip.stop();
            bgmClip.close();
        }
    }

    public boolean isVsAI() {
        return vsAI;
    }
}
