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
    private int curY = 330;
    private int speed = 5;
    private int vx = 0;
    private int vy = 0;
    private int jumpStrength = -15;
    private int gravity = GameConstants.GRAVITY;
    private boolean onGround = true;
    
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
        //idleFlip = idleGif.flip(width, height);
        //runFlip = runGif.flip(width, height);
        //jumpFlip = jumpGif.flip(width, height);
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
        
        if (vx == 0 && onGround) setIdle();
        
        if (curY >= 330) {
            curY = 330;
            vy = 0;
            onGround = true;
        }
        setLocation(curX, curY);
    }
    
    public void moveLeft() {
        vx = -speed;
        setRun();
    }
    
    public void moveRight() {
        vx = speed;
        setRun();
    }
    
    public void stopMoving() {
        vx = 0;
        setIdle();
    }
    
    public void jump() {
        if (onGround) {
            vy = jumpStrength;
            onGround = false;
            setJump();
        }
    }
}
