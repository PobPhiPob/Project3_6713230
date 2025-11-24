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
    static final String FILE_BG          = PATH + "tilebase1.jpg";
    static final String FILE_BG_MAP1     = PATH + "tilebase1.jpg";
    static final String FILE_BG_MAP2     = PATH + "tilebase2.jpg";
    static final String FILE_BG_MAP3     = PATH + "tilebase3.jpg";
    static final String FILE_BG_MAP4     = PATH + "tilebase4.jpg";
    static final String FILE_BG_MAP5     = PATH + "tilebase5.jpg";
    static final String FILE_BG_MAP6     = PATH + "tilebase6.jpg";
    
    static final String FILE_THEME                  = PATH + "Sound/CardCastle.wav";
    static final String FILE_MENU_THEME             = PATH + "Sound/Menu.wav";
    static final String FILE_MC_ATTACK_SFX          = PATH + "Sound/Knight/sword-swing.wav";
    static final String FILE_MC_ATTACK3_SFX         = PATH + "Sound/Knight/attack3.wav";
    static final String FILE_MC_RUN_SFX             = PATH + "Sound/Knight/running.wav";
    static final String FILE_MC_ROLL_SFX            = PATH + "Sound/Knight/roll.wav";
    static final String FILE_MC_HIT_SFX             = PATH + "Sound/Knight/hit.wav";
    static final String FILE_MC_HEAL_SFX            = PATH + "Sound/Knight/heal.wav";
    static final String FILE_MC_DEAD_SFX            = PATH + "Sound/Knight/dead.wav";
    static final String FILE_MONSTER_HIT_SFX        = PATH + "Sound/Monsters/BasicHit.wav";
    static final String FILE_SKELETON_ATTACK_SFX    = PATH + "Sound/Monsters/Skeleton/Attack.wav";
    static final String FILE_SKELETON_WALK_SFX      = PATH + "Sound/Monsters/Skeleton/Walk.wav";
    static final String FILE_SKELETON_HIT_SFX       = PATH + "Sound/Monsters/Skeleton/Hit.wav";
    static final String FILE_SKELETON_DEAD_SFX      = PATH + "Sound/Monsters/Skeleton/Dead.wav";
    static final String FILE_BAT_IDLE_SFX           = PATH + "Sound/Monsters/Bat/Idle.wav";
    static final String FILE_BAT_HIT_SFX            = PATH + "Sound/Monsters/Bat/Hit.wav";
    static final String FILE_BAT_DEAD_SFX           = PATH + "Sound/Monsters/Bat/Dead.wav";
    
    static final String FILE_MC_IDLE     = PATH + "Sprite/Knight/mc_Idle.gif";
    static final String FILE_MC_IDLEFLIP = PATH + "Sprite/Knight/mc_IdleFlip.gif";
    static final String FILE_MC_RUN      = PATH + "Sprite/Knight/mc_Run.gif";
    static final String FILE_MC_RUNFLIP  = PATH + "Sprite/Knight/mc_RunFlip.gif";
    static final String FILE_MC_JUMP     = PATH + "Sprite/Knight/mc_Jump.gif";
    static final String FILE_MC_JUMPFLIP = PATH + "Sprite/Knight/mc_JumpFlip.gif";
    static final String FILE_MC_ROLL     = PATH + "Sprite/Knight/mc_Roll.gif";
    static final String FILE_MC_ROLLFLIP = PATH + "Sprite/Knight/mc_RollFlip.gif";
    static final String FILE_MC_ATTACK1  = PATH + "Sprite/Knight/mc_Attack1.gif";
    static final String FILE_MC_ATTACK1FLIP     = PATH + "Sprite/Knight/mc_Attack1Flip.gif";
    static final String FILE_MC_ATTACK2         = PATH + "Sprite/Knight/mc_Attack2.gif";
    static final String FILE_MC_ATTACK2FLIP     = PATH + "Sprite/Knight/mc_Attack2Flip.gif";
    static final String FILE_MC_ATTACK3         = PATH + "Sprite/Knight/mc_AttackCombo2hit.gif";
    static final String FILE_MC_ATTACK3FLIP     = PATH + "Sprite/Knight/mc_AttackCombo2hitFlip.gif";
    static final String FILE_MC_HIT             = PATH + "Sprite/Knight/mc_Hit.gif";
    static final String FILE_MC_HITFLIP         = PATH + "Sprite/Knight/mc_HitFlip.gif";
    static final String FILE_MC_DEAD            = PATH + "Sprite/Knight/mc_Dead.gif";
    static final String FILE_MC_DEADFLIP        = PATH + "Sprite/Knight/mc_DeadFlip.gif";
    
    static final String FILE_SKELETON_ATTACK       = PATH + "Sprite/Monsters/Skeleton/Attack.gif";
    static final String FILE_SKELETON_IDLE         = PATH + "Sprite/Monsters/Skeleton/Idle.gif";
    static final String FILE_SKELETON_WALK         = PATH + "Sprite/Monsters/Skeleton/Walk.gif";
    static final String FILE_SKELETON_HIT          = PATH + "Sprite/Monsters/Skeleton/Onehit.gif";
    static final String FILE_SKELETON_DEAD         = PATH + "Sprite/Monsters/Skeleton/Dead.gif";
    static final String FILE_SKELETON_ATTACKFLIP   = PATH + "Sprite/Monsters/Skeleton/Attackflip.gif";
    static final String FILE_SKELETON_IDLEFLIP     = PATH + "Sprite/Monsters/Skeleton/Idleflip.gif";
    static final String FILE_SKELETON_WALKFLIP     = PATH + "Sprite/Monsters/Skeleton/Walkflip.gif";
    static final String FILE_SKELETON_HITFLIP      = PATH + "Sprite/Monsters/Skeleton/Onehitflip.gif";
    static final String FILE_SKELETON_DEADFLIP     = PATH + "Sprite/Monsters/Skeleton/Deadflip.gif";

    static final String FILE_GOLEM_IDLE       = PATH + "/Sprite/Boss/Idle.gif";
    static final String FILE_GOLEM_ATTACK1       = PATH + "/Sprite/Boss/Attack1.gif";
    static final String FILE_GOLEM_ATTACK1FLIP       = PATH + "/Sprite/Boss/Attack1flip.gif";
    static final String FILE_GOLEM_PREJUMP       = PATH + "/Sprite/Boss/prejump.gif";
    static final String FILE_GOLEM_ONAIR         = PATH + "/Sprite/Boss/onair.png";
    static final String FILE_GOLEM_FALL          = PATH + "/Sprite/Boss/fall.png";
    static final String FILE_GOLEM_GROUNDHIT     = PATH + "/Sprite/Boss/groundHit.png";
    static final String FILE_GOLEM_PARTICLE      = PATH + "/Sprite/Boss/particle.gif";
    static final String FILE_GOLEM_ROCK        = PATH + "/Sprite/Boss/rock.png";
    static final String FILE_GOLEM_ROCKPARTICLE        = PATH + "/Sprite/Boss/rockParticle.png";
    static final String FILE_GOLEM_SLIDE         = PATH + "/Sprite/Boss/slide.png";
    static final String FILE_GOLEM_SLIDEPARTICLE = PATH + "/Sprite/Boss/slideParticle.gif";
    static final String FILE_GOLEM_SLIDEPARTICLEFLIP = PATH + "/Sprite/Boss/slideParticleflip.gif";
    static final String FILE_GOLEM_HIT_SPRITE    = PATH + "/Sprite/Boss/hit.png";
    static final String FILE_GOLEM_DEAD    = PATH + "/Sprite/Boss/dead.png";
    
    static final String FILE_BAT_IDLE              = PATH + "Sprite/Monsters/Bat/Idle.gif";
    static final String FILE_BAT_DEAD              = PATH + "Sprite/Monsters/Bat/Dead.gif";
    static final String FILE_BAT_IDLEFLIP          = PATH + "Sprite/Monsters/Bat/Idleflip.gif";
    static final String FILE_BAT_DEADFLIP          = PATH + "Sprite/Monsters/Bat/Deadflip.gif";

    static final String FILE_TITLE     = PATH + "UI/Title.gif";
    static final String FILE_CASTLE_BG     = PATH + "BG/Castle.gif";
    static final String FILE_HOLLOW_BG     = PATH + "BG/Church.gif";    
    static final String FILE_FOREST_BG     = PATH + "BG/Forest.gif";
    static final String FILE_RAIN_BG     = PATH + "BG/Rain.gif";
    
    static final String FILE_DUMMY       = PATH + "dummy.png";

    static final int FRAME_WIDTH  = 960;
    static final int FRAME_HEIGHT = 640;
    static final int TILE_SIZE = 32;
    
    static final int MC_WIDTH = 45;
    static final int MC_HEIGHT = 80;

    static final int MC_ROLL_WIDTH = 100;
    static final int MC_ROLL_HEIGHT = 80;
    static final int MC_ATK_WIDTH = 120;
    static final int MC_ATK_HEIGHT = 110;
    
    static final int SKELETON_WIDTH  = 50;
    static final int SKELETON_HEIGHT = 80;
    static final int SKELETON_ATK_WIDTH  = 80;
    static final int SKELETON_ATK_HEIGHT = 95;
    
    static final int ROCK_WIDTH          = 100;
    static final int ROCK_HEIGHT         = 100;
    static final int ROCK_FALL_VY = 3;
    static final int ROCK_PROJECTILE_VX = 10;
    static final int PARTICLE_DURATION_MS = 1000;
    
    final int GOLEM_WIDTH         = 230;
    static final int GOLEM_HEIGHT        = 170;
    static final int GOLEM_ATK_WIDTH     = 270;
    static final int GOLEM_ATK_HEIGHT    = 170;
    static final int GOLEM_JUMP_WIDTH     = 290;
    static final int GOLEM_JUMP_HEIGHT    = 240;
    static final int GOLEM_MAX_HP        = 400;
    static final int GOLEM_MELEE_DMG     = 10;
    static final int GOLEM_JUMP_SLIDE_DMG= 25;
    static final int GOLEM_ROCK_DMG      = 25;
    static final int GOLEM_RECHARGE_TIME = 2500;
    
    static final int BAT_WIDTH = 50;
    static final int BAT_HEIGHT = 50;
    static final int BAT_ATK_WIDTH = 40;
    static final int BAT_ATK_HEIGHT = 40;
    
    static final int KNOCKBACK = 3;
    static final int FRAME_RATE_MS = 16; //Timer

    static final int GRAVITY = 1;
    static final int DURATION_MS = 500; //Delay
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
    private Clip clip;
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
    
    public void playOnce()
    {

                    if (clip != null) {
                        if (clip.isRunning()) clip.stop();
                        clip.setMicrosecondPosition(0);
                        clip.start();
                    }

    }
    
    public void playLoop()
    { 

                    if (clip != null) {
                        clip.loop(Clip.LOOP_CONTINUOUSLY);
                    }

    }
    
    public void stop()
    { 
        if (clip != null) {
            clip.stop();
        }
    }
            
    public void setVolume(float gain)
    {
        if (gainControl != null) {
            if (gain < 0.0f)  gain = 0.0f;
            if (gain > 1.0f)  gain = 1.0f;
            float dB = (float)(Math.log(gain) / Math.log(10.0) * 20.0);
            gainControl.setValue(dB);
        }
    }
}