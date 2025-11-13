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
    
    private GameMap map;
    
    public Knight(MainApplication pf, GameMap m) {
        parentFrame = pf;
        this.map = m;
        
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
        
        int nextY = curY + vy;
        
        boolean leftFoot  = map.isSolid(curX + 10,         nextY + height); 
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
        
        curX += vx; 
        
        if (curX < 0) curX = 0;
        if (curX > 960 - width) curX = 960 - width;
        
        if (onGround) {
            if (vx == 0) {
                if (turnRight) setIdle();
                else setIdleFlip();
            } 
            else {
                if (turnRight) setRun();
                else setRunFlip();
            }
        } 
        else {
            if (turnRight) setJump();
            else setJumpFlip();
        }
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
