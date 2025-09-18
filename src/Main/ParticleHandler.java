package Main;

import java.awt.*;
import java.util.Iterator;
import java.util.Stack;

public class ParticleHandler implements Runnable {

    private static final int xPositions = (int)(Constants.SCREEN_WIDTH / Constants.PARTICLE_WIDTH);
    private static final int yPositions = (int)(Constants.SCREEN_HEIGHT / Constants.PARTICLE_HEIGHT);
    private static final Particle[][] grid = new Particle[xPositions][yPositions];
    private final Stack<Particle> particles;
    private boolean inserting;

    private int selectedType;

    private static double mouseX;
    private static double mouseY;

    private static double prevMouseX;
    private static double prevMouseY;

    private double insertionRadius;

    private final Panel panel;

    public ParticleHandler(Panel panel) {
        this.panel = panel;
        particles = new Stack<>();
        inserting = false;
        selectedType = Constants.SAND;
        insertionRadius = 20;
        prevMouseX = -1;
        prevMouseY = -1;
    }

    public void addToRadius(double i) {
        if (insertionRadius + i >= 0) {
            insertionRadius += i;
        }
    }
    public void startInserting() {
        inserting = true;
    }
    public void stopInserting() {
        inserting = false;
        prevMouseX = -1;
        prevMouseY = -1;
    }
    public void selectSand() {
        selectedType = Constants.SAND;
        prevMouseX = -1;
        prevMouseY = -1;
    }
    public void selectWater() {
        selectedType = Constants.WATER;
        prevMouseX = -1;
        prevMouseY = -1;
    }
    public void selectWood() {
        selectedType = Constants.WOOD;
        prevMouseX = -1;
        prevMouseY = -1;
    }
    public void selectFire() {
        selectedType = Constants.FIRE;
        prevMouseX = -1;
        prevMouseY = -1;
    }
    public void selectRemove() {
        selectedType = Constants.REMOVE;
        prevMouseX = -1;
        prevMouseY = -1;
    }
    public void setMousePosition(double x, double y) {
        mouseX = x;
        mouseY = y;
    }
    public static int getMouseX() {
        int x = (int)(Math.floor(mouseX / Constants.PARTICLE_WIDTH));
        return (int) (x * Constants.PARTICLE_WIDTH);
    }
    public static int getMouseY() {
        int y = (int)(Math.floor(mouseY / Constants.PARTICLE_HEIGHT));
        return (int)(y * Constants.PARTICLE_HEIGHT);
    }
    public static void remove(Particle p) {
        if (p != null) {
            grid[p.getX()][p.getY()] = null;
        }
    }
    private Particle createParticle(int x, int y) {

        switch (selectedType) {
            case Constants.SAND:
                return new Sand(x, y);
            case Constants.WATER:
                return new Water(x, y);
            case Constants.WOOD:
                return new Wood(x, y);
        }
        return null;
    }

    private void insertCircular(double mX, double mY) {

        for (int i = 1; i <= insertionRadius; i += 1) {

            for (double j = 0; j < 360; j += 1) {

                int x = (int)(mX + Math.cos(j) * i);
                int y = (int)(mY + Math.sin(j) * i);

                x = (int)(x / Constants.PARTICLE_WIDTH);
                y = (int)(y / Constants.PARTICLE_HEIGHT);

                if (!ParticleHandler.validPoint(x, y)) {
                    continue;
                }
                if (selectedType == Constants.REMOVE) {
                    remove(grid[x][y]);
                }

                else if (selectedType == Constants.FIRE) {
                    if (getType(x, y) == Constants.WOOD) {
                        Wood wood = (Wood)grid[x][y];
                        wood.setOnFire();
                    }
                }
                else if (grid[x][y] == null) {
                    Particle insertedParticle = createParticle(x, y);
                    grid[x][y] = insertedParticle;
                    particles.push(insertedParticle);
                }
            }

        }
        if (prevMouseX < 0 && prevMouseY < 0) {
            prevMouseX = mouseX;
            prevMouseY = mouseY;
        }
    }
    private void insert() {

        if (prevMouseX < 0) {
            insertCircular(mouseX, mouseY);
            return;
        }

        double xDiff = mouseX - prevMouseX;
        double yDiff = mouseY - prevMouseY;
        double insertionDiff = Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2));

        while (insertionDiff > insertionRadius / 2) {

            double cos = xDiff / insertionDiff;
            double sin = yDiff / insertionDiff;

            prevMouseX += cos * insertionRadius * 0.25;
            prevMouseY += sin * insertionRadius * 0.25;

            insertCircular(prevMouseX, prevMouseY);

            xDiff = mouseX - prevMouseX;
            yDiff = mouseY - prevMouseY;
            insertionDiff = Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2));
        }
        insertCircular(mouseX, mouseY);
    }
    public static int getType(int x, int y) {
        if (grid[x][y] == null) {
            return 0;
        }
        return grid[x][y].getType();
    }
    public static Particle getParticle(int x, int y) {
        return grid[x][y];
    }
    public static void setParticle(int x, int y, Particle p) {
        if (p == null) {
            return;
        }
        grid[p.getX()][p.getY()] = null;
        grid[x][y] = p;
        p.setX(x);
        p.setY(y);
    }
    public static void switchParticle(Particle p1, Particle p2) {
        int tempX = p1.getX();
        int tempY = p1.getY();

        p1.setX(p2.getX());
        p1.setY(p2.getY());
        grid[p2.getX()][p2.getY()] = p1;

        p2.setX(tempX);
        p2.setY(tempY);
        grid[tempX][tempY] = p2;
    }
    public static boolean strongerThan(int t1, int t2) {
        return t1 > t2;
    }
    public static boolean validPoint(int x, int y) {
        return x < xPositions && x >= 0 && y < yPositions && y >= 0;
    }

    public void update() {
        if (inserting) {
            insert();
        }

        for (int i = 0; i < particles.size(); i += 1) {
            Particle p = particles.get(i);
            if (!validPoint(p.getX(), p.getY())) {
                continue;
            }

            if (getParticle(p.getX(), p.getY()) != p) {
                particles.remove(p);
                continue;
            }
            p.update();
        }
    }

    public void run() {
        double timerPerFrame = 1 / Constants.FRAMES_PER_SECOND;
        long lastFrameTime = System.nanoTime();

        while (true) {
            long now = System.nanoTime();
            if ((now - lastFrameTime) / 1_000_000_000.0 >= timerPerFrame) {
                update();
                lastFrameTime = now;
            }
        }
    }

    public Stack<Particle> getParticles() {
        return particles;
    }
    public Stack<Point> getCircle() {
        Stack<Point> circlePoints = new Stack<>();


        for (double i = 0; i < 360; i += 1) {
            double x = mouseX + Math.cos(i) * insertionRadius;
            double y = mouseY + Math.sin(i) * insertionRadius;

            circlePoints.push(new Point((int)x, (int)y));
        }
        return circlePoints;
    }
    public int getSelectedType() {
        return selectedType;
    }
}
