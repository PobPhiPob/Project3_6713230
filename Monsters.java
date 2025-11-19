/**
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

public abstract class Monsters extends JLabel{
    
    protected MainApplication parentFrame;
    protected GameMap map;
    
    protected int width;
    protected int height;
    protected int curX;
    protected int curY;
    protected int hp;
    protected int attackPoint;
    protected int speed;
    
    protected int vx = 0;
    protected int vy = 0;
    protected int gravity = GameConstants.GRAVITY;
    protected final int CHASE_RANGE = 300;
    protected final int ATTACK_RANGE = 50;
    protected boolean isAttacking = false;
    protected boolean onGround = true;
    protected boolean turnLeft = true;
    protected long deathTimeEnd = 0;
    protected long hitTime = 0;
    protected boolean isHit = false;
    protected long attackTimeEnd = 0;
    private boolean hitboxActive = false;
    
    protected JLabel attack;
    protected int atk_width;
    protected int atk_height;
    
    protected GameIcon idleGif, walkGif, deadGif, attackGif, hitGif;
    protected GameIcon idleFlip, walkFlip, deadFlip, attackFlip, hitFlip;
    

    public Monsters(MainApplication pf, GameMap m, int startX, int startY, 
                   int monsterWidth, int monsterHeight, int atkWidth, int atkHeight, 
                   int initialHP, int initialAttack, int initialSpeed) {
        
        this.parentFrame = pf;
        this.map = m;
        this.curX = startX;
        this.curY = startY;
        
        this.width = monsterWidth;
        this.height = monsterHeight;
        this.atk_width = atkWidth;
        this.atk_height = atkHeight;
        this.hp = initialHP;
        this.attackPoint = initialAttack;
        this.speed = initialSpeed;
        
        this.setLayout(null);
        
        attack = new JLabel();
        attack.setVisible(false);
        attack.setBorder(BorderFactory.createLineBorder(Color.PINK, 2));

        setBounds(curX, curY, width, height);
        setBorder(BorderFactory.createLineBorder(Color.RED, 2));
    }

    public Rectangle getHitBox()                { return new Rectangle(curX, curY, width, height); }
    public int getHp()                          { return hp; }
    public int getAttackPoint()                 { return attackPoint; }
    public boolean isAttacking()                { return isAttacking; }
    public boolean isHitboxActive()             { return hitboxActive; }
    public boolean isDead()                     { return hp <= 0; }
    public long getAttackTimeEnd()              { return attackTimeEnd; }
    public JLabel getAttackLabel()              { return attack; }
    public boolean isDeathAnimationFinished()   { return isDead() && System.currentTimeMillis() > deathTimeEnd; }
    
    // --- Movement and Physics
    public void updateLocation() {
        if (isDead()) {
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
    
    // --- Combat Logic
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
            setDead();
        }else {
            hitTime = System.currentTimeMillis() + GameConstants.DURATION_MS;
            isHit = true;
            setHit();
            
            int kb = GameConstants.KNOCKBACK;
            vx = (turnLeft ? kb : -kb);
            vy = -kb;
        }
    }
    
    // --- Behavior Logic
    public void updateBehavior(Knight player) {
        if (isDead()) {
            setDead();
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
            if (!isAttacking) {
                isAttacking = true;
                attackTimeEnd = System.currentTimeMillis() + (3*GameConstants.DURATION_MS);
            }
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
            vx = 0;
        }
        
        if (isAttacking) {
            
            long elapsedTime = attackTimeEnd - System.currentTimeMillis();
        
            if (elapsedTime >= GameConstants.DURATION_MS && 
                elapsedTime < (2 * GameConstants.DURATION_MS)) 
            {
                hitboxActive = true;
            } else {
                hitboxActive = false;
            }
            if (System.currentTimeMillis() > attackTimeEnd) {
                isAttacking = false;
                attack.setVisible(false);
                hitboxActive = false;
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

    // --- Animation Setters
    protected void setWalk() {
        this.setVisible(true);
        attack.setVisible(false);
        if (turnLeft) {
            setIcon(walkFlip);
        } else {
            setIcon(walkGif);
        }
    }
    
    protected void setIdle() {
        if (turnLeft) {
            setIcon(idleFlip);
        } else {
            setIcon(idleGif);
        }
        if(getIcon() == null) {
            setIcon(idleGif);
        }
    }
    
    public void setDead() {
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
        if (turnLeft) {
            setIcon(hitFlip);
        } else {
            setIcon(hitGif);
        }
    }
}