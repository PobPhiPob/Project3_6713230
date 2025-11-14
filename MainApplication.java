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
import javax.swing.border.Border;

public class MainApplication extends JFrame {
    private JPanel contentpane;
    private GameIcon backgroundImg;
    private JLabel drawpane;
    private GameSoundEffect themeSound;
    private Timer timer;
    
    private Knight mc;
    private MainApplication currentFrame;
    
    private GameMap gameMap = new GameMap();
    
    private int framewidth  = GameConstants.FRAME_WIDTH;
    private int frameheight = GameConstants.FRAME_HEIGHT;
    
    public static void main(String[] args) {
        new MainApplication();
    }
    
    public MainApplication() {
        setTitle("Game Game");
        setResizable(false);
	setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );
        contentpane = (JPanel)getContentPane();
	contentpane.setLayout( new BorderLayout() );
        contentpane.setPreferredSize(new Dimension(framewidth, frameheight)); 
        pack();
        setLocationRelativeTo(null);
	setVisible(true);
        
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_A)
                    mc.moveLeft();
                if (e.getKeyCode() == KeyEvent.VK_D)
                    mc.moveRight();
                if (e.getKeyCode() == KeyEvent.VK_SPACE)
                    mc.jump();
                if (e.getKeyCode() == KeyEvent.VK_J)
                    mc.attack();
            }
            
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_D)
                    mc.stopMoving();
            }
        });
        
        addComponent();
        timer = new Timer(16, new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               mc.updateLocation();
               repaint();
           }
        });
        timer.start();
    }
    
    public void addComponent() {
        backgroundImg  = new GameIcon(GameConstants.FILE_BG).resize(framewidth, frameheight);
	drawpane = new JLabel();
        Image bg = new ImageIcon(GameConstants.FILE_BG).getImage();
        bg = bg.getScaledInstance(framewidth, frameheight, Image.SCALE_SMOOTH);
        backgroundImg = new GameIcon(bg);
        drawpane.setIcon(backgroundImg);

        drawpane.setLayout(null);
        
        GameMap gameMap = new GameMap(); 

        mc = new Knight(currentFrame ,gameMap);

        Border hitbox = BorderFactory.createLineBorder(Color.RED, 2);
        mc.setBorder(hitbox);
        drawpane.add(mc);

//	themeSound = new GameSoundEffect(GameConstants.FILE_THEME);
//        themeSound.playLoop(); themeSound.setVolume(0.2f);
        
        contentpane.add(drawpane);
        validate();
    }
}
