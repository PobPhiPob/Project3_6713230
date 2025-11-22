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

public class MainApplication extends JFrame {
    private JPanel contentpane;
    private JLabel drawpane;
    private JLabel hpLabel, staminaLabel;
    private JProgressBar hpBar, staminaBar;
    private GameSoundEffect themeSound;
    private Timer timer;
    private int counter = 0;
    
    private ArrayList<GameIcon> backgroundImages = new ArrayList<>();
    private ArrayList<Monsters> monsters = new ArrayList<>();
    private int currentBgIndex = 0;
    
    private Knight mc;
    private Dummy dm;
    private MainApplication currentFrame;
    
    private boolean movingLeft = false; // fix character movement bug when release key
    private boolean movingRight = false;
    
    private GameMap gameMap = new GameMap();
    
    private int framewidth  = GameConstants.FRAME_WIDTH;
    private int frameheight = GameConstants.FRAME_HEIGHT;
    
    public static void main(String[] args) {
        new MainApplication();
    }

    public Knight getMc() {
        return mc;
    }

    public Dummy getDm() {
        return dm;
    }
    
    public MainApplication() {
        setTitle("Game Game");
        setResizable(false);
	setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );
        currentFrame = this;
        contentpane = (JPanel)getContentPane();
	contentpane.setLayout( new BorderLayout() );
        contentpane.setPreferredSize(new Dimension(framewidth, frameheight));
        pack();
        setLocationRelativeTo(null);
	setVisible(true);
        
        addComponent();
        spawnMonsters();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_A)
                    movingLeft = true;
                if (e.getKeyCode() == KeyEvent.VK_D)
                    movingRight = true;
                if (e.getKeyCode() == KeyEvent.VK_W)
                    mc.jump();
                if (e.getKeyCode() == KeyEvent.VK_J)
                    mc.attack();
                if (e.getKeyCode() == KeyEvent.VK_K)
                    mc.roll();
                if (e.getKeyCode() == KeyEvent.VK_L)
                    mc.regenHp();
            }
            
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_A)
                    movingLeft = false;
                if (e.getKeyCode() == KeyEvent.VK_D)
                    movingRight = false;
            }
        });
        
        timer = new Timer(GameConstants.FRAME_RATE_MS, new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
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

                                System.out.println("MC hit by Monster! HP left: " + mc.getHp());
                            }

                    }
                    if (!m.isDead() && mc.getHitBox().intersects(m.getHitBox())) {
                        mc.damage(m.getAttackPoint());
                        System.out.println("MC body-collided! HP left: " + mc.getHp());
                    }

                    if (mc.isAttacking() && mc.getAttackHitBox().intersects(m.getHitBox())) {
                        m.damage(mc.getAttackPoint(), mc.getTurn());
                    }
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
                
               if (mc.getX() > 910) { 
                    gameMap.nextLevel();
                    gameMap.clearMapVisuals(drawpane);
                    mc.setStartPosition();
                    currentBgIndex++;
                    if (currentBgIndex >= backgroundImages.size()) {
                        currentBgIndex = 0;
                    }
                    drawpane.setIcon(backgroundImages.get(currentBgIndex));
                    gameMap.drawMap(drawpane);//usefordebug
                    drawpane.remove(mc);//usefordebug
                    drawpane.add(mc);//usefordebug
                    spawnMonsters();
               }
               drawpane.revalidate();
               repaint();
               if (counter == 150) {
                   mc.regenHp();
                   mc.regenStamina();
                   counter = 0;
               } else counter++;
               //System.out.println("Counter: " + counter);
           }
        });
        timer.start();
    }
    
    public void addComponent() {
        //backgroundImg  = new GameIcon(GameConstants.FILE_BG).resize(framewidth, frameheight);
	drawpane = new JLabel();
        
        Image bg1 = new ImageIcon(GameConstants.FILE_BG).getImage();
        bg1 = bg1.getScaledInstance(framewidth, frameheight, Image.SCALE_SMOOTH);
        backgroundImages.add(new GameIcon(bg1));
        Image bg2 = new ImageIcon(GameConstants.FILE_BG_MAP2).getImage();
        bg2 = bg2.getScaledInstance(framewidth, frameheight, Image.SCALE_SMOOTH);
        backgroundImages.add(new GameIcon(bg2));
        Image bg3 = new ImageIcon(GameConstants.FILE_BG_MAP3).getImage();
        bg3 = bg3.getScaledInstance(framewidth, frameheight, Image.SCALE_SMOOTH);
        backgroundImages.add(new GameIcon(bg3));
        Image bg4 = new ImageIcon(GameConstants.FILE_BG_MAP4).getImage();
        bg4 = bg4.getScaledInstance(framewidth, frameheight, Image.SCALE_SMOOTH);
        backgroundImages.add(new GameIcon(bg4));
        Image bg5 = new ImageIcon(GameConstants.FILE_BG_MAP5).getImage();
        bg5 = bg5.getScaledInstance(framewidth, frameheight, Image.SCALE_SMOOTH);
        backgroundImages.add(new GameIcon(bg5));
        Image bg6 = new ImageIcon(GameConstants.FILE_BG_MAP6).getImage();
        bg6 = bg6.getScaledInstance(framewidth, frameheight, Image.SCALE_SMOOTH);
        backgroundImages.add(new GameIcon(bg6));
        gameMap.drawMap(drawpane);//usefordebug
        drawpane.revalidate();//usefordebug
        drawpane.repaint();//usefordebug

        drawpane.setIcon(backgroundImages.get(currentBgIndex));
        drawpane.setLayout(null);

        mc = new Knight(currentFrame ,gameMap);

        Border hitbox = BorderFactory.createLineBorder(Color.RED, 2);
        mc.setBorder(hitbox);
        dm = new Dummy(currentFrame);
        
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
        staminaLabel.setBounds(10, 110, 150, 30);
        staminaLabel.setForeground(Color.WHITE);
        staminaLabel.setFont(new Font("Monospaced", Font.BOLD, 18));

        staminaBar = new JProgressBar(SwingConstants.HORIZONTAL, 0, mc.getStamina());
        staminaBar.setBounds(10, 80, 200, 30);
        staminaBar.setValue(mc.getStamina());
        staminaBar.setForeground(Color.ORANGE);
        staminaBar.setBackground(Color.DARK_GRAY);

        drawpane.add(hpLabel);
        drawpane.add(hpBar);
        drawpane.add(staminaLabel);
        drawpane.add(staminaBar);

        drawpane.add(mc.getAttackLabel());
        drawpane.add(mc.getRollLabel());
        drawpane.add(mc);
        drawpane.add(dm);

	themeSound = new GameSoundEffect(GameConstants.FILE_THEME);
      themeSound.playLoop(); themeSound.setVolume(0.2f);

        contentpane.add(drawpane);
        validate();
        repaint();
    }

    private void spawnMonsters() {
        for (Monsters m : monsters) {
            drawpane.remove(m.getAttackLabel());
            drawpane.remove(m);
        }
        monsters.clear();

        Random rand = new Random();
        final int TILE_SIZE = GameConstants.TILE_SIZE;
        final int MIN_X = 50;
        final int MAX_X = 900;
        final int MIN_Y = 50;
        final int MAX_Y = 550; 
        final int AIR_MAX_Y = 300; 

        if (gameMap.getCurrentLevel() == 0 || gameMap.getCurrentLevel() == 6) {
            return;
        } else {

            int minMonsters = 3;
            int maxMonsters = 6;
            int numToSpawn = rand.nextInt(maxMonsters - minMonsters + 1) + minMonsters;

            for (int i = 0; i < numToSpawn; i++) {

                int monsterType = rand.nextInt(2);
                int startX = 0;
                int startY = 0;

                if (monsterType == 0) {
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
                        boolean hasSupport = gameMap.isSolid(startX + 10, checkY_below_ground) && 
                                             gameMap.isSolid(startX + skeletonWidth - 10, checkY_below_ground);

                        boolean notCeiling = !gameMap.isSolid(startX + 10, startY + 1) && 
                                             !gameMap.isSolid(startX + skeletonWidth - 10, startY + 1);
                        
                        boolean notInMap = !gameMap.isSolid(startX/2 + 10, startY/2 + 1) && 
                                           !gameMap.isSolid((startX + skeletonWidth)/2 - 10, startY/2 + 1);

                        boolean isLethalArea = gameMap.isLethal(startX + 10, groundLevelY) || 
                                               gameMap.isLethal(startX + skeletonWidth - 10, groundLevelY);

                        if (hasSupport && !isLethalArea && notCeiling && notInMap) {
                            validSpawnFound = true;
                            break;
                        }
                    }

                    if (validSpawnFound) {
                        monsters.add(new Skeleton(this, gameMap, startX, startY));
                    }

                } else {

                    while (true){
                        startX = rand.nextInt(MAX_X - MIN_X + 1) + MIN_X;

                        if (rand.nextBoolean()) {
                            startY = rand.nextInt(AIR_MAX_Y - MIN_Y + 1) + MIN_Y;
                        } else {
                            startY = rand.nextInt(MAX_Y - AIR_MAX_Y + 1) + AIR_MAX_Y;
                        }

                        int checkX_center = startX + GameConstants.BAT_WIDTH / 2;
                        int checkY_center = startY + GameConstants.BAT_HEIGHT / 2;

                        boolean isColliding = gameMap.isSolid(checkX_center, checkY_center) ||
                                              gameMap.isLethal(checkX_center, checkY_center);

                        if (!isColliding) {
                            break;
                        }
                    } 

                    monsters.add(new Bat(this, gameMap, startX, startY));
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
}
