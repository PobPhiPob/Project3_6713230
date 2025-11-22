package Project3_6713230;

import javax.swing.*;
import java.awt.*;

public class Rock extends JLabel {
    private GameMap map;
    private MainApplication parentFrame;
    
    private int width = GameConstants.ROCK_WIDTH;
    private int height = GameConstants.ROCK_HEIGHT;
    private int curX;
    private int curY;
    private int vy = 0;
    private int gravity = GameConstants.GRAVITY;
    private boolean isDestroyed = false;
    
    private GameIcon rockIcon;
    
    public Rock(MainApplication pf, GameMap m, int startX, int startY) {
        parentFrame = pf;
        this.map  = m;
        this.curX = startX;
        this.curY = startY;
        
        // โหลด icon ก้อนหิน
        rockIcon = new GameIcon(GameConstants.FILE_GOLEM_ROCK).resize(width, height);
        
        setBounds(curX, curY, width, height);
        setIcon(rockIcon);
        // setBorder(BorderFactory.createLineBorder(Color.GREEN, 1)); // for debug
    }
    
    public Rectangle getHitBox() {
        return new Rectangle(curX, curY, width, height);
    }
    
    public boolean isDestroyed() {
        return isDestroyed;
    }
    
    public void destroy() {
        isDestroyed = true;
    }

    public void updateLocation() {
        if (isDestroyed) return;
        
        vy += gravity;

        int nextY = curY + vy;
        
        // Check collision with floor/tiles
        boolean leftFoot  = map.isSolid(curX + 5, nextY + height);  
        boolean rightFoot = map.isSolid(curX + width - 5, nextY + height);

        if (vy >= 0 && (leftFoot || rightFoot)) {  
            // ชนพื้นแล้ว ทำลายตัวเอง
            isDestroyed = true;
        } else if (curY > GameConstants.FRAME_HEIGHT) {
            // ตกนอกจอ ทำลายตัวเอง
            isDestroyed = true;
        } else {
            curY = nextY;
        }

        setLocation(curX, curY);
    }
}