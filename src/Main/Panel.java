package Main;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;

public class Panel extends JPanel {

    private final JFrame frame;
    private final ParticleHandler ph;
    private final Mouse mouse;
    private final Keyboard keyboard;
    private boolean showSand;
    private Thread phThread;
    public boolean finishedDrawing;

    public Panel() {
        ph = new ParticleHandler(this);
        mouse = new Mouse(this);
        keyboard = new Keyboard(this);
        showSand = true;
        Dimension size = new Dimension((int)Constants.SCREEN_WIDTH, (int)Constants.SCREEN_HEIGHT);
        addMouseMotionListener(mouse);
        addMouseListener(mouse);
        addMouseWheelListener(mouse);
        addKeyListener(keyboard);
        setMinimumSize(size);
        setPreferredSize(size);
        setMinimumSize(size);
        setBackground(Color.DARK_GRAY);
        setVisible(true);
        setDoubleBuffered(true);
        frame = new Frame(this);

        finishedDrawing = true;
        phThread = new Thread(ph);
        phThread.start();
    }

    public void paint(Graphics g) {
        super.paint(g);

        for (int i = 0; i < ph.getParticles().size(); i += 1) {
            Particle p = ph.getParticles().get(i);
            if (p.getType() == Constants.SAND && !showSand) {
                continue;
            }

            g.setColor(p.getColor());

            g.fillRect((int)(p.getX() * Constants.PARTICLE_WIDTH), (int)(p.getY() * Constants.PARTICLE_HEIGHT),
                    (int)Constants.PARTICLE_WIDTH, (int)Constants.PARTICLE_HEIGHT);

        }

        g.setColor(Constants.TYPE_COLOR[ph.getSelectedType()]);
        for (Point p : ph.getCircle()) {
            g.fillRect((int)p.getX(), (int)p.getY(), 1, 1);
        }
    }

    public void update() {
        repaint();
    }

    public void setMousePosition(double x, double y) {
        ph.setMousePosition(x, y);
    }
    public void startInserting() {
        ph.startInserting();
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
    public void changeShowSandState() {
        if (showSand) {
            showSand = false;
            return;
        }
        showSand = true;
    }
    public void addToRadius(double i) {
        ph.addToRadius(i);
    }
}
