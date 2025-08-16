import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard implements KeyListener {

    private final Panel panel;

    public Keyboard(Panel p) {
        panel = p;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_S:
                panel.selectSand();
                break;
            case KeyEvent.VK_W:
                panel.selectWater();
                break;
            case KeyEvent.VK_D:
                panel.selectWood();
                break;
            case KeyEvent.VK_F:
                panel.selectFire();
                break;
            case KeyEvent.VK_R:
                panel.selectRemove();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
