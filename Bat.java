/**
 *
 * @author 6713230_Natwarit Chuboonsub
 * @author 6713229_Nutthapat Techapornhiran
 * @author 6713235_Teetath Prapasanon
 * @author 6713239_Nitich Uanjityanon
 * @author 6713243_Puttipong Chutipongwanit
 */
package Project3_6713230;

public class Bat extends Monsters {
    
    private static final int BAT_WIDTH = GameConstants.BAT_WIDTH;
    private static final int BAT_HEIGHT = GameConstants.BAT_HEIGHT;
    private static final int BAT_ATK_WIDTH = GameConstants.BAT_ATK_WIDTH;
    private static final int BAT_ATK_HEIGHT = GameConstants.BAT_ATK_HEIGHT;
    private static final int INITIAL_HP = 30;
    private static final int ATTACK_POINT = 30;
    private static final int SPEED = 3;
    
    protected GameSoundEffect idleSFX, hitSFX;
    
    public Bat(MainApplication pf, GameMap m, int startX, int startY) {
        
        super(pf, m, startX, startY, 
              BAT_WIDTH, BAT_HEIGHT, 
              BAT_ATK_WIDTH, BAT_ATK_HEIGHT, 
              INITIAL_HP, ATTACK_POINT, SPEED);

        this.gravity = 0; 

        idleGif    = new GameIcon(GameConstants.FILE_BAT_IDLE).resize(width, height);
        deadGif    = new GameIcon(GameConstants.FILE_BAT_DEAD).resize(width, height);

        idleFlip   = new GameIcon(GameConstants.FILE_BAT_IDLEFLIP).resize(width, height);
        deadFlip   = new GameIcon(GameConstants.FILE_BAT_DEADFLIP).resize(width, height);
        
        idleSFX    = new GameSoundEffect(GameConstants.FILE_BAT_IDLE_SFX);
        hitSFX    = new GameSoundEffect(GameConstants.FILE_BAT_HIT_SFX);
        
        setIcon(idleGif);
    }
    
    @Override
    public void updateLocation() {
        if (isDead()) {
            setDead();
            vx = 0;
            vy = 0;
            setLocation(curX, curY);
            return;
        }

        int nextX = curX + vx;
        int nextY = curY + vy;

        int checkX_center = curX + width / 2;
        int checkY_middle = curY + height / 2;

        boolean isKnockedBack = System.currentTimeMillis() < hitTime;

        if (vx != 0) {

            int checkX_left = nextX;
            int checkX_right = nextX + width;

            boolean leftWallHit = map.isSolid(checkX_left, checkY_middle) || map.isLethal(checkX_left, checkY_middle);
            boolean rightWallHit = map.isSolid(checkX_right, checkY_middle) || map.isLethal(checkX_right, checkY_middle);

            if (vx > 0) {
                if (rightWallHit) {
                    vx = 0;
                    if (!isKnockedBack) {
                        turnLeft = true;
                    }
                } else {
                    curX = nextX;
                }
            } else if (vx < 0) {
                if (leftWallHit) {
                    vx = 0;
                    if (!isKnockedBack) {
                        turnLeft = false;
                    }
                } else {
                    curX = nextX;
                }
            }
        }

        if (vy != 0) {

            int checkY_top = nextY;
            int checkY_bottom = nextY + height;

            boolean topHit = map.isSolid(checkX_center, checkY_top) || map.isLethal(checkX_center, checkY_top);
            boolean bottomHit = map.isSolid(checkX_center, checkY_bottom) || map.isLethal(checkX_center, checkY_bottom);

            if (vy < 0) {
                if (topHit) {
                    vy = 0;
                } else {
                    curY = nextY;
                }
            } else if (vy > 0) {
                if (bottomHit) {
                    vy = 0;
                } else {
                    curY = nextY;
                }
            }
        }

        if (vx != 0 || vy != 0) {
            setWalk(); 
        } else {
            setIdle();
        }

        setLocation(curX, curY);
    }

    @Override
    public void updateBehavior(Knight player) {
        if (isDead()) {
            setDead();
            idleSFX.stop();
            return;
        }   
        
        if (isHit) {
            if (System.currentTimeMillis() < hitTime) {
                return;
            }
            isHit = false;
        }

        int playerCenterX = player.getCurX() + player.getWidth() / 2;
        int monsterCenterX = curX + width / 2;
        int distanceX = playerCenterX - monsterCenterX;
        int absDistanceX = Math.abs(distanceX) + 10;
        
        int playerHeadY = player.getCurY();
        int monsterCenterY = curY + height / 2;
        int distanceY = playerHeadY - monsterCenterY;
        int absDistanceY = Math.abs(distanceY) + 30;

        if (absDistanceX < ATTACK_RANGE && absDistanceY < height) {
            if (!isAttacking) {
                isAttacking = true;
                attackTimeStart = System.currentTimeMillis() + (2*GameConstants.DURATION_MS);
                attackTimeEnd = System.currentTimeMillis() + (4*GameConstants.DURATION_MS);
            }
            vx = 0;
            vy = 0;

        } else if (absDistanceX < CHASE_RANGE && absDistanceY < CHASE_RANGE) {
            
            if (distanceX < 0) {
                turnLeft = true;
                vx = -speed;
            } else {
                turnLeft = false;
                vx = speed;
            }
            
            if (distanceY < 0) {
                vy = -speed;
            } else {
                vy = speed;
            }
            
        } else {
            vx = 0;
            vy = 0;
        }
        
        if (isAttacking) {
        
            long now = System.currentTimeMillis();
            
                if (now >= attackTimeStart && now <= attackTimeEnd) {
                    hitboxActive = true;
                } else {
                    hitboxActive = false;
                }
            if (System.currentTimeMillis() > attackTimeEnd) {
                isAttacking = false;
                attack.setVisible(false);
                hitboxActive = false;
            } else {
                setAttack();
                return;
            }
        }else {
            if (vx != 0) {
                setWalk();
            } else {
                setIdle();
            }
        }
    }
    
    @Override
    protected void setWalk() {
        if (isDead()) {
            setDead();
            return;
        }
        this.setVisible(true);
        attack.setVisible(false);
        playIdleSound();
        if (turnLeft) {
            setIcon(idleFlip);
        } else {
            setIcon(idleGif);
        }
    }
    
    @Override
    public void setAttack() {
        this.setVisible(false);
        attack.setVisible(true);
        
        if (turnLeft) {
              attack.setIcon(idleFlip);
              attack.setBounds(curX -(atk_width - width),curY - (atk_height - height),atk_width, atk_height);

        } else {
              attack.setIcon(idleGif);
              attack.setBounds(curX,curY - (atk_height - height),atk_width, atk_height);
        }
    }
    
    @Override
    protected void playIdleSound() {
        if (idleSFX != null) {
            idleSFX.playOnce();
        }
    }
    
    @Override
    protected void playAttackSound() {
//        if (attackSFX != null) {
//            attackSFX.playLoop();
//        }
    }
    
    @Override
    protected void playWalkSound() {
//        if (walkSFX != null) {
//            walkSFX.playLoop();
//        }
    }
    
    @Override
    protected void playHitSound() {
        if (hitSFX != null) {
            hitSFX.playOnce();
        }
    }
    
    @Override
    protected void playDeadSound() {
//        if (deadSFX != null) {
//            deadSFX.playLoop();
//        }
    }
}