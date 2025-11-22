/*
 *
 * @author 6713230_Natwarit Chuboonsub
 * @author 6713229_Nutthapat Techapornhiran
 * @author 6713235_Teetath Prapasanon
 * @author 6713239_Nitich Uanjityanon
 * @author 6713243_Puttipong Chutipongwanit
 */
package Project3_6713230;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class MenuSystem extends JFrame {

    private JLabel backgroundLabel;
    private String[] backgrounds = {GameConstants.FILE_CASTLE_BG, GameConstants.FILE_HOLLOW_BG, GameConstants.FILE_FOREST_BG, GameConstants.FILE_RAIN_BG};

    private int currentBgIndex = 0;
    private Timer bgTimer;

    private int frameheight = GameConstants.FRAME_HEIGHT;
    private int framewidth = GameConstants.FRAME_WIDTH;

    // UI Components for requirements
    private JRadioButton[] difficultyRadios;
    private JComboBox<String> backgroundCombo;
    private JSlider volumeSlider;
    private JTextField playerNameField;

    public static void main(String[] args) {
        MenuSystem menu = new MenuSystem();
        menu.setVisible(true);
    }

    public MenuSystem() {
        setTitle("ROCK & SLASH - Main Menu");
        setSize(framewidth, frameheight);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        backgroundLabel = new JLabel();
        updateBackground();
        backgroundLabel.setLayout(null);

        // Animated background timer
        bgTimer = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentBgIndex = (currentBgIndex + 1) % backgrounds.length;
                updateBackground();
            }
        });
        bgTimer.start();

        // Title
        JLabel titleLabel = new JLabel();
        ImageIcon title = new ImageIcon(GameConstants.FILE_TITLE);
        titleLabel.setIcon(title);
        titleLabel.setBounds(230, -100, 500, 500);
        backgroundLabel.add(titleLabel);

        // Play Button
        JButton playButton = createMenuButton("PLAY");
        playButton.setBounds(380, 280, 200, 50);
        playButton.addActionListener(e -> startGame());
        backgroundLabel.add(playButton);

        // Options Button (Opens another dialog - Required)
        JButton optionsButton = createMenuButton("OPTIONS");
        optionsButton.setBounds(380, 350, 200, 50);
        optionsButton.addActionListener(e -> openOptions());
        backgroundLabel.add(optionsButton);

        // Credits Button (Opens another dialog - Required)
        JButton creditsButton = createMenuButton("CREDITS");
        creditsButton.setBounds(380, 420, 200, 50);
        creditsButton.addActionListener(e -> openCredits());
        backgroundLabel.add(creditsButton);

        // Exit Button
        JButton exitButton = createMenuButton("EXIT");
        exitButton.setBounds(380, 490, 200, 50);
        exitButton.addActionListener(e -> System.exit(0));
        backgroundLabel.add(exitButton);

        add(backgroundLabel);
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(getBackground());
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };

        button.setFont(new Font("Serif", Font.BOLD, 24));
        button.setForeground(Color.WHITE);

        button.setBackground(new Color(50, 50, 60, 200));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 150), 2));

        button.addMouseListener(new MouseAdapter() {
            // Handler 1: mouseEntered
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(60, 60, 80, 230));
                button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));
                button.repaint();
            }

            // Handler 2 : mouseExited
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(50, 50, 60, 200));
                button.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 150), 2));
                button.repaint();
            }
        });

        return button;
    }

    private void updateBackground() {
        Image bg = new ImageIcon(backgrounds[currentBgIndex]).getImage();
        bg = bg.getScaledInstance(framewidth, frameheight, Image.SCALE_DEFAULT);
        backgroundLabel.setIcon(new ImageIcon(bg));
    }

    private void startGame() {
        bgTimer.stop();
        dispose();
        new MainApplication();
    }

    private void openOptions() {
        JDialog optionsDialog = new JDialog(this, "Options", true);
        optionsDialog.setSize(650, 600);
        optionsDialog.setLocationRelativeTo(this);
        optionsDialog.setLayout(null);
        optionsDialog.getContentPane().setBackground(new Color(20, 20, 30));

        // --- TITLE ---
        JLabel titleLabel = new JLabel("GAME OPTIONS", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(0, 20, 650, 40);
        optionsDialog.add(titleLabel);

        int leftX = 50;
        int rightX = 360;
        int startY = 90; 

        //Player Name
        JLabel nameLabel = new JLabel("Player Name:");
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setBounds(leftX, startY, 150, 25);
        optionsDialog.add(nameLabel);

        playerNameField = new JTextField("Player1");
        playerNameField.setBounds(leftX, startY + 30, 200, 30);
        optionsDialog.add(playerNameField);

        // Difficulty Radio Buttons
        int diffY = startY + 80;
        JLabel diffLabel = new JLabel("Select Difficulty:");
        diffLabel.setForeground(Color.WHITE);
        diffLabel.setBounds(leftX, diffY, 200, 25);
        optionsDialog.add(diffLabel);

        ButtonGroup difficultyGroup = new ButtonGroup();
        String[] difficulties = {"Easy Mode", "Normal Mode", "Hard Mode", "Expert Mode", "Nightmare Mode"};
        difficultyRadios = new JRadioButton[5];

        int radioY = diffY + 30;
        for (int i = 0; i < 5; i++) {
            difficultyRadios[i] = new JRadioButton(difficulties[i]);
            difficultyRadios[i].setForeground(Color.WHITE);
            difficultyRadios[i].setBackground(new Color(20, 20, 30));
            difficultyRadios[i].setBounds(leftX + 20, radioY, 180, 25);
            difficultyGroup.add(difficultyRadios[i]);

            // --- Handler 3: ActionListener for radio buttons
            final int index = i;
            difficultyRadios[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("Difficulty selected: " + difficulties[index]);
                }
            });

            optionsDialog.add(difficultyRadios[i]);
            radioY += 35;
        }
        difficultyRadios[1].setSelected(true); // Default Normal

        int rightY = startY;

        //Background ComboBox
        JLabel bgLabel = new JLabel("Menu Background:");
        bgLabel.setForeground(Color.WHITE);
        bgLabel.setBounds(rightX, rightY, 150, 25);
        optionsDialog.add(bgLabel);

        String[] bgOptions = {"Castle", "Church", "Forest", "Rain", "Random"};
        backgroundCombo = new JComboBox<>(bgOptions);
        backgroundCombo.setBounds(rightX, rightY + 30, 200, 30);
        backgroundCombo.setSelectedIndex(4); // Default Random Cycle

        // --- Handler 4: ItemListener for ComboBox 
        backgroundCombo.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    System.out.println("Background selected: " + e.getItem());
                    int selectedIndex = backgroundCombo.getSelectedIndex();
                    if (selectedIndex < 4) {
                        bgTimer.stop();
                        currentBgIndex = selectedIndex;
                        updateBackground();
                    } else if (selectedIndex == 4) {
                        bgTimer.start();
                    } else {
                        bgTimer.stop();
                    }
                }
            }
        });
        optionsDialog.add(backgroundCombo);

        // Volume Slider
        rightY += 80; 
        JLabel volumeLabel = new JLabel("Volume:");
        volumeLabel.setForeground(Color.WHITE);
        volumeLabel.setBounds(rightX, rightY, 100, 25);
        optionsDialog.add(volumeLabel);

        volumeSlider = new JSlider(0, 100, 50);
        volumeSlider.setBounds(rightX, rightY + 30, 200, 40);
        volumeSlider.setBackground(new Color(20, 20, 30));
        volumeSlider.setForeground(Color.WHITE);
        volumeSlider.setMajorTickSpacing(25);
        volumeSlider.setPaintTicks(true);
        volumeSlider.setPaintLabels(true);
        optionsDialog.add(volumeSlider);

        // Back Button
        JButton backButton = new JButton("BACK");
        int btnWidth = 120;
        int btnHeight = 40;
        int dialogWidth = 650;
        int dialogHeight = 600;

        backButton.setBounds((dialogWidth - btnWidth) / 2 - 10, dialogHeight - 100, btnWidth, btnHeight);
        backButton.setFocusable(false);
        backButton.addActionListener(e -> optionsDialog.dispose());
        optionsDialog.add(backButton);

        optionsDialog.setVisible(true);
    }

    private void openCredits() {
        JDialog creditsDialog = new JDialog(this, "Credits", true);
        creditsDialog.setSize(500, 400);
        creditsDialog.setLocationRelativeTo(this);
        creditsDialog.setLayout(null);
        creditsDialog.getContentPane().setBackground(new Color(20, 20, 30));

        JLabel titleLabel = new JLabel("CREDITS");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(180, 20, 200, 40);
        creditsDialog.add(titleLabel);

        JTextArea creditsText = new JTextArea();
        creditsText.setText(
                "\n\n"
                + "Developed by:\n\n"
                + "6713230 - Natwarit Chuboonsub\n"
                + "6713229 - Nutthapat Techapornhiran\n"
                + "6713235 - Teetath Prapasanon\n"
                + "6713239 - Nitich Uanjityanon\n"
                + "6713243 - Puttipong Chutipongwanit\n\n"
        );
        creditsText.setFont(new Font("Monospaced", Font.PLAIN, 16));
        creditsText.setForeground(Color.WHITE);
        creditsText.setBackground(new Color(20, 20, 30));
        creditsText.setEditable(false);
        creditsText.setBounds(50, 80, 400, 220);
        creditsDialog.add(creditsText);

        JButton backButton = new JButton("BACK");
        backButton.setBounds(200, 310, 100, 40);
        backButton.addActionListener(e -> creditsDialog.dispose());
        creditsDialog.add(backButton);

        creditsDialog.setVisible(true);
    }
}