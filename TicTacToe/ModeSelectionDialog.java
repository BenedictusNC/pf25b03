package TicTacToe;
import javax.swing.*;
import java.awt.*;
import javax.sound.sampled.*;
import java.net.URL;

public class ModeSelectionDialog extends JDialog {
    private boolean vsAI = false;
    private Clip bgmClip;

    public ModeSelectionDialog(JFrame parent) {
        super(parent, "Tic Tac Toe", true);
        setLayout(new BorderLayout());
        setSize(360, 200);
        setResizable(false);
        setLocationRelativeTo(parent);

        // === Start background music ===
        try {
            URL soundURL = getClass().getClassLoader().getResource("audio/menu_bgm.wav");
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL);
            bgmClip = AudioSystem.getClip();
            bgmClip.open(ais);
            bgmClip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            System.err.println("BGM load error: " + e.getMessage());
        }

        // === Title Label ===
        JLabel title = new JLabel("Tic Tac Toe", JLabel.CENTER);
        title.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        title.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        title.setForeground(new Color(51, 30, 0));
        add(title, BorderLayout.NORTH);

        // === Button Panel ===
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        buttonPanel.setBackground(new Color(255, 239, 184));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 20, 40));

        // === Player vs Player Button ===
        JButton pvpButton = createTextButton("Player vs Player");
        pvpButton.addActionListener(e -> {
            vsAI = false;
            SoundEffect.MENU.play();
            stopMusic();
            dispose();
        });

        // === Player vs AI Button ===
        JButton aiButton = createTextButton("Player vs AI");
        aiButton.addActionListener(e -> {
            vsAI = true;
            SoundEffect.MENU.play();
            stopMusic();
            dispose();
        });

        buttonPanel.add(pvpButton);
        buttonPanel.add(aiButton);
        add(buttonPanel, BorderLayout.CENTER);
    }

    private JButton createTextButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        button.setBackground(new Color(255, 248, 220));
        button.setForeground(new Color(80, 40, 0));
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
