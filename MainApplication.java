/**
 *
 * @author 6713230_Natwarit Chuboonsub
 * @author 6713229_Nutthapat Techapornhiran
 * @author 6713235_Teetath Prapasanon
 * @author 6713239_Nitich Uanjityanon
 * @author 6713243_Puttipong Chutipongwanit
 */
package Project3_6713230;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class MainApplication extends JFrame {
    private JPanel contentpane;
    private GameIcon backgroundImg;
    private JLabel drawpane;
    private GameSoundEffect themeSound;
    
    private Knight mc;
    private MainApplication currentFrame;
    
    private int framewidth  = GameConstants.FRAME_WIDTH;
    private int frameheight = GameConstants.FRAME_HEIGHT;
    
    public static void main(String[] args) {
        new MainApplication();
    }
    
    public MainApplication() {
        setTitle("Game Game");
        setResizable(false);
        setLocationRelativeTo(null);
	setVisible(true);
	setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );
        contentpane = (JPanel)getContentPane();
	contentpane.setLayout( new BorderLayout() );
        contentpane.setPreferredSize(new Dimension(framewidth, frameheight)); 
        pack();
        
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_A)
                    mc.moveLeft();
                if (e.getKeyCode() == KeyEvent.VK_D)
                    mc.moveRight();
            }
            
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_A)
                    mc.setIdle();
                if (e.getKeyCode() == KeyEvent.VK_D)
                    mc.setIdle();
            }
        });
        
        addComponent();
    }
    
    public void addComponent() {
        backgroundImg  = new GameIcon(GameConstants.FILE_BG).resize(framewidth, frameheight);
	drawpane = new JLabel();
        Image bg = new ImageIcon(GameConstants.FILE_BG).getImage();
        bg = bg.getScaledInstance(framewidth, frameheight, Image.SCALE_SMOOTH);
        backgroundImg = new GameIcon(bg);
        drawpane.setIcon(backgroundImg);

        drawpane.setLayout(null);

        mc = new Knight(currentFrame);
/*
        int mcX = 200;  
        int mcY = 350;

        mc.setBounds(mcX, mcY, GameConstants.MC_WIDTH, GameConstants.MC_HEIGHT);
        mc.setIcon(new GameIcon(GameConstants.FILE_MC_IDLE)
                .resize(GameConstants.MC_WIDTH, GameConstants.MC_HEIGHT));
*/
        drawpane.add(mc);

//	themeSound = new GameSoundEffect(GameConstants.FILE_THEME);
//        themeSound.playLoop(); themeSound.setVolume(0.2f);
        
        contentpane.add(drawpane);
        validate();
    }
}
