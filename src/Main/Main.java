package Main;

import java.awt.Toolkit;

public class Main implements Runnable {
    private final Panel panel;
    private final Thread panelThread;
    public Main() {
        panel = new Panel();
        panel.requestFocusInWindow();
        panelThread = new Thread(this);
        panelThread.start();
    }
    public static void main(String[] args) {
        Main main = new Main();
    }

    @Override
    public void run() {

        double timerPerFrame = 1 / Constants.FRAMES_PER_SECOND;
        long lastFrameTime = System.nanoTime();

        while (true) {
            long now = System.nanoTime();
            if ((now - lastFrameTime) / 1_000_000_000.0 >= timerPerFrame) {
                panel.update();
                Toolkit.getDefaultToolkit().sync();
                lastFrameTime = now;
            }
        }
    }
}