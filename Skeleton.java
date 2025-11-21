/**
 *
 * @author 6713230_Natwarit Chuboonsub
 * @author 6713229_Nutthapat Techapornhiran
 * @author 6713235_Teetath Prapasanon
 * @author 6713239_Nitich Uanjityanon
 * @author 6713243_Puttipong Chutipongwanit
 */
package Project3_6713230;

public class Skeleton extends Monsters {
    
    private static final int SKELETON_WIDTH = GameConstants.SKELETON_WIDTH;
    private static final int SKELETON_HEIGHT = GameConstants.SKELETON_HEIGHT;
    private static final int SKELETON_ATK_WIDTH = GameConstants.SKELETON_ATK_WIDTH;
    private static final int SKELETON_ATK_HEIGHT = GameConstants.SKELETON_ATK_HEIGHT;
    private static final int INITIAL_HP = 50;
    private static final int ATTACK_POINT = 20;
    private static final int SPEED = 2;
    
    protected GameSoundEffect attackSFX, walkSFX, hitSFX, deadSFX;
    
    public Skeleton(MainApplication pf, GameMap m, int startX, int startY) {
        
        super(pf, m, startX, startY, 
              SKELETON_WIDTH, SKELETON_HEIGHT, 
              SKELETON_ATK_WIDTH, SKELETON_ATK_HEIGHT, 
              INITIAL_HP, ATTACK_POINT, SPEED);

        idleGif    = new GameIcon(GameConstants.FILE_SKELETON_IDLE).resize(width, height);
        walkGif    = new GameIcon(GameConstants.FILE_SKELETON_WALK).resize(width, height);
        hitGif     = new GameIcon(GameConstants.FILE_SKELETON_HIT).resize(width, height);
        attackGif  = new GameIcon(GameConstants.FILE_SKELETON_ATTACK).resize(atk_width, atk_height);
        deadGif    = new GameIcon(GameConstants.FILE_SKELETON_DEAD).resize(width, height);

        idleFlip   = new GameIcon(GameConstants.FILE_SKELETON_IDLEFLIP).resize(width, height);
        walkFlip   = new GameIcon(GameConstants.FILE_SKELETON_WALKFLIP).resize(width, height);
        hitFlip    = new GameIcon(GameConstants.FILE_SKELETON_HITFLIP).resize(width, height);
        attackFlip = new GameIcon(GameConstants.FILE_SKELETON_ATTACKFLIP).resize(atk_width, atk_height);
        deadFlip   = new GameIcon(GameConstants.FILE_SKELETON_DEADFLIP).resize(width, height);
        
        attackSFX  = new GameSoundEffect(GameConstants.FILE_SKELETON_ATTACK_SFX);
        walkSFX    = new GameSoundEffect(GameConstants.FILE_SKELETON_WALK_SFX);
        hitSFX     = new GameSoundEffect(GameConstants.FILE_SKELETON_HIT_SFX);
        deadSFX    = new GameSoundEffect(GameConstants.FILE_SKELETON_DEAD_SFX);
        
        setIcon(idleGif);
    }
    
    @Override
    protected void playIdleSound() {
//        if (idleSFX != null) {
//            idleSFX.playOnce();
//        }
    }
    
    @Override
    protected void playAttackSound() {
        if (attackSFX != null) {
            attackSFX.playOnce();
        }
    }
    
    @Override
    protected void playWalkSound() {
        if (walkSFX != null) {
            walkSFX.playLoop();
        }
    }
    
    @Override
    protected void playHitSound() {
        if (hitSFX != null) {
            hitSFX.playOnce();
        }
    }
    
    @Override
    protected void playDeadSound() {
        if (deadSFX != null) {
            deadSFX.playOnce();
        }
    }
    
}