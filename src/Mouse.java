import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;

public class Mouse implements MouseMotionListener, MouseListener {

    private final Panel panel;

    public Mouse(Panel p) {
        panel = p;
    }

    public void mouseClicked(MouseEvent e) {
        panel.setMousePosition(e.getX(), e.getY());
    }
    public void mousePressed(MouseEvent e) {
        panel.setMousePosition(e.getX(), e.getY());
        panel.startInserting(e.getX(), e.getY());
    }
    public void mouseReleased(MouseEvent e) {
        panel.setMousePosition(e.getX(), e.getY());
        panel.stopInserting();
    }
    public void mouseEntered(MouseEvent e) {
        panel.setMousePosition(e.getX(), e.getY());

    }
    public void mouseExited(MouseEvent e) {

    }
    public void mouseDragged(MouseEvent e) {
        panel.setMousePosition(e.getX(), e.getY());
        panel.startInserting(e.getX(), e.getY());
    }

    public void mouseMoved(MouseEvent e) {
        panel.setMousePosition(e.getX(), e.getY());
    }

}
