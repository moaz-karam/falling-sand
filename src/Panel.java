import javax.swing.*;
import java.awt.*;
import java.util.Iterator;

public class Panel extends JPanel {

    private final JFrame frame;
    private final ParticleHandler ph;
    private final Mouse mouse;
    private final Keyboard keyboard;

    public Panel() {
        ph = new ParticleHandler();
        mouse = new Mouse(this);
        keyboard = new Keyboard(this);

        Dimension size = new Dimension((int)Constants.SCREEN_WIDTH, (int)Constants.SCREEN_HEIGHT);
        addMouseMotionListener(mouse);
        addMouseListener(mouse);
        addKeyListener(keyboard);
        setMinimumSize(size);
        setPreferredSize(size);
        setMinimumSize(size);
        setBackground(Color.DARK_GRAY);
        setVisible(true);
        frame = new Frame(this);
    }

    public void paint(Graphics g) {
        super.paint(g);
        for (Iterator<ParticleHandler.Particle> iter = ph.getParticles(); iter.hasNext();) {

            ParticleHandler.Particle p = iter.next();

            g.setColor(Constants.TYPE_COLOR[p.getType()]);

            g.fillRect((int)(p.getX() * Constants.PARTICLE_WIDTH), (int)(p.getY() * Constants.PARTICLE_HEIGHT),
                    (int)Constants.PARTICLE_WIDTH, (int)Constants.PARTICLE_HEIGHT);
        }
        g.setColor(Constants.TYPE_COLOR[ph.getSelectedType()]);
        g.fillRect(ph.getMouseX(), ph.getMouseY(), (int)Constants.PARTICLE_WIDTH, (int)Constants.PARTICLE_HEIGHT);
    }

    public void update() {
        ph.update();
    }

    public void setMousePosition(double x, double y) {
        ph.setMousePosition(x, y);
    }
    public void startInserting(double x, double y) {
        ph.startInserting(x, y);
    }
    public void stopInserting() {
        ph.stopInserting();
    }
    public void selectSand() {
        ph.selectSand();
    }
    public void selectWater() {
        ph.selectWater();
    }
    public void selectWood() {
        ph.selectWood();
    }
    public void selectFire() {ph.selectFire();}
    public void selectRemove() {ph.selectRemove();}
}
