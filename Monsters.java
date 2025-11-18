package Project3_6713230;

/**
 *
 * @author 6713230_Natwarit Chuboonsub
 * @author 6713229_Nutthapat Techapornhiran
 * @author 6713235_Teetath Prapasanon
 * @author 6713239_Nitich Uanjityanon
 * @author 6713243_Puttipong Chutipongwanit
 */

import javax.swing.*;
import java.awt.*;

public class Monsters extends JLabel {
    private MainApplication parentFrame;
    private GameMap map;
    
    private int width = GameConstants.SKELETON_WIDTH;
    private int height = GameConstants.SKELETON_HEIGHT;
    private int curX;
    private int curY;
    private int hp = 50;
    private int attackPoint = 5;
    private int speed = 2; 
    private int vx = 0;
    private int vy = 0;
    private int gravity = GameConstants.GRAVITY;
    private final int CHASE_RANGE = 300;
    private final int ATTACK_RANGE = 40;
    private boolean isAttacking = false;
    private boolean isDead = false;
    private boolean onGround = true;
    private boolean turnLeft = true;
    private boolean deadAnimationSet = false;
    private long deathTimeEnd = 0;
    private long hitTime = 0;
    private boolean isHit = false;
    private long attackTimeEnd = 0;
    
    private JLabel attack;
    private int atk_width = GameConstants.SKELETON_ATK_WIDTH;
    private int atk_height = GameConstants.SKELETON_ATK_HEIGHT;
    
    
    private GameIcon idleGif, walkGif, deadGif, attackGif, hitGif;
    private GameIcon idleFlip, walkFlip, deadFlip, attackFlip, hitFlip;
    
    public Rectangle getHitBox() {
        return new Rectangle(curX, curY, width, height);
    }

    public int getHp() {
        return hp;
    }
    
    public int getAttackPoint() {
        return attackPoint;
    }
    
    public boolean isAttacking() {
        return isAttacking;
    }
    
    public boolean isDead() {
        return hp <= 0;
    }
    
    public JLabel getAttackLabel() {return attack;}
    
    public Monsters(MainApplication pf, GameMap m, int startX, int startY) {
        parentFrame = pf;
        this.map  = m;
        this.curX = startX;
        this.curY = startY;
        
        idleGif    = new GameIcon(GameConstants.FILE_SKELETON_IDLE).resize(width, height);
        walkGif    = new GameIcon(GameConstants.FILE_SKELETON_WALK).resize(width, height);
        hitGif     = new GameIcon(GameConstants.FILE_SKELETON_HIT).resize(width, height);
        attackGif  = new GameIcon(GameConstants.FILE_SKELETON_ATTACK).resize(atk_width, atk_height);
        deadGif    = new GameIcon(GameConstants.FILE_SKELETON_DEAD).resize(width, height);

        idleFlip   = new GameIcon(GameConstants.FILE_SKELETON_IDLEFLIP).resize(width, height);
        walkFlip   = new GameIcon(GameConstants.FILE_SKELETON_WALKFLIP).resize(width, height);
        hitFlip    = new GameIcon(GameConstants.FILE_SKELETON_HITFLIP).resize(width, height);
        attackFlip = new GameIcon(GameConstants.FILE_SKELETON_ATTACKFLIP).resize(atk_width, atk_height);
        deadFlip   = new GameIcon(GameConstants.FILE_SKELETON_DEADFLIP).resize(width, height);
        
        attack = new JLabel();
        attack.setVisible(false);
        attack.setBorder(BorderFactory.createLineBorder(Color.PINK, 2));

        setBounds(curX, curY, width, height);
        setIcon(idleGif);
        setBorder(BorderFactory.createLineBorder(Color.RED, 2));
    }

    public void updateLocation() {
        if (isDead) {
            setDead();
            vx = 0;
            vy = 0;
            setLocation(curX, curY);
            return;
        }
        
        vy += gravity;

        int nextY = curY + vy;
        int checkY_top = curY + 5;
        int checkY_bottom = curY + height - 5; 
        
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

        boolean leftFoot  = map.isSolid(curX + 10, nextY + height);  
        boolean rightFoot = map.isSolid(curX + width - 10, nextY + height);

        if (vy >= 0 && (leftFoot || rightFoot)) {  
            vy = 0;
            onGround = true;

            int tileRow = (nextY + height) / map.TILE_SIZE;
            curY = (tileRow * map.TILE_SIZE) - height;
        }  
        else {
            curY = nextY;
            onGround = false;
        }

        int nextX = curX + vx;
        
        boolean isKnockedBack = System.currentTimeMillis() < hitTime;

        if (vx > 0) {
            boolean feetHit = map.isSolid(nextX + width, checkY_bottom);
            if (feetHit && isKnockedBack) {
                vx = 0; 
                turnLeft = true; 
            } else {
                curX = nextX; 
            }
        } else if (vx < 0) {
            boolean feetHit = map.isSolid(nextX, checkY_bottom);
            if (feetHit && isKnockedBack) {
                vx = 0; 
                turnLeft = false; 
            } else {
                curX = nextX; 
            }
        }

        setLocation(curX, curY);
    }
    
    public void updateBehavior(Knight player) {  
        if (isDead) {
            if (!deadAnimationSet) {
                setDead(); 
                deadAnimationSet = true;
            }
            return; 
        }
        
        if (isHit) {
            if (System.currentTimeMillis() < hitTime) {
                return;
            }
            isHit = false;
        }

        int playerCenterX = player.getCurX() + player.getWidth() / 2;
        int monsterCenterX = curX + width / 2;
        int distanceX = playerCenterX - monsterCenterX;
        int absDistanceX = Math.abs(distanceX);
        int distanceY = Math.abs(player.getCurY() - curY);

        isAttacking = false;

        if (absDistanceX < ATTACK_RANGE && distanceY < height) {
            isAttacking = true; 
            attackTimeEnd = System.currentTimeMillis() + GameConstants.DURATION_MS;
            vx = 0;

        } else if (absDistanceX < CHASE_RANGE && distanceY < height) {
            if (distanceX < 0) { 
                turnLeft = true;
                vx = -speed;
            } else { 
                turnLeft = false;
                vx = speed;
            }
        } else {
//            if (turnLeft) {
//                vx = -speed;
//            } else {
//                vx = speed;
//            }
            vx = 0;
        }
        
        if (isAttacking) {
            if (System.currentTimeMillis() > attackTimeEnd) {
                isAttacking = false;
                attack.setVisible(false);
            } else {
                setAttack(); 
                return;
            }
        }else {
            if (vx != 0) {
                setWalk();
            } else {
                setIdle();
            }
        }
    }

    public void damage(int d) {
        if (hp <= 0) return;
        if (System.currentTimeMillis() < hitTime){
            setHit();
            return;
        }
        hp -= d;
        if (hp <= 0) {
            hp = 0;
            vx = 0;
            vy = 0;
            isDead = true;
            setDead();
        }else {
            hitTime = System.currentTimeMillis() + GameConstants.DURATION_MS;
            isHit = true;
            setHit();
            
            int kb = 2; 
            vx = (turnLeft ? kb : -kb);
            vy = -kb;
            }
    }
    
    private void setWalk() {
        this.setVisible(true);
        attack.setVisible(false);
        if (turnLeft) {
            setIcon(walkFlip);
        } else {
            setIcon(walkGif);
        }
    }
    
    private void setIdle() {
        this.setVisible(true);
        attack.setVisible(false);
        if (turnLeft) {
            setIcon(idleFlip);
        } else {
            setIcon(idleGif);
        }
    }
    
    public void setDead() {
        this.setVisible(true);
        attack.setVisible(false);
        if (turnLeft) {
            setIcon(deadFlip);
        } else {
            setIcon(deadGif);
        }
        if (deathTimeEnd == 0) { 
            deathTimeEnd = System.currentTimeMillis() + (3*GameConstants.DURATION_MS);
        }
    }
    
    public void setAttack() {
        this.setVisible(false);
        attack.setVisible(true);
        
        if (turnLeft) {
              attack.setIcon(attackFlip);
              attack.setBounds(curX -(atk_width - width),curY - (atk_height - height),atk_width, atk_height);

        } else {
              attack.setIcon(attackGif);
              attack.setBounds(curX,curY - (atk_height - height),atk_width, atk_height);
        }
    }
    
    public void setHit() {
        this.setVisible(true);
        attack.setVisible(false);
        if (turnLeft) {
            setIcon(hitFlip);   
        } else {
            setIcon(hitGif);
        }
    }
    
    public boolean isDeathAnimationFinished() {
        return isDead && System.currentTimeMillis() > deathTimeEnd;
    }
}
