/**
 *
 * @author 6713230_Natwarit Chuboonsub
 * @author 6713229_Nutthapat Techapornhiran
 * @author 6713235_Teetath Prapasanon
 * @author 6713239_Nitich Uanjityanon
 * @author 6713243_Puttipong Chutipongwanit
 */
package Project3_6713230;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Knight extends JLabel {
    private GameFrame parentFrame;

    private GameIcon idleGif, runGif, jumpGif, hitGif, rollGif, deadGif;
    private GameIcon idleFlip, runFlip, jumpFlip, hitFlip, rollFlip, deadFlip;

    private JLabel rollLabel;
    private GameSoundEffect attackSFX, attack3SFX, runSFX, rollSFX, healSFX, hitSFX, deadSFX;

    //For attack animation
    private JLabel attackLabel;
    private GameIcon attack1Gif, attack2Gif, attack3Gif;
    private GameIcon attack1Flip, attack2Flip, attack3Flip;
    
    private int width = GameConstants.MC_WIDTH;
    private int height = GameConstants.MC_HEIGHT;

    private int rollWidth = GameConstants.MC_ROLL_WIDTH;
    private int rollHeight = GameConstants.MC_ROLL_HEIGHT;

    //Get MC_ATTACK Sprite scale
    private int attackImageWidth = GameConstants.MC_ATK_WIDTH;
    private int attackImageHeight = GameConstants.MC_ATK_HEIGHT;
    
    private int hitboxWidth = GameConstants.MC_WIDTH;
    private int hitboxHeight = GameConstants.MC_HEIGHT;
    
    private int curX = 10;
    private int curY = 420;
    private int speed = 5;
    private int rollSpeed = 6;
    private int vx = 0;
    private int vy = 0;
    private int jumpStrength = -12;
    private int gravity = GameConstants.GRAVITY;
    private boolean onGround = true;
    private boolean turnLeft = false;
    
    private int hp = 100;
    private int attackPoint = 25;
    private int currentAttack;
    private int stamina = 50;
    private boolean attack;
    private boolean isHit;
    private boolean canInput = true;
    private boolean roll;
    private boolean heal;
    private long invincibleEndTime = 0;
    private boolean invincible = false;
    private boolean isDead;
    private boolean deathAnimetionFinished = false;
    
    private GameMap map;
    
    public Knight(GameFrame pf, GameMap m) {
        parentFrame = pf;
        this.map = m;
        
        idleGif = new GameIcon(GameConstants.FILE_MC_IDLE).resize(width, height);
        runGif = new GameIcon(GameConstants.FILE_MC_RUN).resize(width, height);
        jumpGif = new GameIcon(GameConstants.FILE_MC_JUMP).resize(width, height);
        hitGif = new GameIcon(GameConstants.FILE_MC_HIT).resize(width, height);
        rollGif = new GameIcon(GameConstants.FILE_MC_ROLL).resize(80, 100);
        deadGif = new GameIcon(GameConstants.FILE_MC_DEAD).resize(100, 80);
        
        idleFlip = new GameIcon(GameConstants.FILE_MC_IDLEFLIP).resize(width, height);
        runFlip = new GameIcon(GameConstants.FILE_MC_RUNFLIP).resize(width, height);
        jumpFlip = new GameIcon(GameConstants.FILE_MC_JUMPFLIP).resize(width, height);
        hitFlip = new GameIcon(GameConstants.FILE_MC_HITFLIP).resize(width, height);
        rollFlip = new GameIcon(GameConstants.FILE_MC_ROLLFLIP).resize(80, 100);
        deadFlip = new GameIcon(GameConstants.FILE_MC_DEADFLIP).resize(100, 80);

        attack1Gif = new GameIcon(GameConstants.FILE_MC_ATTACK1).resize(attackImageWidth, attackImageHeight);
        attack2Gif = new GameIcon(GameConstants.FILE_MC_ATTACK2).resize(attackImageWidth, attackImageHeight);
        attack3Gif = new GameIcon(GameConstants.FILE_MC_ATTACK3).resize(attackImageWidth, attackImageHeight);
       
        attack1Flip = new GameIcon(GameConstants.FILE_MC_ATTACK1FLIP).resize(attackImageWidth, attackImageHeight);
        attack2Flip = new GameIcon(GameConstants.FILE_MC_ATTACK2FLIP).resize(attackImageWidth, attackImageHeight);
        attack3Flip = new GameIcon(GameConstants.FILE_MC_ATTACK3FLIP).resize(attackImageWidth, attackImageHeight);
        
        attackLabel = new JLabel();
        attackLabel.setVisible(false);
        attackLabel.setBorder(BorderFactory.createLineBorder(Color.PINK, 2));

        rollLabel = new JLabel();
        rollLabel.setVisible(false);

        attackSFX = new GameSoundEffect(GameConstants.FILE_MC_ATTACK_SFX);
        attack3SFX = new GameSoundEffect(GameConstants.FILE_MC_ATTACK3_SFX);
        runSFX = new GameSoundEffect(GameConstants.FILE_MC_RUN_SFX);
        rollSFX = new GameSoundEffect(GameConstants.FILE_MC_ROLL_SFX);
        hitSFX = new GameSoundEffect(GameConstants.FILE_MC_HIT_SFX);
        healSFX = new GameSoundEffect(GameConstants.FILE_MC_HEAL_SFX);
        deadSFX = new GameSoundEffect(GameConstants.FILE_MC_DEAD_SFX);
        
        setBounds(curX, curY, width, height);
    }
    
    public JLabel getAttackLabel() { return attackLabel; }
    public JLabel getRollLabel() { return rollLabel; }
    public Rectangle getAttackHitBox() { 
    int atkX = curX;
    int atkY = curY - (attackImageHeight - height); // ชดเชย Y ให้ตรงกับการแสดงผล

    if (turnLeft) {
        // เมื่อหันซ้าย Hitbox ต้องเริ่มก่อนตำแหน่ง curX ของตัวละครหลัก
        // เพื่อครอบคลุมพื้นที่การโจมตีด้านซ้าย
        atkX = curX - attackImageWidth; // กำหนดให้ Hitbox เริ่มที่ขอบซ้ายของภาพโจมตี
    }
    
    // ใช้ขนาดภาพการโจมตีเป็นขนาด Hitbox
    return new Rectangle(atkX, atkY, attackImageWidth, attackImageHeight); 
}
    public Rectangle getHitBox() { return new Rectangle(curX, curY, hitboxWidth, hitboxHeight); }
    public int getCurX() { return curX; }
    public int getCurY() { return curY; }
    public int getHp() { return hp; }
    public int getStamina() { return stamina; }
    public boolean isDeathAnimetionFinished() { return deathAnimetionFinished; }
    public boolean isAttacking() { return attack; }
    public int getAttackPoint() { return attackPoint; }
    public boolean getTurn() { return turnLeft; }

    public void playSound(GameSoundEffect sfx) {
        if (sfx != null) {
            sfx.playOnce();
        }
    }

    public void playRunSound() {
        if (runSFX != null) {
            if (vx != 0 && onGround) {
                runSFX.playLoop();
            } else {
                runSFX.stop();
            }
        }
    }
    
    public void setIdle() {
        if (turnLeft) {
            setIcon(idleFlip);
        } else {
            setIcon(idleGif);
        }
    }
    
    public void setRun() {
        if (turnLeft) {
            setIcon(runFlip);
        } else {
            setIcon(runGif);
        }
    }
    
    public void setJump() {
        if (turnLeft) {
            setIcon(jumpFlip);
        } else {
            setIcon(jumpGif);
        }
    }

    public void setHit() {
        if (turnLeft) {
            setIcon(hitFlip);
        } else {
            setIcon(hitGif);
        }
    }

    public void setRoll() {
        setIcon(null);
        rollLabel.setVisible(true);
        if (turnLeft) {
            rollLabel.setIcon(rollFlip);
            rollLabel.setBounds(curX - ((rollWidth - width) / 2), curY - (rollHeight - height), rollWidth, rollHeight);
        } else {
            rollLabel.setIcon(rollGif);
            rollLabel.setBounds(curX, curY - (rollHeight - height), rollWidth, rollHeight);
        }
    }

    public void setDead() {
        attackLabel.setVisible(false);
        rollLabel.setVisible(false);
        playSound(deadSFX);

        if (turnLeft) {
            setIcon(deadFlip);
            setBounds(curX, curY, 100, 80);
        } else {
            setIcon(deadGif);
            setBounds(curX - width, curY, 100, 80);
        }

        new Timer(600, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                deathAnimetionFinished = true;
                ((Timer)e.getSource()).stop();
            }
        }).start();
    }
    
    public void setAttack1() {
        setIcon(null);
        attackLabel.setVisible(true);
        
        if (turnLeft) {
            attackLabel.setIcon(attack1Flip);
            attackLabel.setBounds(curX - (attackImageWidth - width), curY - (attackImageHeight - height), attackImageWidth, attackImageHeight);
        } else {
            attackLabel.setIcon(attack1Gif);
            attackLabel.setBounds(curX, curY - (attackImageHeight - height), attackImageWidth, attackImageHeight);
        }
    }
    
    public void setAttack2() {
        setIcon(null);
        attackLabel.setVisible(true);
        
        if (turnLeft) {
            attackLabel.setIcon(attack2Flip);
            attackLabel.setBounds(curX - (attackImageWidth - width), curY - (attackImageHeight - height), attackImageWidth, attackImageHeight);
        } else {
            attackLabel.setIcon(attack2Gif);
            attackLabel.setBounds(curX, curY - (attackImageHeight - height), attackImageWidth, attackImageHeight);
        }
    }
    
    public void setAttack3() {
        setIcon(null);
        attackLabel.setVisible(true);

        if (turnLeft) {
            attackLabel.setIcon(attack3Flip);
            attackLabel.setBounds(curX - (attackImageWidth - width), curY - (attackImageHeight - height), attackImageWidth, attackImageHeight);
        } else {
            attackLabel.setIcon(attack3Gif);
            attackLabel.setBounds(curX, curY - (attackImageHeight - height), attackImageWidth, attackImageHeight);
        }
    }

    public void damage(int d) {
        long now = System.currentTimeMillis();
        if (invincible) {
            if (now < invincibleEndTime) return;
            invincible = false;
        }
        if (isDead) return;
        System.out.println("Dodge: " + roll);
        if (roll) return;
        
        isHit = true;
        setHit();
        playSound(hitSFX);
        hp -= d;
        if (hp < 0) hp = 0;
        invincible = true;
        invincibleEndTime = now + (2 * GameConstants.DURATION_MS);
        flashEffect();
    }
    
    private void flashEffect() {
        Timer flashTimer = new Timer(60, null);
        flashTimer.addActionListener(new ActionListener() {
            private int count = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (count >= 6 || !invincible) {
                    setVisible(true);
                    flashTimer.stop();
                    return;
                }

                setVisible(!isVisible());
                count++;
            }
        });
        flashTimer.start();

        new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isHit = false;
                ((Timer)e.getSource()).stop();
            }
        }).start();
    }

    public void regenHp() {
        if (!canInput || attack || roll || !onGround || isDead) return;
        canInput = false;
        heal = true;
        setIdle();
        long now = System.currentTimeMillis();

        new Timer(450, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                canInput = true;
                ((Timer)e.getSource()).stop();
            }
        }).start();

        new Timer(650, e -> {
            heal = false;
            hp += 25;
            if (hp >= 100)
                hp = 100;
            playSound(healSFX);
            invincible = true;
            invincibleEndTime = now + (2 * GameConstants.DURATION_MS);
            flashEffect();
            System.out.println("HP: " + hp);
            ((Timer) e.getSource()).stop();
        }).start();
    }

    public void regenStamina() {
        stamina += 25;
        if (stamina >= 50)
            stamina = 50;
        System.out.println("Stamina: " + stamina);
    }

    public void attack() {
        if (!canInput || roll || heal || isDead) return;
        canInput = false;
        attack = true;
        
        switch(currentAttack) {
            case 0:
                setAttack1();
                break;
            case 1:
                setAttack2();
                break;
            case 2:
                setAttack3();
                break;
        }
        if (currentAttack == 2) playSound(attack3SFX);
        else playSound(attackSFX);

        new Timer(450, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                canInput = true;
                ((Timer)e.getSource()).stop();
            }
        }).start();

        if (currentAttack == 2) {
            new Timer(650, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    attack = false;
                    attackLabel.setVisible(false);
                    currentAttack = 0;
                    setIdle();
                    ((Timer)e.getSource()).stop();
                }
            }).start();
        } else {
            new Timer(300, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    attack = false;
                    attackLabel.setVisible(false);
                    setIdle();
                    ((Timer)e.getSource()).stop();
                }
            }).start();
        }
        currentAttack++;
    }

    public void roll() {
        if (!canInput || attack || !onGround || stamina < 25 || heal || isDead) return;
        canInput = false;
        roll = true;
        setRoll();
        playSound(rollSFX);

        new Timer(850, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                canInput = true;
                ((Timer)e.getSource()).stop();
            }
        }).start();

        new Timer(650, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                roll = false;
                rollLabel.setVisible(false);
                if (parentFrame.getStaminaCounter() >= 188)
                    stamina -= 50;
                else stamina -= 25;
                setIdle();
                ((Timer)e.getSource()).stop();
            }
        }).start();
    }

    public void updateLocation() {
        if (attack || isHit || heal || isDead) return;
        if (hp <= 0) {
            setDead();
            isDead = true;
            return;
        }

        vy += gravity;
        
        int nextY = curY + vy;
        
        boolean leftFoot = map.isSolid(curX + 10, nextY + height);
        boolean rightFoot = map.isSolid(curX + width - 10, nextY + height);
        
        if (vy >= 0 && (leftFoot || rightFoot)) {
            vy = 0;
            onGround = true;
            
            int tileRow = (nextY + height) / map.TILE_SIZE;
            curY = (tileRow * map.TILE_SIZE) - height;
        } else {
            curY = nextY;
            onGround = false;
        }
        
        int nextX = curX + vx;
        
        int checkY_top = curY + 5;
        int checkY_bottom = curY + height - 5;

        if (vx > 0) {
            boolean feetHit = map.isSolid(nextX + width, checkY_bottom);
            
            if (feetHit) {
                vx = 0;
                int tileCol = (nextX + width) / map.TILE_SIZE;
                curX = (tileCol * map.TILE_SIZE) - width;
            } else {
                curX = nextX;
            }
        } else if (vx < 0) {
            boolean feetHit = map.isSolid(nextX, checkY_bottom);
            
            if (feetHit) {
                vx = 0;
                int tileCol = nextX / map.TILE_SIZE;
                curX = (tileCol + 1) * map.TILE_SIZE;
            } else {
                curX = nextX;
            }
        }
        
        boolean hitHazard = false;
        
        if (map.isHazard(curX + 15, curY + height) || map.isHazard(curX + width - 15, curY + height)) {
            hitHazard = true;
        } else if (map.isHazard(curX + 15, curY) || map.isHazard(curX + width - 15, curY)) {
            hitHazard = true;
        } else if (map.isHazard(curX - 1, curY + height - 10) || map.isHazard(curX + width + 1, curY + height - 10)) {
            hitHazard = true;
        }
        
        if (hitHazard) {
            damage(25);
            vy = -10;
            onGround = false;
            if (turnLeft) {
                vx = 8;
            } else {
                vx = -8;
            }
        }
        
        boolean hitLethal = false;
        
        if (map.isLethal(curX + 15, curY + height) || map.isLethal(curX + width - 15, curY + height)) {
            hitLethal = true;
        } else if (map.isLethal(curX + 15, curY) || map.isLethal(curX + width - 15, curY)) {
            hitLethal = true;
        } else if (map.isLethal(curX - 1, curY + height - 10) || map.isLethal(curX + width + 1, curY + height - 10)) {
            hitLethal = true;
        }
        
        if (hitLethal) {
            damage(999);
            vy = -10;
            onGround = false;
            if (turnLeft) {
                vx = 8;
            } else {
                vx = -8;
            }
        }
        
        if (curX < 0) curX = 0;
        if (curX > 960 - width) curX = 960 - width;

        if (roll) {
            curY += vy;
            if (turnLeft) {
                curX -= rollSpeed;
                rollLabel.setBounds(curX - ((rollWidth - width) / 2), curY - (rollHeight - height), rollWidth, rollHeight);
            } else {
                curX += rollSpeed;
                rollLabel.setBounds(curX, curY - (rollHeight - height), rollWidth, rollHeight);
            }
            setLocation(curX, curY);
            return;
        }

        if (onGround) {
            if (vx == 0) {
                setIdle();
            } else {
                setRun();
            }
        } else {
            setJump();
        }
        
        playRunSound();
        setLocation(curX, curY);
    }
    
    public void moveLeft() {
        if (roll) return;
        vx = -speed;
        turnLeft = true;
    }
    
    public void moveRight() {
        if (roll) return;
        vx = speed;
        turnLeft = false;
    }
    
    public void stopMoving() {
        vx = 0;
    }
    
    public void jump() {
        if (onGround) {
            vy = jumpStrength;
            onGround = false;
        }
    }
    
    public void setStartPosition() {
        curX = 10;
        curY = 350;
        vx = 0;
        vy = 0;
        
        int currentLevel = map.getCurrentLevel();

        if (currentLevel == 4) { 
            curX = 10;
            curY = 50;
        }
        
        setLocation(curX, curY);
    }
}