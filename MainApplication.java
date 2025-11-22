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

public class MainApplication extends JFrame {
    private JPanel contentpane;
    private JLabel drawpane;
    private GameSoundEffect themeSound;
    private Timer timer;
    
    private ArrayList<GameIcon> backgroundImages = new ArrayList<>();
    private ArrayList<Monsters> monsters = new ArrayList<>();
    // ใช้ Golem แทน Boss
    private Golem golemBoss;
    private ArrayList<Rock> rocks = new ArrayList<>();
    
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
                if (e.getKeyCode() == KeyEvent.VK_SPACE)
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
        
        timer = new Timer(16, new ActionListener() {
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

                    if (!m.isDead() && mc.getHitBox().intersects(m.getHitBox())) {
                        mc.damage(m.getAttackPoint());
                    }

                    if (mc.isAttacking() && mc.getAttackHitBox().intersects(m.getHitBox())) {
                        m.damage(mc.getAttackPoint());
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
               
               for (Rock r : rocks) {
                    r.updateLocation();
                    
                    if (!r.isDestroyed() && mc.getHitBox().intersects(r.getHitBox())) {
                        mc.damage(GameConstants.GOLEM_ROCK_DMG);
                        r.destroy(); // ทำลายก้อนหินเมื่อชนผู้เล่น
                    }
                }
               
               // Cleanup Golem
                if (golemBoss != null && golemBoss.isDeathAnimationFinished()) {
                    drawpane.remove(golemBoss);
                    drawpane.remove(golemBoss.getAttackLabel());
                    drawpane.remove(golemBoss.getParticleLabel());
                    golemBoss = null;
                }
                
                // Cleanup Rocks
                rocks.removeIf(r -> {
                    if (r.isDestroyed()) {
                        drawpane.remove(r);
                        return true;
                    }
                    return false;
                });
               

                monsters.removeIf(m -> {
                    if (m.isDead() && m.isDeathAnimationFinished()) {
                        drawpane.remove(m);
                        drawpane.remove(m.getAttackLabel());
                        return true;
                    }
                    return false;
                });
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
        gameMap.drawMap(drawpane);//usefordebug
        drawpane.revalidate();//usefordebug
        drawpane.repaint();//usefordebug

        drawpane.setIcon(backgroundImages.get(currentBgIndex));
        drawpane.setLayout(null);

        mc = new Knight(currentFrame ,gameMap);

        Border hitbox = BorderFactory.createLineBorder(Color.RED, 2);
        mc.setBorder(hitbox);
        dm = new Dummy(currentFrame);
        
        drawpane.add(mc.getAttackLabel());
        drawpane.add(mc);
        drawpane.add(dm);

//	themeSound = new GameSoundEffect(GameConstants.FILE_THEME);
//      themeSound.playLoop(); themeSound.setVolume(0.2f);

        contentpane.add(drawpane);
        validate();
        repaint();
    }
    public JLabel getDrawPane() {
        return drawpane;
    }

    private void spawnMonsters() {
        for (Monsters m : monsters) {
            drawpane.remove(m.getAttackLabel());
            drawpane.remove(m);
        }
        monsters.clear();

        if (gameMap.getCurrentLevel() == 0) {
             monsters.add(new Monsters(this, gameMap, 300, 400));
             monsters.add(new Monsters(this, gameMap, 600, 400));
             golemBoss = new Golem(this, gameMap, 300, 350); // ตำแหน่ง Boss
             drawpane.add(golemBoss.getAttackLabel());
             drawpane.add(golemBoss.getParticleLabel());
             drawpane.add(golemBoss);
        } else if (gameMap.getCurrentLevel() == 1) {
             monsters.add(new Monsters(this, gameMap, 500, 300));
        }

        for (Monsters m : monsters) {
            drawpane.add(m.getAttackLabel());
            drawpane.add(m);
        }
        drawpane.revalidate(); 
        drawpane.repaint();
    }
    
    // Function สำหรับ Golem เรียกใช้เพื่อ Spawn ก้อนหิน
    public void spawnRock(int x, int y) {
        Rock newRock = new Rock(this, gameMap, x, y);
        rocks.add(newRock);
        drawpane.add(newRock);
        // ต้อง revalidate/repaint หรืออาศัย timer ในการ repaint รอบถัดไป
    }
}
