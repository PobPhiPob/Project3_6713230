/**
 *
 * @author 6713230_Natwarit Chuboonsub
 * @author 6713229_Nutthapat Techapornhiran
 * @author 6713235_Teetath Prapasanon
 * @author 6713239_Nitich Uanjityanon
 * @author 6713243_Puttipong Chutipongwanit
 */
package Project3_6713230;

import java.awt.Image;
import javax.swing.ImageIcon;
import javax.sound.sampled.*;

interface GameConstants
{
    //----- Resource files
    static final String PATH             = "src/main/java/Project3_6713230/resources/";
    static final String FILE_BG          = PATH + "tokyo.jpg";
    
    static final String FILE_THEME       = PATH + "theme.wav";
    
    static final String FILE_MC_IDLE     = PATH + "/Sprite/mc_Idle.gif";
    static final String FILE_MC_RUN      = PATH + "/Sprite/mc_Run.gif";

    static final int FRAME_WIDTH  = 1280;
    static final int FRAME_HEIGHT = 720;
    
    static final int MC_WIDTH = 250;
    static final int MC_HEIGHT = 250;
}

class GameIcon extends ImageIcon
{
    public GameIcon(String fname)  { super(fname); }
    public GameIcon(Image image)   { super(image); }

    public GameIcon resize(int width, int height)
    {
	Image oldimg = this.getImage();
	Image newimg = oldimg.getScaledInstance(width, height, java.awt.Image.SCALE_DEFAULT);
        return new GameIcon(newimg);
    }
}

class GameSoundEffect
{
    private Clip         clip;
    private FloatControl gainControl;         

    public GameSoundEffect(String filename)
    {
	try
	{
            java.io.File file = new java.io.File(filename);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(audioStream);            
            gainControl = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
	}
	catch (Exception e) { e.printStackTrace(); }
    }
    public void playOnce()             { clip.setMicrosecondPosition(0); clip.start(); }
    public void playLoop()             { clip.loop(Clip.LOOP_CONTINUOUSLY); }
    public void stop()                 { clip.stop(); }
    public void setVolume(float gain)
    {
        if (gain < 0.0f)  gain = 0.0f;
        if (gain > 1.0f)  gain = 1.0f;
        float dB = (float)(Math.log(gain) / Math.log(10.0) * 20.0);
        gainControl.setValue(dB);
    }
}
