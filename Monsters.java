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
    private int attackPoint = 10;
    private int speed = 2; 
    private int vx = 0;
    private int vy = 0;
    private int gravity = GameConstants.GRAVITY;
    private final int CHASE_RANGE = 250;
    private final int ATTACK_RANGE = 30;
    private boolean isAttacking = false;
    private boolean isDead = false;
    private boolean onGround = true;
    private boolean turnLeft = true;
    private boolean deadAnimationSet = false;
    private boolean isHit = false;
    
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
    
    public Monsters(MainApplication pf, GameMap m, int startX, int startY) {
        parentFrame = pf;
        this.map  = m;
        this.curX = startX;
        this.curY = startY;
        
        idleGif    = new GameIcon(GameConstants.FILE_SKELETON_IDLE).resize(width, height);
        walkGif    = new GameIcon(GameConstants.FILE_SKELETON_WALK).resize(width, height);
        hitGif     = new GameIcon(GameConstants.FILE_SKELETON_HIT).resize(width, height);
        attackGif  = new GameIcon(GameConstants.FILE_SKELETON_ATTACK).resize(width, height);
        deadGif    = new GameIcon(GameConstants.FILE_SKELETON_DEAD).resize(width, height);

        idleFlip   = new GameIcon(GameConstants.FILE_SKELETON_IDLEFLIP).resize(width, height);
        walkFlip   = new GameIcon(GameConstants.FILE_SKELETON_WALKFLIP).resize(width, height);
        hitFlip    = new GameIcon(GameConstants.FILE_SKELETON_HITFLIP).resize(width, height);
        attackFlip = new GameIcon(GameConstants.FILE_SKELETON_ATTACKFLIP).resize(width, height);
        deadFlip   = new GameIcon(GameConstants.FILE_SKELETON_DEADFLIP).resize(width, height);

        setBounds(curX, curY, width, height);
        setIcon(idleGif);
        setBorder(BorderFactory.createLineBorder(Color.RED, 2));
    }

    public void updateLocation() {
        vy += gravity;

        int nextY = curY + vy;

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
        int checkY_top = curY + 5; 
        int checkY_bottom = curY + height - 5; 

        if (vx > 0) {
            boolean feetHit = map.isSolid(nextX + width, checkY_bottom);
            if (feetHit) {
                vx = 0; 
                turnLeft = true; 
            } else {
                curX = nextX; 
            }
        } else if (vx < 0) {
            boolean feetHit = map.isSolid(nextX, checkY_bottom);
            if (feetHit) {
                vx = 0; 
                turnLeft = false; 
            } else {
                curX = nextX; 
            }
        }

        setLocation(curX, curY);
    }
    
    public void updateBehavior(Knight player) {
        if (hp <= 0) {
            isDead = true; 
            vx = 0; 
        }
        
        if (isDead) {
            if (!deadAnimationSet) {
                setDead(); 
                deadAnimationSet = true;
            }
            return; 
        }
        
        if (isHit) {
            setHit();
            isHit = true;
            vx = 0;
            vy = 0;
            return;
        }

        int playerCenterX = player.getCurX() + player.getWidth() / 2;
        int monsterCenterX = curX + width / 2;
        int distanceX = playerCenterX - monsterCenterX;
        int absDistanceX = Math.abs(distanceX);
        int distanceY = Math.abs(player.getCurY() - curY);

        isAttacking = false;

        if (absDistanceX < ATTACK_RANGE && distanceY < height) {
            isAttacking = true;
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
            if (turnLeft) {
                vx = -speed;
            } else {
                vx = speed;
            }
        }

        if (isAttacking) {
            setAttack();
        } else {
            if (vx != 0) {
                setWalk();
            } else {
                setIdle();
            }
        }
    }

    public void damage(int d) {
        if (hp <= 0) return;
        hp -= d;
        if (hp <= 0) {
            hp = 0;
            vx = 0;
            vy = 0;
            isDead = true;
        }else {
            isHit = true;
        }
    }
    
    private void setWalk() {
        if (turnLeft) {
            setIcon(walkFlip);
        } else {
            setIcon(walkGif);
        }
    }
    
    private void setIdle() {
        if (turnLeft) {
            setIcon(idleFlip);
        } else {
            setIcon(idleGif);
        }
    }
    
    public void setDead() {
        if (turnLeft) {
            setIcon(deadFlip);
        } else {
            setIcon(deadGif);
        }
    }
    
    public void setAttack() {
        if (turnLeft) {
            setIcon(attackFlip);
        } else {
            setIcon(attackGif);
        }
    }
    
    public void setHit() {
        if (turnLeft) {
            setIcon(hitFlip);
        } else {
            setIcon(hitGif);
        }
    }
}
