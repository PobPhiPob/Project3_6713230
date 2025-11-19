package Project3_6713230;

import javax.swing.*;

public class Dummy extends JLabel {
    private MainApplication parentFrame;
    private GameIcon dummyGif;

    private int width = 70;
    private int height = 80;
    private int curX = 800;
    private int curY = 420;

    private int hp = 100;

    public Dummy(MainApplication pf) {
        parentFrame = pf;

        dummyGif = new GameIcon(GameConstants.FILE_DUMMY).resize(width, height);
        setBounds(curX, curY, width, height);
        setIcon(dummyGif);
    }

    public void damage(int d) {
        hp -= d;
        System.out.println("Dummy HP: " + hp);
    }
}

