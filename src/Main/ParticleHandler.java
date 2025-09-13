package Main;

import java.awt.*;
import java.util.Hashtable;
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

    private final Panel panel;

    public ParticleHandler(Panel panel) {
        this.panel = panel;
        particles = new Stack<>();
        inserting = false;
        selectedType = Constants.SAND;

    }

    public void startInserting() {
        inserting = true;
    }
    public void stopInserting() {
        inserting = false;
    }
    public void selectSand() {
        selectedType = Constants.SAND;
    }
    public void selectWater() {
        selectedType = Constants.WATER;
    }
    public void selectWood() {
        selectedType = Constants.WOOD;
    }
    public void selectFire() {selectedType = Constants.FIRE;}
    public void selectRemove() {selectedType = Constants.REMOVE;}
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
    private void insert() {

        int xIndex = (int)(Math.floor(mouseX / Constants.PARTICLE_WIDTH));
        int yIndex = (int)(Math.floor(mouseY / Constants.PARTICLE_HEIGHT));

        if (selectedType < Constants.WOOD) {
            yIndex -= Constants.PARTICLE_SPEED;
        }

        for (int y = yIndex - 2; y < yIndex + 2; y += 1) {

            for (int x = xIndex + 2; x >= xIndex - 2; x -= 1) {
                if (x < 0 || y < 0 || x >= xPositions || y >= yPositions) {
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


//        if (xIndex < 0 || yIndex < 0 || xIndex >= xPositions || yIndex >= yPositions) {
//            return;
//        }
//
//
//        if (selectedType == Constants.REMOVE) {
//            if (grid[xIndex][yIndex] != null) {
//                remove(grid[xIndex][yIndex]);
//            }
//        }
//
//        else if (selectedType == Constants.FIRE) {
//            if (getType(xIndex, yIndex) == Constants.WOOD) {
//                Wood wood = (Wood)grid[xIndex][yIndex];
//                wood.setOnFire();
//            }
//        }
//        else if (grid[xIndex][yIndex] == null) {
//            Particle insertedParticle = createParticle(xIndex, yIndex);
//            grid[xIndex][yIndex] = insertedParticle;
//            particles.push(insertedParticle);
//        }
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
        if (!panel.finishedDrawing) {
            return;
        }
        if (inserting) {
            insert();
        }

        for (Iterator<Particle> iter = particles.iterator(); iter.hasNext();) {
            Particle p = iter.next();

            if (!validPoint(p.getX(), p.getY())) {
                continue;
            }

            if (getParticle(p.getX(), p.getY()) != p) {
                iter.remove();
                continue;
            }
            p.update();
        }

        panel.finishedDrawing = false;
        panel.repaint();
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
//        Particle[] returnedArray = new Particle[particles.size()];
//        particles.toArray(returnedArray);
        return particles;
    }
    public int getSelectedType() {
        return selectedType;
    }
}
