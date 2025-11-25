/**
 *
 * @author 6713230_Natwarit Chuboonsub
 * @author 6713229_Nutthapat Techapornhiran
 * @author 6713235_Teetath Prapasanon
 * @author 6713239_Nitich Uanjityanon
 * @author 6713243_Puttipong Chutipongwanit
 */
package Project3_6713230;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;
import java.util.ArrayList;
import java.util.Random;

public class GameFrame extends JFrame {

    private JPanel contentpane;
    private JLabel drawpane;
    private JLabel hpLabel, staminaLabel;
    private JProgressBar hpBar, staminaBar;
    private JLabel potionLabel, potion;
    private GameIcon potionIcon;
    private GameSoundEffect themeSound;
    private GameSoundEffect bossTheme;
    private Timer timer;
    private int staminaCounter = 0;

    private String[] backgroundFilePaths;
    private GameIcon currentBackgroundIcon;
    
    private ArrayList<Monsters> monsters = new ArrayList<>();
    private Golem golemBoss;
    private ArrayList<Rock> rocks = new ArrayList<>();
    private int currentBgIndex = 0;

    private Knight mc;
    private GameFrame currentFrame;

    private boolean movingLeft = false;
    private boolean movingRight = false;

    private GameMap gameMap = new GameMap();

    private int framewidth = GameConstants.FRAME_WIDTH;
    private int frameheight = GameConstants.FRAME_HEIGHT;

    private boolean isPaused = false;
    private JDialog pauseDialog;
    private JDialog gameOverDialog;

    private float currentVolume = 0.2f;

    public static void main(String[] args) {
        new GameFrame();
    }

    public JLabel getDrawPane() {
        return drawpane;
    }

    public int getStaminaCounter() {
        return staminaCounter;
    }

    public GameFrame() {
        setTitle("Rock & Slash Game");
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        currentFrame = this;
        contentpane = (JPanel) getContentPane();
        contentpane.setLayout(new BorderLayout());
        contentpane.setPreferredSize(new Dimension(framewidth, frameheight));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        addComponent();
        spawnMonsters();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    togglePause();
                    return;
                }
                if (isPaused) {
                    return;
                }

                if (e.getKeyCode() == KeyEvent.VK_A) {
                    movingLeft = true;
                }
                if (e.getKeyCode() == KeyEvent.VK_D) {
                    movingRight = true;
                }
                if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_W) {
                    mc.jump();
                    mc.isJumping = true;
                }
                if (e.getKeyCode() == KeyEvent.VK_J) {
                    mc.attack();
                }
                if (e.getKeyCode() == KeyEvent.VK_K || e.getKeyCode() == KeyEvent.VK_SHIFT) {
                    mc.roll();
                }
                if (e.getKeyCode() == KeyEvent.VK_L) {
                    mc.regenHp();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (isPaused) {
                    return;
                }

                if (e.getKeyCode() == KeyEvent.VK_A) {
                    movingLeft = false;
                }
                if (e.getKeyCode() == KeyEvent.VK_D) {
                    movingRight = false;
                }
                if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_W) {
                    mc.isJumping = false;
                }
            }
        });

        timer = new Timer(GameConstants.FRAME_RATE_MS, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isPaused) {
                    return;
                }

                if (movingLeft && !movingRight) {
                    mc.moveLeft();
                } else if (movingRight && !movingLeft) {
                    mc.moveRight();
                } else {
                    mc.stopMoving();
                }
                mc.updateLocation();

                for (Monsters m : monsters) {
                    m.updateBehavior(mc);
                    m.updateLocation();

                    if (m.isAttacking() && m.isHitboxActive()) {
                        Rectangle monsterAttackBox = m.getAttackLabel().getBounds();
                        Rectangle mcHitBox = mc.getHitBox();

                        if (monsterAttackBox.intersects(mcHitBox)) {
                            mc.damage(m.getAttackPoint());
                        }
                    }

                    if (!m.isDead() && mc.getHitBox().intersects(m.getHitBox())) {
                        mc.damage(m.getAttackPoint());
                    }

                    if (mc.isAttacking() && mc.getAttackHitBox().intersects(m.getHitBox())) {
                        m.damage(mc.getAttackPoint(), mc.getTurn());
                    }
                }

                if (golemBoss != null) {
                    golemBoss.updateBehavior(mc);
                    golemBoss.updateLocation();
                    // Boss Damage to Player
                    if (!golemBoss.isDead()) {
                        // Damage 1: Collision/Slide/Jump Damage (Move 2/3)
                        if (golemBoss.isCollisionDamageActive() && mc.getHitBox().intersects(golemBoss.getHitBox())) {
                            mc.damage(GameConstants.GOLEM_JUMP_SLIDE_DMG);
                        }
                        // Damage 2: Attack 1 Melee Hit
                        if (golemBoss.isAttack1Active() && mc.getHitBox().intersects(golemBoss.getAttack1HitBox())) {
                            mc.damage(GameConstants.GOLEM_MELEE_DMG);
                        }
                        // Damage 3: GroundHit Particle Damage
                        if (golemBoss.isParticleDamageActive() && mc.getHitBox().intersects(golemBoss.getParticleHitBox())) {
                            mc.damage(GameConstants.GOLEM_MELEE_DMG); // ใช้ดาเมจเบาสำหรับ particle
                        }
                        // Player Damage to Boss
                        if (mc.isAttacking() && mc.getAttackHitBox().intersects(golemBoss.getHitBox())) {
                            golemBoss.damage(mc.getAttackPoint());
                        }
                    }
                }
                ArrayList<Rock> rocksToRemove = new ArrayList<>();
                for (Rock r : rocks) {
                    r.updateLocation();
                    if (mc.isAttacking() && mc.getAttackHitBox().intersects(r.getHitBox())) {
                        if (!r.isDestroyed() && !r.isParticle()) {
                            r.destroyAndParticle();
                            System.out.println("Rock hit by Knight's attack!");
                        }
                    }
                    if (r.getHitBox().intersects(mc.getHitBox())) {
                        // Rock ชนผู้เล่น
                        if (!r.isDestroyed() && !r.isParticle()) {
                            mc.damage(GameConstants.GOLEM_ROCK_DMG); // สมมติว่ามี GOLEM_ROCK_DAMAGE
                            r.destroyAndParticle(); // เปลี่ยนเป็น Particle
                        }
                    }
                    if (r.isDestroyed()) {
                        rocksToRemove.add(r);
                    }
                }
                // Cleanup Golem
                if (golemBoss != null && golemBoss.isDeathAnimationFinished()) {
                    drawpane.remove(golemBoss);
                    drawpane.remove(golemBoss.getAttackLabel());
                    drawpane.remove(golemBoss.getParticleLabel());
                    drawpane.remove(golemBoss.getJumpLabel());
                    golemBoss = null;
                }

                // Cleanup Rocks
                for (Rock r : rocksToRemove) {
                    rocks.remove(r);
                    drawpane.remove(r);
                }

                monsters.removeIf(m -> {
                    if (m.isDead() && m.isDeathAnimationFinished()) {
                        drawpane.remove(m);
                        drawpane.remove(m.getAttackLabel());
                        return true;
                    }
                    return false;
                });

                hpLabel.setText("HP: " + mc.getHp());
                hpBar.setValue(mc.getHp());
                staminaLabel.setText("Stamina: " + mc.getStamina());
                staminaBar.setValue(mc.getStamina());
                potionLabel.setText("Potion: " + mc.getPotion());

                // Check if player died
                if (mc.getHp() <= 0) {
                    if (gameOverDialog == null && mc.isDeathAnimetionFinished()) {
                        showGameOver();
                    }
                }

                if (mc.getX() > 910) {
                    if (currentBgIndex == backgroundFilePaths.length - 1) {
                        movingRight = false; 
                        mc.updateLocation(); 
                    } else {
                        gameMap.nextLevel();
                        gameMap.clearMapVisuals(drawpane);
                        mc.setStartPosition();

                        currentBgIndex++;
                        Image newBg = new ImageIcon(backgroundFilePaths[currentBgIndex]).getImage();
                        newBg = newBg.getScaledInstance(framewidth, frameheight, Image.SCALE_SMOOTH);
                        currentBackgroundIcon = new GameIcon(newBg);

                        drawpane.setIcon(currentBackgroundIcon);

                        gameMap.drawMap(drawpane);
                        drawpane.remove(mc);
                        drawpane.add(mc);
                        spawnMonsters();
                    }
                }

                drawpane.revalidate();
                repaint();

                staminaCounter++;

                if (staminaCounter >= 188 && mc.getHp() > 0 && mc.getStamina() < 50) {
                    mc.regenStamina();
                    staminaCounter = 0;
                }
            }
        });
        timer.start();
    }

    public void addComponent() {
        drawpane = new JLabel();

        backgroundFilePaths = new String[]{
            GameConstants.FILE_BG_MAP1,
            GameConstants.FILE_BG_MAP2,
            GameConstants.FILE_BG_MAP3,
            GameConstants.FILE_BG_MAP4,
            GameConstants.FILE_BG_MAP5,
            GameConstants.FILE_BG_MAP6
        };
        
        Image initialBg = new ImageIcon(backgroundFilePaths[currentBgIndex]).getImage();
        initialBg = initialBg.getScaledInstance(framewidth, frameheight, Image.SCALE_SMOOTH);
        currentBackgroundIcon = new GameIcon(initialBg);

        gameMap.drawMap(drawpane);
        drawpane.revalidate();
        drawpane.repaint();

        drawpane.setIcon(currentBackgroundIcon);
        drawpane.setLayout(null);

        mc = new Knight(currentFrame, gameMap);

//        Border hitbox = BorderFactory.createLineBorder(Color.RED, 2);
//        mc.setBorder(hitbox);

        hpLabel = new JLabel("HP: " + mc.getHp());
        hpLabel.setBounds(10, 40, 150, 30);
        hpLabel.setForeground(Color.WHITE);
        hpLabel.setFont(new Font("Monospaced", Font.BOLD, 18));

        hpBar = new JProgressBar(SwingConstants.HORIZONTAL, 0, mc.getHp());
        hpBar.setBounds(10, 10, 200, 30);
        hpBar.setValue(mc.getHp());
        hpBar.setForeground(Color.RED);
        hpBar.setBackground(Color.DARK_GRAY);

        staminaLabel = new JLabel("Stamina: " + mc.getStamina());
        staminaLabel.setBounds(220, 40, 150, 30);
        staminaLabel.setForeground(Color.WHITE);
        staminaLabel.setFont(new Font("Monospaced", Font.BOLD, 18));

        staminaBar = new JProgressBar(SwingConstants.HORIZONTAL, 0, mc.getStamina());
        staminaBar.setBounds(220, 10, 200, 30);
        staminaBar.setValue(mc.getStamina());
        staminaBar.setForeground(Color.ORANGE);
        staminaBar.setBackground(Color.DARK_GRAY);

        potionLabel = new JLabel("Potion: " + mc.getPotion());
        potionLabel.setBounds(480, 15, 150, 30);
        potionLabel.setForeground(Color.WHITE);
        potionLabel.setFont(new Font("Monospaced", Font.BOLD, 18));

        potionIcon = new GameIcon(GameConstants.FILE_POTION).resize(40, 40);
        potion = new JLabel();
        potion.setBounds(430, 10, 40, 40);
        potion.setIcon(potionIcon);

        drawpane.add(hpLabel);
        drawpane.add(hpBar);
        drawpane.add(staminaLabel);
        drawpane.add(staminaBar);
        drawpane.add(potionLabel);
        drawpane.add(potion);

        drawpane.add(mc.getAttackLabel());
        drawpane.add(mc.getRollLabel());
        drawpane.add(mc);

        themeSound = new GameSoundEffect(GameConstants.FILE_THEME);
        themeSound.playLoop();
        themeSound.setVolume(currentVolume);

        bossTheme = new GameSoundEffect(GameConstants.FILE_BOSS_THEME);
        bossTheme.setVolume(currentVolume);

        contentpane.add(drawpane);
        validate();
        repaint();
    }

    private void spawnMonsters() {
        for (Monsters m : monsters) {
            m.stopAllSounds();
            drawpane.remove(m.getAttackLabel());
            drawpane.remove(m);
        }
        monsters.clear();

        if (gameMap.getCurrentLevel() != 5 && golemBoss != null) {
            drawpane.remove(golemBoss);
            drawpane.remove(golemBoss.getAttackLabel());
            drawpane.remove(golemBoss.getParticleLabel());
            drawpane.remove(golemBoss.getParticleLabel2());
            drawpane.remove(golemBoss.getJumpLabel());
            golemBoss = null;
            bossTheme.stop();

            for (Rock m : rocks) {
                drawpane.remove(m);
            }
            rocks.clear();
        }

        Random rand = new Random();
        final int TILE_SIZE = GameConstants.TILE_SIZE;
        final int MIN_X = 50;
        final int MAX_X = 900;
        final int MIN_Y = 50;
        final int MAX_Y = 550;
        final int AIR_MAX_Y = 300;

        if (gameMap.getCurrentLevel() == 0) {
            monsters.add(new Skeleton(this, gameMap, 300, 400));
            monsters.add(new Skeleton(this, gameMap, 600, 400));
        } else if (gameMap.getCurrentLevel() == 1) {
            monsters.add(new Skeleton(this, gameMap, 600, 300));
        } else if (gameMap.getCurrentLevel() == 5){
            themeSound.stop();
            bossTheme.playLoop();
            golemBoss = new Golem(this, gameMap, 300, 350);
        } else {
            // Random spawning for other levels
            int minMonsters = 4;
            int maxMonsters = 7;
            int numToSpawn = rand.nextInt(maxMonsters - minMonsters + 1) + minMonsters;

            for (int i = 0; i < numToSpawn; i++) {
                int monsterType = rand.nextInt(2);
                int startX = 0;
                int startY = 0;

                if (monsterType == 0) {
                    // Spawn Skeleton
                    int skeletonHeight = GameConstants.SKELETON_HEIGHT;
                    int skeletonWidth = GameConstants.SKELETON_WIDTH;

                    boolean validSpawnFound = false;
                    for (int attempt = 0; attempt < 100; attempt++) {
                        startX = rand.nextInt(MAX_X - MIN_X + 1) + MIN_X;

                        int minRow = MIN_Y / TILE_SIZE;
                        int maxRow = MAX_Y / TILE_SIZE;
                        int randomStandingRow = rand.nextInt(maxRow - minRow) + minRow;
                        int groundLevelY = randomStandingRow * TILE_SIZE;
                        startY = groundLevelY - skeletonHeight;

                        int checkY_below_ground = groundLevelY + 1;
                        boolean hasSupport = gameMap.isSolid(startX + 10, checkY_below_ground)
                                && gameMap.isSolid(startX + skeletonWidth - 10, checkY_below_ground);

                        boolean notCeiling = !gameMap.isSolid(startX + 10, startY + 1)
                                && !gameMap.isSolid(startX + skeletonWidth - 10, startY + 1);

                        boolean notInMap = !gameMap.isSolid(startX, startY)
                                && !gameMap.isSolid(startX + skeletonWidth - 1, startY)
                                && !gameMap.isSolid(startX, startY + skeletonHeight - 1)
                                && !gameMap.isSolid(startX + skeletonWidth - 1, startY + skeletonHeight - 1)
                                && !gameMap.isSolid(startX + skeletonWidth / 2, startY + skeletonHeight / 2);

                        boolean isLethalArea = !gameMap.isLethal(startX, startY)
                                && !gameMap.isLethal(startX + skeletonWidth - 1, startY)
                                && !gameMap.isLethal(startX, startY + skeletonHeight - 1)
                                && !gameMap.isLethal(startX + skeletonWidth - 1, startY + skeletonHeight - 1)
                                && !gameMap.isLethal(startX + skeletonWidth / 2, startY + skeletonHeight / 2);
                        
                        boolean isHazardArea = !gameMap.isHazard(startX, startY)
                                && !gameMap.isHazard(startX + skeletonWidth - 1, startY)
                                && !gameMap.isHazard(startX, startY + skeletonHeight - 1)
                                && !gameMap.isHazard(startX + skeletonWidth - 1, startY + skeletonHeight - 1)
                                && !gameMap.isHazard(startX + skeletonWidth / 2, startY + skeletonHeight / 2);

                        if (hasSupport && !isLethalArea && notCeiling && notInMap && isHazardArea) {
                            validSpawnFound = true;
                            break;
                        }
                    }

                    if (validSpawnFound) {
                        monsters.add(new Skeleton(this, gameMap, startX, startY));
                    }
                } else {
                    // Spawn Bat (if Bat class exists)
                    try {
                        while (true) {
                            startX = rand.nextInt(MAX_X - MIN_X + 1) + MIN_X;

                            if (rand.nextBoolean()) {
                                startY = rand.nextInt(AIR_MAX_Y - MIN_Y + 1) + MIN_Y;
                            } else {
                                startY = rand.nextInt(MAX_Y - AIR_MAX_Y + 1) + AIR_MAX_Y;
                            }

                            int checkX_center = startX + GameConstants.BAT_WIDTH / 2;
                            int checkY_center = startY + GameConstants.BAT_HEIGHT / 2;

                            boolean isColliding = gameMap.isSolid(checkX_center, checkY_center)
                                    || gameMap.isLethal(checkX_center, checkY_center) 
                                    || gameMap.isHazard(checkX_center, checkY_center);

                            if (!isColliding) {
                                break;
                            }
                        }
                        monsters.add(new Bat(this, gameMap, startX, startY));
                    } catch (Exception ex) {
                        monsters.add(new Skeleton(this, gameMap, startX, startY));
                    }
                }
            }
        }

        for (Monsters m : monsters) {
            drawpane.add(m);
            drawpane.add(m.getAttackLabel());
        }
        drawpane.revalidate();
        drawpane.repaint();
    }

    public void spawnRock(int x, int y, int vx, int vy) {
        Rock newRock = new Rock(this, gameMap, x, y, vx, vy);
        rocks.add(newRock);
        drawpane.add(newRock);
    }

    private void togglePause() {
        if (gameOverDialog != null && gameOverDialog.isVisible()) {
            return;
        }

        isPaused = !isPaused;

        if (isPaused) {
            showPauseMenu();
        }
    }

    private void showPauseMenu() {
        movingLeft = false;
        movingRight = false;
        mc.stopAllSounds();
        for (Monsters m : monsters) {
            m.stopAllSounds();
        }
        if (gameMap.getCurrentLevel() == 5) bossTheme.stop();
        else themeSound.stop();
        pauseDialog = new JDialog(this, "PAUSED", true);
        pauseDialog.setSize(400, 300);
        pauseDialog.setLocationRelativeTo(this);
        pauseDialog.setLayout(null);
        pauseDialog.setUndecorated(true);
        pauseDialog.getContentPane().setBackground(new Color(20, 20, 30, 250));

        JLabel titleLabel = new JLabel("GAME PAUSED");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(80, 30, 300, 50);
        pauseDialog.add(titleLabel);

        JButton resumeButton = createDialogButton("RESUME");
        resumeButton.setBounds(100, 100, 200, 50);
        resumeButton.addActionListener(e -> {
            isPaused = false;
            pauseDialog.dispose();
            pauseDialog = null;
            if (gameMap.getCurrentLevel() == 5) bossTheme.playLoop();
            else themeSound.playLoop();
        });
        pauseDialog.add(resumeButton);

        JButton optionsButton = createDialogButton("OPTIONS");
        optionsButton.setBounds(100, 170, 200, 50);
        optionsButton.addActionListener(e -> {
            pauseDialog.setVisible(false);
            openOptions();
            // Show pause menu again after closing options
            if (isPaused && pauseDialog != null) {
                pauseDialog.setVisible(true);
            }
        });
        pauseDialog.add(optionsButton);

        JButton mainMenuButton = createDialogButton("MAIN MENU");
        mainMenuButton.setBounds(100, 240, 200, 50);
        mainMenuButton.addActionListener(e -> {
            pauseDialog.dispose();
            pauseDialog = null;
            timer.stop();
            dispose();
            if (gameMap.getCurrentLevel() == 5) bossTheme.stop();
            themeSound.stop();
            restartGame();
            MenuSystem menu = new MenuSystem();
            menu.setVisible(true);
        });
        pauseDialog.add(mainMenuButton);

        pauseDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                isPaused = false;
                pauseDialog = null;
            }
        });

        pauseDialog.setVisible(true);
    }

    private void openOptions() {
        OptionsDialog dialog = new OptionsDialog(this, currentVolume, themeSound);
        dialog.setVisible(true);
        currentVolume = dialog.getCurrentVolume();
    }

    private void showGameOver() {
        isPaused = true;
        gameOverDialog = new JDialog(this, "GAME OVER", true);
        gameOverDialog.setSize(400, 350);
        gameOverDialog.setLocationRelativeTo(this);
        gameOverDialog.setLayout(null);
        gameOverDialog.setUndecorated(true);
        gameOverDialog.getContentPane().setBackground(new Color(30, 10, 10, 250));
        if (gameMap.getCurrentLevel() == 5) bossTheme.stop();
        else themeSound.stop();

        JLabel titleLabel = new JLabel("YOU DIED");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 40));
        titleLabel.setForeground(new Color(200, 50, 50));
        titleLabel.setBounds(100, 40, 300, 60);
        gameOverDialog.add(titleLabel);

        JLabel messageLabel = new JLabel("YYYYY Mai dai keng...");
        messageLabel.setFont(new Font("Serif", Font.ITALIC, 18));
        messageLabel.setForeground(Color.LIGHT_GRAY);
        messageLabel.setBounds(100, 110, 300, 30);
        gameOverDialog.add(messageLabel);

        JButton restartButton = createDialogButton("RESTART");
        restartButton.setBounds(100, 160, 200, 50);
        restartButton.addActionListener(e -> {
            gameOverDialog.dispose();
            gameOverDialog = null;
            restartGame();
        });
        gameOverDialog.add(restartButton);

        JButton exitButton = createDialogButton("MAIN MENU");
        exitButton.setBounds(100, 230, 200, 50);
        exitButton.addActionListener(e -> {
            gameOverDialog.dispose();
            gameOverDialog = null;
            timer.stop();
            dispose();
            MenuSystem menu = new MenuSystem();
            menu.setVisible(true);
        });
        gameOverDialog.add(exitButton);
        gameOverDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameOverDialog.setVisible(true);
                ((Timer) e.getSource()).stop();
            }
        }).start();
    }

    private void restartGame() {
        isPaused = false;
        staminaCounter = 0;
        currentBgIndex = 0;
        bossTheme = new GameSoundEffect(GameConstants.FILE_BOSS_THEME);
        bossTheme.setVolume(currentVolume);
        themeSound = new GameSoundEffect(GameConstants.FILE_THEME);
        themeSound.playLoop();
        themeSound.setVolume(currentVolume);
        
        Image initialBg = new ImageIcon(backgroundFilePaths[currentBgIndex]).getImage(); 
        initialBg = initialBg.getScaledInstance(framewidth, frameheight, Image.SCALE_SMOOTH);
        currentBackgroundIcon = new GameIcon(initialBg);

        for (Monsters m : monsters) {
            m.stopAllSounds();
            drawpane.remove(m.getAttackLabel());
            drawpane.remove(m);
        }
        monsters.clear();

        if (golemBoss != null) {
            drawpane.remove(golemBoss);
            drawpane.remove(golemBoss.getAttackLabel());
            drawpane.remove(golemBoss.getParticleLabel());
            drawpane.remove(golemBoss.getParticleLabel2());
            drawpane.remove(golemBoss.getJumpLabel());
            golemBoss = null;

            for (Rock m : rocks) {
                drawpane.remove(m);
            }
            rocks.clear();
        }

        gameMap.clearMapVisuals(drawpane);
        gameMap = new GameMap();

        movingLeft = false;
        movingRight = false;
        mc.isJumping = false;
        drawpane.remove(mc.getAttackLabel());
        drawpane.remove(mc.getRollLabel());
        drawpane.remove(mc);
        mc = new Knight(currentFrame, gameMap);
//        Border hitbox = BorderFactory.createLineBorder(Color.RED, 2);
//        mc.setBorder(hitbox);

        hpLabel.setText("HP: " + mc.getHp());
        hpBar.setValue(mc.getHp());
        staminaLabel.setText("Stamina: " + mc.getStamina());
        staminaBar.setValue(mc.getStamina());

        drawpane.setIcon(currentBackgroundIcon);
        gameMap.drawMap(drawpane);

        drawpane.add(mc.getAttackLabel());
        drawpane.add(mc.getRollLabel());
        drawpane.add(mc);

        spawnMonsters();

        drawpane.revalidate();
        drawpane.repaint();
    }

    private JButton createDialogButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(getBackground());
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };

        button.setFont(new Font("Serif", Font.BOLD, 20));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(50, 50, 60, 200));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 150), 2));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(80, 80, 100, 230));
                button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));
                button.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(50, 50, 60, 200));
                button.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 150), 2));
                button.repaint();
            }
        });
        return button;
    }
}
