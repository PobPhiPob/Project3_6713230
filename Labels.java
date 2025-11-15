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
import java.awt.event.*;

class Knight extends JLabel {
    private MainApplication parentFrame;
    private GameIcon idleGif, runGif, jumpGif, attack1Gif, attack2Gif, attack3Gif;
    private GameIcon idleFlip, runFlip, jumpFlip, attack1Flip, attack2Flip, attack3Flip;
    
    
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
    private int attackPoint = 5;
    private int currentAttack;
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
        attack1Gif = new GameIcon(GameConstants.FILE_MC_ATTACK1).resize(65, 160);
        attack2Gif = new GameIcon(GameConstants.FILE_MC_ATTACK2).resize(65, 160);
        attack3Gif = new GameIcon(GameConstants.FILE_MC_ATTACK3).resize(65, 160);
        
        idleFlip = new GameIcon(GameConstants.FILE_MC_IDLEFLIP).resize(width, height);
        runFlip = new GameIcon(GameConstants.FILE_MC_RUNFLIP).resize(width, height);
        jumpFlip = new GameIcon(GameConstants.FILE_MC_JUMPFLIP).resize(width, height);
        attack1Flip = new GameIcon(GameConstants.FILE_MC_ATTACK1FLIP).resize(width, height);
        attack2Flip = new GameIcon(GameConstants.FILE_MC_ATTACK2FLIP).resize(width, height);
        attack3Flip = new GameIcon(GameConstants.FILE_MC_ATTACK3FLIP).resize(width, height);
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
    
    public void setAttack1() {
        setIcon(attack1Gif);
    }
    
    public void setAttack2() {
        setIcon(attack2Gif);
    }
    
    public void setAttack3() {
        setIcon(attack3Gif);
    }
        
    public void setHit(boolean h) {
        hit = h;
    }
    public void damage(int d) {
        if (hit) {
            hp -= d;
        }
    }
    public void attack() {
        if (attack) return;
        attack = true;
        switch(currentAttack) {
            case 0:
                setAttack1();
                currentAttack = 1;
                break;
            case 1:
                setAttack2(); 
                currentAttack = 2;
                break;
            case 2:
                setAttack3(); 
                currentAttack = 0;
                break;
        }
        
        new Timer(350, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                attack = false;
                
                ((Timer)e.getSource()).stop();
            }
        }).start();
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
        
        if (attack) return;
        
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
    }
    
    public void moveRight() {
        vx = speed;
        turnLeft = false;
        turnRight = true;
    }
    
    public void stopMoving() {
        vx = 0;
    }
    
    public void jump() {
        if (onGround) {
            vy = jumpStrength;
            onGround = false;
        }
    }
    
    public void setStartPosition() {
        curX = 10; 
        curY = 200;
        vx = 0;    
        vy = 0;     
        setLocation(curX, curY);
    }
}
