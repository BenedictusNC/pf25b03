package TicTacToe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.sound.sampled.*;
import java.net.URL;

public class UsernameInputDialog extends JDialog {
    private String username = "";
    private Clip bgmClip;
    private JTextField usernameField;
    private boolean submitted = false;
    private JLabel title; // Make title a field

    public UsernameInputDialog(JFrame parent) {
        super(parent, "Input Username", true);
        setSize(400, 260);
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
        bgPanel.setLayout(new GridBagLayout());
        setContentPane(bgPanel);

        // === Main content panel with rounded background ===
        JPanel contentPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0,0,0,120));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 32, 32);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(18, 24, 18, 24));

        // === Title label with drop shadow ===
        title = new JLabel("Masukkan Nama Player 1", JLabel.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                // Draw drop shadow
                g2.setColor(new Color(0, 0, 0, 120));
                g2.setFont(getFont());
                g2.drawString(getText(), 3, getHeight() - 13 + 2);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        title.setFont(new Font("Comic Sans MS", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setOpaque(false);

        contentPanel.add(title);
        contentPanel.add(Box.createVerticalStrut(18));

        // === Input panel ===
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setOpaque(false);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        usernameField = new JTextField();
        usernameField.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
        usernameField.setColumns(12);
        usernameField.setOpaque(true);
        usernameField.setBackground(Color.WHITE);
        usernameField.setForeground(new Color(51, 0, 0));
        usernameField.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(51, 0, 0)));
        inputPanel.add(usernameField, BorderLayout.CENTER);

        JButton submitBtn = new JButton("OK");
        submitBtn.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        submitBtn.setBackground(Color.WHITE);
        submitBtn.setOpaque(true);
        submitBtn.setForeground(new Color(51, 0, 0));
        submitBtn.setFocusPainted(false);
        submitBtn.setContentAreaFilled(true);
        submitBtn.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        submitBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        submitBtn.addActionListener(e -> submit());
        inputPanel.add(submitBtn, BorderLayout.SOUTH);

        usernameField.addActionListener(e -> submit());

        contentPanel.add(inputPanel);
        bgPanel.add(contentPanel);
    }

    private void submit() {
        username = usernameField.getText().trim();
        if (username.isEmpty()) {
            username = "Player 1";
        }
        submitted = true;
        stopMusic();
        dispose();
    }

    private void stopMusic() {
        if (bgmClip != null && bgmClip.isRunning()) {
            bgmClip.stop();
            bgmClip.close();
        }
    }

    public String getUsername() {
        return username;
    }

    public boolean isSubmitted() {
        return submitted;
    }

    public void setTitleLabel(String text) {
        title.setText(text);
    }
}
