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
    
    private int framewidth  = GameConstants.FRAME_WIDTH;
    private int frameheight = GameConstants.FRAME_HEIGHT;
    
    public static void main(String[] args) {
        new MainApplication();
    }
    
    public MainApplication() {
        setTitle("Game Game");
	setSize(framewidth, frameheight);
        setLocationRelativeTo(null);
	setVisible(true);
	setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );
        contentpane = (JPanel)getContentPane();
	contentpane.setLayout( new BorderLayout() );
        addComponent();
    }
    
    public void addComponent() {
        backgroundImg  = new GameIcon(GameConstants.FILE_BG).resize(framewidth, frameheight);
	drawpane = new JLabel();
	drawpane.setIcon(backgroundImg);
        drawpane.setLayout(null);

//	themeSound = new GameSoundEffect(GameConstants.FILE_THEME);
//        themeSound.playLoop(); themeSound.setVolume(0.2f);
        
        contentpane.add(drawpane);
        validate();
    }
}
