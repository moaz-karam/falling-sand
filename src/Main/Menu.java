package Main;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Menu extends JPanel implements KeyListener {


    private final Frame frame;
    private final Panel panel;

    public Menu() {

        Dimension size = new Dimension((int)Constants.SCREEN_WIDTH, (int)Constants.SCREEN_HEIGHT);
        setMinimumSize(size);
        setPreferredSize(size);
        setMinimumSize(size);

        panel = new Panel();
        frame = new Frame(this);
    }

    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.blue);
//        g.fillRect(0, 0, 50, 50);

        g.setFont(Font.decode(Font.DIALOG_INPUT));
        g.drawString("Hello world", 100, 200);
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {

    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }
}
