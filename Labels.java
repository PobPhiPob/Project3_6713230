/**
 *
 * @author 6713230_Natwarit Chuboonsub
 * @author 6713229_Nutthapat Techapornhiran
 * @author 6713235_Teetath Prapasanon
 * @author 6713239_Nitich Uanjityanon
 * @author 6713243_Puttipong Chutipongwanit
 */
package Project3_6713230;

import java.util.*;
import javax.swing.*;

class Knight extends JLabel {
    private MainApplication parentFrame;
    private GameIcon idleGif, runGif, jumpGif, idleFlip, runFlip, jumpFlip;
    
    private int width = GameConstants.MC_WIDTH;
    private int height = GameConstants.MC_HEIGHT;
    
    private int curX = 10;
    private int curY = 400;
    private int speed = 5;
    private int vx = 0;
    private int vy = 0;
    private int jumpStrength = -15;
    private int gravity = GameConstants.GRAVITY;
    private boolean onGround = true;
    private boolean turnLeft = false;
    private boolean turnRight = true;
    
    private int hp = 100;
    private int attackPoint;
    private int stamina;
    private boolean hit;
    private boolean attack;
    private boolean parry;
    
    public Knight(MainApplication pf) {
        parentFrame = pf;
        
        idleGif = new GameIcon(GameConstants.FILE_MC_IDLE).resize(width, height);
        runGif = new GameIcon(GameConstants.FILE_MC_RUN).resize(width, height);
        jumpGif = new GameIcon(GameConstants.FILE_MC_JUMP).resize(width, height);
        idleFlip = new GameIcon(GameConstants.FILE_MC_IDLEFLIP).resize(width, height);
        runFlip = new GameIcon(GameConstants.FILE_MC_RUNFLIP).resize(width, height);
        jumpFlip = new GameIcon(GameConstants.FILE_MC_JUMPFLIP).resize(width, height);
        setBounds(curX, curY, width, height);
    }
    
    public void setIdle() {
        setIcon(idleGif);
    }
    
    public void setRun() {
        setIcon(runGif);
    }
    
    public void setJump() {
        setIcon(jumpGif);
    }
    
    public void setIdleFlip() {
        setIcon(idleFlip);
    }
    
    public void setRunFlip() {
        setIcon(runFlip);
    }
    
    public void setJumpFlip() {
        setIcon(jumpFlip);
    }
        
    public void setHit(boolean h) {
        hit = h;
    }
    public void damage(int d) {
        if (hit) {
            hp -= d;
        }
    }
    public int attack() {
        return attack ? attackPoint : 0;
    }
    
    public void updateLocation() {
        vy += gravity;
        
        curX += vx;
        curY += vy;
        
        if (curY >= 400) {
            curY = 400;
            vy = 0;
            onGround = true;
        }
        
        if (vy == 0 && onGround && turnRight) setRun();
        if (vy == 0 && onGround && turnLeft) setRunFlip();
        
        if (vx == 0 && onGround && turnRight) setIdle();
        if (vx == 0 && onGround && turnLeft) setIdleFlip();
        
        setLocation(curX, curY);
    }
    
    public void moveLeft() {
        vx = -speed;
        turnLeft = true;
        turnRight = false;
        setRunFlip();
    }
    
    public void moveRight() {
        vx = speed;
        turnLeft = false;
        turnRight = true;
        setRun();
    }
    
    public void stopMoving() {
        vx = 0;
    }
    
    public void jump() {
        if (onGround) {
            vy = jumpStrength;
            onGround = false;
            if (turnLeft) setJumpFlip();
            else setJump();
        }
    }
}
