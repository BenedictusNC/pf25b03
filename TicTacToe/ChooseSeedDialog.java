package TicTacToe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ChooseSeedDialog extends JDialog {
    private boolean isDiamond = true;
    private boolean submitted = false;

    public ChooseSeedDialog(JFrame parent) {
        super(parent, "Pilih", true);
        setSize(500, 320);
        setResizable(false);
        setLocationRelativeTo(parent);

        JPanel bgPanel = new JPanel() {
            private Image bgImage = new ImageIcon(getClass().getClassLoader().getResource("image/menu_bg.png")).getImage();
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bgImage != null) {
                    g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        bgPanel.setLayout(new BoxLayout(bgPanel, BoxLayout.Y_AXIS));
        setContentPane(bgPanel);

        JLabel title = new JLabel("Pilih Karakter Anda", JLabel.CENTER);
        title.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(BorderFactory.createEmptyBorder(18, 10, 10, 10));
        bgPanel.add(title);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 60, 20)); // Lebih lebar

        ImageIcon diamondIcon = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("image/Diamond.png")).getImage().getScaledInstance(96, 96, Image.SCALE_SMOOTH));
        JButton diamondBtn = new JButton("Diamond", diamondIcon);
        diamondBtn.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        diamondBtn.setForeground(new Color(51, 0, 0));
        diamondBtn.setFocusPainted(false);
        diamondBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        diamondBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
        diamondBtn.setHorizontalTextPosition(SwingConstants.CENTER);
        diamondBtn.setIconTextGap(10);
        diamondBtn.addActionListener(e -> {
            isDiamond = true;
            submitted = true;
            dispose();
        });

        ImageIcon emeraldIcon = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("image/Emerald.png")).getImage().getScaledInstance(96, 96, Image.SCALE_SMOOTH));
        JButton emeraldBtn = new JButton("Emerald", emeraldIcon);
        emeraldBtn.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        emeraldBtn.setForeground(new Color(51, 0, 0));
        emeraldBtn.setFocusPainted(false);
        emeraldBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        emeraldBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
        emeraldBtn.setHorizontalTextPosition(SwingConstants.CENTER);
        emeraldBtn.setIconTextGap(10);
        emeraldBtn.addActionListener(e -> {
            isDiamond = false;
            submitted = true;
            dispose();
        });

        buttonPanel.add(diamondBtn);
        buttonPanel.add(emeraldBtn);
        bgPanel.add(buttonPanel);
    }

    public boolean isDiamond() {
        return isDiamond;
    }
    public boolean isSubmitted() {
        return submitted;
    }
}
