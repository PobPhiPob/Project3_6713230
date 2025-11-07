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
    private GameIcon idleGif;
    
    private int width = GameConstants.MC_WIDTH;
    private int height = GameConstants.MC_HEIGHT;
    
    private int hp = 100;
    private int attackPoint;
    private int stamina;
    private boolean hit;
    private boolean attack;
    private boolean parry;
    
    public Knight(MainApplication pf) {
        parentFrame = pf;
        
        idleGif = new GameIcon(GameConstants.FILE_MC_IDLE).resize(width, height);
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
}
