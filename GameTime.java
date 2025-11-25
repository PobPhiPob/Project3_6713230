package Project3_6713230;

public class GameTime extends Thread {
    private long startTime;
    private long elaspedSecond;
    private boolean running = false;

    @Override
    public void run() {
        while(running) {
            long now = System.currentTimeMillis();
            elaspedSecond = now - startTime;
            System.out.println(elaspedSecond);

            try { Thread.sleep(16); } catch (InterruptedException e) { e.printStackTrace(); }
        }
    }

    public void startTimer() {
        elaspedSecond = 0;
        startTime = System.currentTimeMillis();
        running = true;
    }

    public void pause() {
        running = false;
    }

    public void resume() {
        startTime = System.currentTimeMillis() - elaspedSecond;
        running = true;
    }

    public String getTime() {
        return String.format("%.1f", (float) elaspedSecond / 100);
    }
}
