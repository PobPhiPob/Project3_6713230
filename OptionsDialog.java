package Project3_6713230;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;

public class OptionsDialog extends JDialog {

    private float currentVolume;
    private GameSoundEffect soundEffect;
    private JSlider volumeSlider;
    private JLabel volumeValueLabel;
    private JComboBox<String> backgroundCombo;
    private MenuSystem menuParent;

    public OptionsDialog(JFrame parent, float initialVolume, GameSoundEffect sound) {
        super(parent, "Options", true);
        this.currentVolume = initialVolume;
        this.soundEffect = sound;

        if (parent instanceof MenuSystem) {
            this.menuParent = (MenuSystem) parent;
        }

        setSize(700, 650);
        setLocationRelativeTo(parent);
        setUndecorated(true);

        BackgroundPanel mainPanel = new BackgroundPanel();
        mainPanel.setLayout(null);
        setContentPane(mainPanel);

        createComponents();
    }

    private void createComponents() {
        int leftX = 60;
        int rightX = 380;
        int startY = 100;

        // Title - Stone gray color matching menu style
        JLabel titleLabel = new JLabel("GAME OPTIONS", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 36));
        titleLabel.setForeground(new Color(210, 205, 195)); // Stone gray-white
        titleLabel.setBounds(0, 30, 700, 60);
        add(titleLabel);

        // Difficulty Section
        int diffY = startY + 95;
        JLabel diffLabel = new JLabel("Select Difficulty:");
        diffLabel.setFont(new Font("Arial", Font.BOLD, 16));
        diffLabel.setForeground(new Color(190, 185, 175)); // Lighter stone gray
        diffLabel.setBounds(leftX, diffY, 250, 30);
        add(diffLabel);

        ButtonGroup difficultyGroup = new ButtonGroup();
        String[] difficulties = {"Easy", "Normal", "Hard", "Expert", "Nightmare"};
        Color[] diffColors = {
            new Color(120, 180, 120),      // Easy - muted green
            new Color(160, 160, 180),      // Normal - gray
            new Color(200, 140, 80),       // Hard - muted orange
            new Color(180, 100, 100),      // Expert - muted red
            new Color(140, 100, 140)       // Nightmare - muted purple
        };

        int radioY = diffY + 35;
        for (int i = 0; i < 5; i++) {
            StyledRadioButton radio = new StyledRadioButton(difficulties[i], diffColors[i]);
            radio.setBounds(leftX + 20, radioY, 200, 35);
            difficultyGroup.add(radio);
            add(radio);

            final int index = i;
            radio.addActionListener(e -> System.out.println("Difficulty: " + difficulties[index]));

            if (i == 1) {
                radio.setSelected(true);
            }
            radioY += 40;
        }

        // Volume Section
        int volumeY = startY;
        JLabel volumeLabel = new JLabel("Volume Control:");
        volumeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        volumeLabel.setForeground(new Color(190, 185, 175)); // Stone gray
        volumeLabel.setBounds(rightX, volumeY, 250, 30);
        add(volumeLabel);

        volumeValueLabel = new VolumeValueLabel(String.format("%.0f%%", currentVolume * 100));
        volumeValueLabel.setBounds(rightX + 220, volumeY + 45, 80, 40);
        add(volumeValueLabel);

        volumeSlider = new JSlider(0, 100, (int) (currentVolume * 100));
        volumeSlider.setOpaque(false);
        volumeSlider.setFocusable(false);
        volumeSlider.setBounds(rightX, volumeY + 45, 210, 50);
        volumeSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int value = volumeSlider.getValue();
                currentVolume = value / 100.0f;
                volumeValueLabel.setText(String.format("%.0f%%", currentVolume * 100));
                if (soundEffect != null) {
                    soundEffect.setVolume(currentVolume);
                }
            }
        });
        add(volumeSlider);

        // Background ComboBox Section
        if (menuParent != null) {
            int bgY = volumeY + 120;
            JLabel bgLabel = new JLabel("Menu Background:");
            bgLabel.setFont(new Font("Arial", Font.BOLD, 16));
            bgLabel.setForeground(new Color(190, 185, 175)); // Stone gray
            bgLabel.setBounds(rightX, bgY, 250, 30);
            add(bgLabel);

            String[] bgOptions = {"Castle", "Church", "Forest", "Rain", "Random"};
            backgroundCombo = new JComboBox<>(bgOptions);
            backgroundCombo.setFont(new Font("Arial", Font.PLAIN, 14));
            backgroundCombo.setForeground(new Color(200, 195, 185)); // Same as Credits text
            backgroundCombo.setBackground(new Color(20, 19, 23)); // Same as Credits panel
            backgroundCombo.setBorder(BorderFactory.createLineBorder(new Color(90, 85, 80), 2)); // Same as Credits border
            backgroundCombo.setBounds(rightX, bgY + 35, 250, 40);
            backgroundCombo.setSelectedIndex(4);

            backgroundCombo.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        int selectedIndex = backgroundCombo.getSelectedIndex();
                        if (menuParent != null) {
                            menuParent.changeBackground(selectedIndex);
                        }
                    }
                }
            });
            add(backgroundCombo);
        }

        // Info Panel 
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(null);
        infoPanel.setOpaque(true);
        infoPanel.setBackground(new Color(20, 19, 23)); 
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(90, 85, 80), 2), 
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        infoPanel.setBounds(rightX, volumeY + (menuParent != null ? 190 : 120), 280, 180);
        add(infoPanel);

        JLabel infoTitle = new JLabel("Game Information");
        infoTitle.setFont(new Font("Arial", Font.BOLD, 16));
        infoTitle.setForeground(new Color(210, 205, 195)); 
        infoTitle.setBounds(5, 5, 250, 30);
        infoPanel.add(infoTitle);

        String[] infoTexts = {
            "• Press ESC to pause",
            "• A/D - Move",
            "• SPACE/W - Jump",
            "• J - Attack",
            "• L - Heal",
            "• K - Roll"
        };
        
        for (int i = 0; i < infoTexts.length; i++) {
            JLabel info = new JLabel(infoTexts[i]);
            info.setFont(new Font("Monospaced", Font.PLAIN, 12));
            info.setForeground(new Color(200, 195, 185));
            info.setBounds(10, 30 + (i * 20), 250, 20);
            infoPanel.add(info);
        }

        // Back Button - Dark stone theme
        GlowButton backButton = new GlowButton("BACK", new Color(60, 58, 62));
        backButton.setBounds(250, 560, 200, 50);
        backButton.addActionListener(e -> dispose());
        add(backButton);
    }

    public float getCurrentVolume() {
        return currentVolume;
    }
}

class BackgroundPanel extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        // dark gradient
        GradientPaint gradient = new GradientPaint(
            0, 0, new Color(8, 8, 12),
            0, getHeight(), new Color(18, 16, 20)
        );
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        
        // Subtle stone-like border
        g2d.setStroke(new BasicStroke(3));
        g2d.setColor(new Color(80, 75, 70, 180));
        g2d.drawRoundRect(10, 10, getWidth() - 20, getHeight() - 20, 15, 15);
        
        // subtle inner highlight
        g2d.setColor(new Color(120, 110, 100, 30));
        g2d.setStroke(new BasicStroke(1));
        g2d.drawRoundRect(13, 13, getWidth() - 26, getHeight() - 26, 12, 12);
    }
}

// radio button
class StyledRadioButton extends JRadioButton {
    private Color accentColor;

    public StyledRadioButton(String text, Color color) {
        super(text);
        this.accentColor = color;
        setFont(new Font("Arial", Font.PLAIN, 14));
        setForeground(new Color(200, 195, 185)); // Light stone text
        setOpaque(false);
        setFocusPainted(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int circleSize = 18;
        int circleY = (getHeight() - circleSize) / 2;

        // outer circle
        g2d.setColor(new Color(12, 12, 15));
        g2d.fillOval(5, circleY, circleSize, circleSize);
        
        g2d.setColor(accentColor);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawOval(5, circleY, circleSize, circleSize);

        // Inner dot if selected
        if (isSelected()) {
            g2d.setColor(accentColor);
            g2d.fillOval(9, circleY + 4, 10, 10);
        }

        // Text
        g2d.setColor(getForeground());
        g2d.setFont(getFont());
        FontMetrics fm = g2d.getFontMetrics();
        g2d.drawString(getText(), 30, (getHeight() + fm.getAscent()) / 2 - 2);
    }
}

// Dark volume label
class VolumeValueLabel extends JLabel {
    public VolumeValueLabel(String text) {
        super(text, SwingConstants.CENTER);
        setFont(new Font("Monospaced", Font.BOLD, 20));
        setForeground(new Color(210, 205, 195)); 
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // background
        g2d.setColor(new Color(20, 19, 23));
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

        // border 
        g2d.setColor(new Color(90, 85, 80));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);

        super.paintComponent(g);
    }
}

// Dark atmospheric button
class GlowButton extends JButton {
    private Color baseColor;
    private boolean isHovered = false;

    public GlowButton(String text, Color color) {
        super(text);
        this.baseColor = color;
        setFont(new Font("Serif", Font.BOLD, 22));
        setForeground(new Color(210, 205, 195)); // Stone white text
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Button background
        if (isHovered) {
            g2d.setColor(baseColor.brighter());
        } else {
            g2d.setColor(baseColor);
        }
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

        // Stone border
        if (isHovered) {
            g2d.setColor(new Color(150, 145, 135));
        } else {
            g2d.setColor(new Color(100, 95, 85));
        }
        g2d.setStroke(new BasicStroke(isHovered ? 3 : 2));
        g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);

        super.paintComponent(g);
    }
}