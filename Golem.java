package Project3_6713230;

/**
 *
 * @author 6713230_Natwarit Chuboonsub
 * @author 6713229_Nutthapat Techapornhiran
 * @author 6713235_Teetath Prapasanon
 * @author 6713239_Nitich Uanjityanon
 * @author 6713243_Puttipong Chutipongwanit
 *
 * Golem Boss: State Machine & Fair Play Logic
 */

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Golem extends JLabel {
    private MainApplication parentFrame;
    private GameMap map;
    private Random rand = new Random();
    
    //----- Golem Stats
    private int width = GameConstants.GOLEM_WIDTH;
    private int height = GameConstants.GOLEM_HEIGHT;
    private int curX;
    private int curY;
    private int hp = GameConstants.GOLEM_MAX_HP;
    private int speed = 2; 
    private int vx = 0;
    private int vy = 0;
    private int gravity = GameConstants.GRAVITY;
    private boolean onGround = true;
    private boolean turnLeft = true;
    private boolean lockedDirection = true;
    private boolean isDead = false;
    private long deathTimeEnd = 0;

    //----- Attack Constants and State
    private final int MELEE_RANGE = 70;      // ระยะตีธรรมดา
    private final int JUMP_RANGE = 350;      // ระยะกระโดด
    private final int SLIDE_SPEED = 10;
    private final int JUMP_STRENGTH = -24;
    private final long PRESLIDE_CHARGE_TIME = 500;
    
    private enum GolemState {
        IDLE, ATTACK1, PREJUMP, ONAIR, FALL, GROUNDHIT, PRESLIDE, SLIDE, SLIDE_END, RECHARGE, DEAD
    }
    private GolemState currentState = GolemState.IDLE;
    private long lastActionTime = 0;
    private final long RECHARGE_DURATION = GameConstants.GOLEM_RECHARGE_TIME;
    
    //----- Attack Animation Labels/Icons
    private JLabel attackLabel; // ใช้สำหรับท่าโจมตีที่ขยายขนาด
    private JLabel particleLabel; // สำหรับ GroundHit particle
    private JLabel particleLabel2;
    private JLabel jumpLabel;
    private int atk_width = GameConstants.GOLEM_ATK_WIDTH;
    private int atk_height = GameConstants.GOLEM_ATK_HEIGHT;
    private int jump_width = GameConstants.GOLEM_JUMP_WIDTH;
    private int jump_height = GameConstants.GOLEM_JUMP_HEIGHT;
    
    private GameIcon idleGif, deadGif, hitGif;
    private GameIcon idleFlip, deadFlip, hitFlip;
    
    // Move 1
    private GameIcon attack1Gif, attack1Flip;
    
    // Move 2
    private GameIcon preJumpGif, onAirGif, fallGif, groundHitGif, particleGif;
    private GameIcon preJumpFlip, onAirFlip, fallFlip, groundHitFlip, particleFlip, particleSlide, particleSlideFlip;
    
    // Move 3
    private GameIcon slideGif, slideFlip;
    
    public Golem(MainApplication pf, GameMap m, int startX, int startY) {
        parentFrame = pf;
        this.map  = m;
        this.curX = startX;
        this.curY = startY;
        
        // Load Golem Sprites (assuming Flip versions exist/can be flipped)
        idleGif = new GameIcon(GameConstants.FILE_GOLEM_IDLE).resize(width, height);
        deadGif = new GameIcon(GameConstants.FILE_GOLEM_DEAD).resize(width, height);
        hitGif  = new GameIcon(GameConstants.FILE_GOLEM_HIT_SPRITE).resize(width, height);

        
        // Move 1: Attack1
        attack1Gif  = new GameIcon(GameConstants.FILE_GOLEM_ATTACK1).resize(atk_width, atk_height);
        attack1Flip = new GameIcon(GameConstants.FILE_GOLEM_ATTACK1FLIP).resize(atk_width, atk_height);
        
        // Move 2: Jump Slam
        preJumpGif  = new GameIcon(GameConstants.FILE_GOLEM_PREJUMP).resize(jump_width, jump_height);
        onAirGif    = new GameIcon(GameConstants.FILE_GOLEM_ONAIR).resize(jump_width, jump_height);
        fallGif     = new GameIcon(GameConstants.FILE_GOLEM_FALL).resize(jump_width, jump_height);
        groundHitGif = new GameIcon(GameConstants.FILE_GOLEM_GROUNDHIT).resize(width, height);
        particleGif  = new GameIcon(GameConstants.FILE_GOLEM_PARTICLE).resize(atk_width, atk_height / 2);
        
        // Move 3: Slide
        slideGif    = new GameIcon(GameConstants.FILE_GOLEM_SLIDE).resize(width, height);
        particleSlide = new GameIcon(GameConstants.FILE_GOLEM_SLIDEPARTICLE).resize(width, height);
        particleSlideFlip = new GameIcon(GameConstants.FILE_GOLEM_SLIDEPARTICLEFLIP).resize(width, height);
        
        // Setup auxiliary labels
        attackLabel = new JLabel();
        attackLabel.setVisible(false);
        attackLabel.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2)); // debug
        
        jumpLabel = new JLabel();
        jumpLabel.setVisible(false);
        jumpLabel.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2)); // debug
        
        particleLabel = new JLabel();
        particleLabel.setVisible(false);
        particleLabel.setBorder(BorderFactory.createLineBorder(Color.CYAN, 2)); // debug
        
        particleLabel2 = new JLabel(); // เพิ่มบรรทัดนี้
        particleLabel2.setVisible(false);
        particleLabel2.setBorder(BorderFactory.createLineBorder(Color.MAGENTA, 2)); // debug
        
        setBounds(curX, curY, width, height);
        setIcon(idleGif);
        setBorder(BorderFactory.createLineBorder(Color.RED, 2)); // debug
        
        parentFrame.getDrawPane().add(attackLabel);
        parentFrame.getDrawPane().add(particleLabel);
        parentFrame.getDrawPane().add(particleLabel2);
        parentFrame.getDrawPane().add(jumpLabel);
    }
    
    public Rectangle getHitBox() {
        return new Rectangle(curX, curY, width, height);
    }

    // Hitbox สำหรับการโจมตีระยะประชิด (Move 1)
    public Rectangle getAttack1HitBox() {
        if (turnLeft) {
            return new Rectangle(curX - (atk_width - width), curY, atk_width, height);
        } else {
            return new Rectangle(curX, curY, atk_width, height);
        }
    }

    // Hitbox สำหรับ GroundHit Particle (Move 2)
    public Rectangle getParticleHitBox() {
        if (particleLabel.isVisible()) {
             // กำหนดพื้นที่ดาเมจจากแรงสั่นสะเทือนให้กว้างกว่าตัว Golem
             return new Rectangle(curX - 50, curY + height - 10, width + 100, atk_height / 2); 
        }
        return new Rectangle(0, 0, 0, 0);
    }
    
    public JLabel getAttackLabel() { return attackLabel; }
    public JLabel getParticleLabel() { return particleLabel; }
    public JLabel getJumpLabel() { return jumpLabel; }
    public int getHp() { return hp; }
    public boolean isDead() { return isDead; }

    // Logic การรับดาเมจ
    public void damage(int d) {
        if (isDead) return;
        hp -= d;
        if (hp <= 0) {
            hp = 0;
            isDead = true;
            currentState = GolemState.DEAD;
        } else {
            // ตั้งสถานะ Hit และ Knockback ชั่วคราว (ถ้าต้องการ)
            // เช่น currentState = GolemState.HIT;
        }
    }
    
    public void updateLocation() {
        if (isDead) {
            vx = 0;
            vy = 0;
            setDead();
            setLocation(curX, curY);
            return;
        }
        
        // Gravity
        vy += gravity;

        int nextY = curY + vy;
        int checkY_top = curY + 5;
        
        // Collision Up (Head hit)
        if (vy < 0) {
            checkY_top = nextY;
            boolean headLeftHit = map.isSolid(curX + 10, checkY_top); 
            boolean headRightHit = map.isSolid(curX + width - 10, checkY_top); 

            if (headLeftHit || headRightHit) {
                vy = 0;
                int tileRow = checkY_top / map.TILE_SIZE;
                curY = (tileRow + 1) * map.TILE_SIZE;
                nextY = curY;
            }
        }

        // Collision Down (Foot hit)
        boolean leftFoot  = map.isSolid(curX + 10, nextY + height);  
        boolean rightFoot = map.isSolid(curX + width - 10, nextY + height);

        if (vy >= 0 && (leftFoot || rightFoot)) {   // ถ้ามีความเร็วลง และ เท้าแตะพื้น
            if (!onGround) {
                // ถ้าเพิ่งลงพื้น
                if (currentState == GolemState.FALL) {
                    currentState = GolemState.GROUNDHIT;
                    lastActionTime = System.currentTimeMillis();
                } else if (currentState != GolemState.GROUNDHIT) {
                    currentState = GolemState.IDLE;
                }
            }
            vy = 0;
            onGround = true;

            int tileRow = (nextY + height) / map.TILE_SIZE;
            curY = (tileRow * map.TILE_SIZE) - height;
        }  
        else { // ถ้ามีความเร็วขึ้น
            curY = nextY;
            onGround = false;
            if (currentState == GolemState.PREJUMP) {
                currentState = GolemState.ONAIR;
            } else if (currentState == GolemState.ONAIR && vy > 0) {
                currentState = GolemState.FALL;
            }
        }
        
        // X movement and side collision (simplified for Boss)
        curX += vx;
        
        // Clamp X to screen bounds
        if (curX < 0) curX = 0;
        if (curX > GameConstants.FRAME_WIDTH - width) curX = GameConstants.FRAME_WIDTH - width;

        setLocation(curX, curY);
        updateVisuals();
    }
    
    public void updateBehavior(Knight player) {  
        if (isDead) {
            currentState = GolemState.DEAD;
            return; 
        }
        
        int playerCenterX = player.getCurX() + player.getWidth() / 2;
        int monsterCenterX = curX + width / 2;
        int distanceX = playerCenterX - monsterCenterX;
        int absDistanceX = Math.abs(distanceX);
        
        // ตั้งค่าทิศทาง
        if (currentState != GolemState.ONAIR && currentState != GolemState.FALL) {
        turnLeft = distanceX < 0;
        }

        // Boss State Machine
        switch (currentState) {
            case IDLE:
                vx = 0;
                // ตรวจสอบ Cooldown ก่อนเริ่มท่าใหม่
                if (System.currentTimeMillis() > lastActionTime + RECHARGE_DURATION) {
                    // Boss Attack Logic (Fair Play)
                    if (absDistanceX < MELEE_RANGE) {
                        currentState = GolemState.ATTACK1; // Move 1: Melee
                    } else if (absDistanceX < JUMP_RANGE && onGround) {
                        currentState = GolemState.PREJUMP; // Move 2: Jump Slam
                    } else {
                        currentState = GolemState.SLIDE; // Move 3: Slide (ใช้เมื่อผู้เล่นอยู่ไกล)
                    }
                    lastActionTime = System.currentTimeMillis(); // Reset timer for new action
                }
                break;
                
            case ATTACK1:
                vx = 0;
                if (System.currentTimeMillis() > lastActionTime+920) { // ระยะเวลา Animation
                    currentState = GolemState.RECHARGE;
                    lastActionTime = System.currentTimeMillis();
                }
                break;

            case PREJUMP: // Move 2: เตรียมกระโดด (Charge up)
                vx = 0;
                if (System.currentTimeMillis() > lastActionTime + 900) { // 0.3s Charge
                    lockedDirection = turnLeft;
                    vx = turnLeft ? -5 : 5; // กำหนดความเร็วไปทางผู้เล่นเล็กน้อย
                    vy = JUMP_STRENGTH;
                    lastActionTime = System.currentTimeMillis();
                    // State จะเปลี่ยนไป ONAIR ใน updateLocation เมื่อหลุดจากพื้น
                }
                break;

            case ONAIR: // Move 2: ลอยกลางอากาศ
            case FALL: // Move 2: ตก
                // ยังคงรักษาทิศทางเดิม
                vx = (lockedDirection ? -5 : 5); 
                break;

            case GROUNDHIT: // Move 2: กระแทกพื้น (Particle damage, Rock spawn)
                vx = 0;
                if (System.currentTimeMillis() > lastActionTime + 100) { // Rock spawn delay
                    // SPawn Rocks 3-5 ก้อน
                    for (int i = 0; i < 4; i++) {
                        int rockX = curX + (i * 100) + rand.nextInt(100) - 150; // สุ่มช่วงการตก
                        //parentFrame.spawnRock(rockX, 0);
                    }
                }
                if (System.currentTimeMillis() > lastActionTime + 500) { // ระยะเวลา Particle
                    currentState = GolemState.RECHARGE;
                    lastActionTime = System.currentTimeMillis();
                }
                break;
                
            case PRESLIDE:
                vx = 0; // ต้องหยุดนิ่งเพื่อเตือนผู้เล่น
                // Lock ทิศทางการพุ่งเมื่อชาร์จครบ
                if (System.currentTimeMillis() > lastActionTime + PRESLIDE_CHARGE_TIME) { 
                    lockedDirection = turnLeft;
                    currentState = GolemState.SLIDE;
                    lastActionTime = System.currentTimeMillis();
                }
                break;
                
            case SLIDE: // Move 3: พุ่ง
                // พุ่งเร็วไปทางผู้เล่น
                vx = turnLeft ? -SLIDE_SPEED : SLIDE_SPEED;
                if (System.currentTimeMillis() > lastActionTime + 800) { // 0.8s Slide Duration
                    currentState = GolemState.SLIDE_END;
                    lastActionTime = System.currentTimeMillis();
                }
                break;
                
            case SLIDE_END: // Move 3: หยุดพุ่ง
                vx = 0;
                if (System.currentTimeMillis() > lastActionTime + 100) { // หยุดชั่วครู่
                    currentState = GolemState.RECHARGE;
                    lastActionTime = System.currentTimeMillis();
                }
                break;

            case RECHARGE: // พัก (Fair Play Logic)
                vx = 0;
                if (System.currentTimeMillis() > lastActionTime + RECHARGE_DURATION) {
                    currentState = GolemState.IDLE; // กลับไป IDLE เพื่อเลือกท่าใหม่
                }
                break;
                
            case DEAD:
                vx = 0;
                if (deathTimeEnd == 0) {
                    deathTimeEnd = System.currentTimeMillis() + (3*GameConstants.DURATION_MS);
                }
                break;
        }
    }
    
    // Logic การแสดงผล Sprite/Animation
    private void updateVisuals() {
        // ซ่อน Attack และ Particle Labels ก่อน
        attackLabel.setVisible(false);
        particleLabel.setVisible(false);
        particleLabel2.setVisible(false);
        jumpLabel.setVisible(false);
        this.setVisible(true);

        switch (currentState) {
            case IDLE:
                this.setVisible(true);
                setIcon(idleGif);
            case RECHARGE:
                this.setVisible(true);
                setIcon(idleGif);
                break;
            case ATTACK1:
                setAttackVisual(turnLeft ? attack1Flip : attack1Gif);
                break;
            case PREJUMP:
                this.setVisible(false);
                jumpLabel.setVisible(true);
                jumpLabel.setIcon(preJumpGif);
                jumpLabel.setBounds(curX , curY - (jump_height - height) , jump_width, jump_height);
                break;
            case ONAIR:
                this.setVisible(false); // <-- ใส่ผิดที่!
                 jumpLabel.setVisible(true);
                jumpLabel.setIcon(onAirGif);
                 jumpLabel.setBounds(curX, curY - (jump_height - height), jump_width, jump_height);
                break;
            case FALL:
                this.setVisible(false); // ซ่อนตัว Golem หลัก
                jumpLabel.setVisible(true);
                jumpLabel.setIcon(fallGif);
                jumpLabel.setBounds(curX, curY - (jump_height - height), jump_width, jump_height);
                break;
            case GROUNDHIT:
                this.setVisible(true);
                setIcon(groundHitGif);
                // แสดง Ground Particle
                particleLabel.setVisible(true);
                particleLabel.setIcon(particleGif);
                // ตำแหน่ง Particle ที่พื้น (ต้องอยู่ใต้ตัว Golem)
                particleLabel.setBounds(curX - 50, curY + height - (atk_height/2) + 20, atk_width + 100, atk_height / 2);
                break;
            case PRESLIDE:
                this.setVisible(true);
                setIcon(idleGif); // ใช้ภาพ IDLE เพื่อแสดงว่ากำลังชาร์จ
                break;
            case SLIDE:
                this.setVisible(true);
                setIcon(slideGif);
                // TODO: สามารถเพิ่ม Slide Particle Label ได้ที่นี่
                particleLabel.setVisible(true);
                particleLabel.setIcon(turnLeft ? particleSlideFlip:particleSlide);
                 particleLabel.setVisible(true);
                particleLabel.setIcon(turnLeft ? particleSlideFlip : particleSlide);
    
                // แสดง Slide Particle ที่มือขวา
                particleLabel2.setVisible(true);
                particleLabel2.setIcon(turnLeft ? particleSlideFlip : particleSlide);

                if (turnLeft) {
                    // พุ่งไปทางซ้าย
                    particleLabel.setBounds(curX + 30 , curY + height - 25, width, 35);  // มือซ้าย
                    particleLabel2.setBounds(curX + 190, curY + height - 25, width, 35); // มือขวา
                } else {
                    // พุ่งไปทางขวา
                    particleLabel.setBounds(curX - 180, curY + height - 25, width, 35);  // มือซ้าย
                    particleLabel2.setBounds(curX , curY + height - 25, width, 35); // มือขวา
                }
                break;
            case SLIDE_END:
                this.setVisible(true);
                setIcon(slideGif); // ค้างภาพ Slide ก่อนพัก
                break;
            case DEAD:
                setDead();
                break;
        }
    }
    
    // Helper function สำหรับแสดงผลท่าโจมตีที่ขยายขนาด
    private void setAttackVisual(GameIcon icon) {
        this.setVisible(false);
        attackLabel.setVisible(true);
        attackLabel.setIcon(icon);
        
        if (turnLeft) {
            attackLabel.setBounds(curX -(atk_width - width), curY - (atk_height - height), atk_width, atk_height);
        } else {
            attackLabel.setBounds(curX, curY - (atk_height - height), atk_width, atk_height);
        }
    }
    
    // สำหรับ Golem
    private void setDead() {
        this.setVisible(true);
        attackLabel.setVisible(false);
        particleLabel.setVisible(false);
        setIcon(deadGif);
        if (deathTimeEnd == 0) { 
            deathTimeEnd = System.currentTimeMillis() + (3*GameConstants.DURATION_MS);
        }
    }
    
    public boolean isDeathAnimationFinished() {
        return isDead && System.currentTimeMillis() > deathTimeEnd;
    }
    
    // ตรวจสอบว่า Golem กำลังทำดาเมจจากการชน (Move 2, 3)
    public boolean isCollisionDamageActive() {
        return currentState == GolemState.ONAIR || currentState == GolemState.FALL || 
               currentState == GolemState.SLIDE;
    }
    
    // ตรวจสอบว่า Golem กำลังใช้ท่าโจมตีระยะประชิด (Move 1)
    public boolean isAttack1Active() {
        return currentState == GolemState.ATTACK1;
    }

    // ตรวจสอบว่า Ground Particle ทำดาเมจ (Move 2)
    public boolean isParticleDamageActive() {
        return currentState == GolemState.GROUNDHIT;
    }
}