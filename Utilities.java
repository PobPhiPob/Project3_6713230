/**
 *
 * @author 6713230_Natwarit Chuboonsub
 * @author 6713229_Nutthapat Techapornhiran
 * @author 6713235_Teetath Prapasanon
 * @author 6713239_Nitich Uanjityanon
 * @author 6713243_Puttipong Chutipongwanit
 */
package Project3_6713230;

import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.ImageIcon;
import java.awt.image.BufferedImage;
import javax.sound.sampled.*;

interface GameConstants
{
    //----- Resource files
    static final String PATH             = "src/main/java/Project3_6713230/resources/";
    static final String FILE_BG          = PATH + "tilebase1.jpg";
    static final String FILE_BG_MAP1     = PATH + "tilebase1.jpg";
    static final String FILE_BG_MAP2     = PATH + "tilebase2.jpg";
    
    static final String FILE_THEME       = PATH + "theme.wav";
    
    static final String FILE_MC_IDLE     = PATH + "/Sprite/Knight/mc_Idle.gif";
    static final String FILE_MC_IDLEFLIP = PATH + "/Sprite/Knight/mc_IdleFlip.gif";
    static final String FILE_MC_RUN      = PATH + "/Sprite/Knight/mc_Run.gif";
    static final String FILE_MC_RUNFLIP  = PATH + "/Sprite/Knight/mc_RunFlip.gif";
    static final String FILE_MC_JUMP     = PATH + "/Sprite/Knight/mc_Jump.gif";
    static final String FILE_MC_JUMPFLIP = PATH + "/Sprite/Knight/mc_JumpFlip.gif";
    static final String FILE_MC_ROLL     = PATH + "/Sprite/Knight/mc_Roll.gif";
    static final String FILE_MC_ROLLFLIP = PATH + "/Sprite/Knight/mc_RollFlip.gif";
    static final String FILE_MC_ATTACK1  = PATH + "/Sprite/Knight/mc_Attack1.gif";
    static final String FILE_MC_ATTACK1FLIP     = PATH + "/Sprite/Knight/mc_Attack1Flip.gif";
    static final String FILE_MC_ATTACK2         = PATH + "/Sprite/Knight/mc_Attack2.gif";
    static final String FILE_MC_ATTACK2FLIP     = PATH + "/Sprite/Knight/mc_Attack2Flip.gif";
    static final String FILE_MC_ATTACK3         = PATH + "/Sprite/Knight/mc_AttackCombo2hit.gif";
    static final String FILE_MC_ATTACK3FLIP     = PATH + "/Sprite/Knight/mc_AttackCombo2hitFlip.gif";
    static final String FILE_MC_HIT             = PATH + "/Sprite/Knight/mc_Hit.gif";
    static final String FILE_MC_HITFLIP         = PATH + "/Sprite/Knight/mc_HitFlip.gif";
    static final String FILE_SKELETON_ATTACK       = PATH + "/Sprite/Monsters/Skeleton/Attack.gif";
    static final String FILE_SKELETON_IDLE         = PATH + "/Sprite/Monsters/Skeleton/Idle.gif";
    static final String FILE_SKELETON_WALK         = PATH + "/Sprite/Monsters/Skeleton/Walk.gif";
    static final String FILE_SKELETON_HIT          = PATH + "/Sprite/Monsters/Skeleton/Onehit.gif";
    static final String FILE_SKELETON_DEAD         = PATH + "/Sprite/Monsters/Skeleton/Dead.gif";
    static final String FILE_SKELETON_ATTACKFLIP   = PATH + "/Sprite/Monsters/Skeleton/Attackflip.gif";
    static final String FILE_SKELETON_IDLEFLIP     = PATH + "/Sprite/Monsters/Skeleton/Idleflip.gif";
    static final String FILE_SKELETON_WALKFLIP     = PATH + "/Sprite/Monsters/Skeleton/Walkflip.gif";
    static final String FILE_SKELETON_HITFLIP      = PATH + "/Sprite/Monsters/Skeleton/Onehitflip.gif";
    static final String FILE_SKELETON_DEADFLIP     = PATH + "/Sprite/Monsters/Skeleton/Deadflip.gif";

    static final String FILE_DUMMY       = PATH + "dummy.png";

    static final int FRAME_WIDTH  = 960;
    static final int FRAME_HEIGHT = 640;
    
    static final int MC_WIDTH = 45;
    static final int MC_HEIGHT = 80;
    
    static final int MC_ATK_WIDTH = 120;
    static final int MC_ATK_HEIGHT = 110;
    
    static final int SKELETON_WIDTH  = 50;
    static final int SKELETON_HEIGHT = 80;
    
    static final int SKELETON_ATK_WIDTH  = 80;
    static final int SKELETON_ATK_HEIGHT = 110;
    
    static final int GRAVITY = 1;
    static final int DURATION_MS = 500; //Dalay
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

    public GameIcon flip(int width, int height) {
        Image img = this.getImage();
        BufferedImage flipped = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = flipped.createGraphics();
        g2.drawImage(img, 0, 0, width, height, width, 0, 0, height, null);
        g2.dispose();

        return new GameIcon(flipped);
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
