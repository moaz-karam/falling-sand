package Main;

import java.awt.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Stack;

public class ParticleHandler implements Runnable {

    public static final int xPositions = (int)(Constants.SCREEN_WIDTH / Constants.PARTICLE_WIDTH);
    public static final int yPositions = (int)(Constants.SCREEN_HEIGHT / Constants.PARTICLE_HEIGHT);
    private static final Particle[][] grid = new Particle[xPositions][yPositions];
    private final Stack<Particle> particles;

    private boolean inserting;

    private int selectedType;

    private static double mouseX;
    private static double mouseY;

    public ParticleHandler() {
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


        for (int y = yIndex - 4; y < yIndex + 4; y += 1) {

            for (int x = xIndex - 4; x < xIndex + 4; x += 1) {
                if (x < 0 || y < 0 || x >= xPositions || y >= yPositions) {
                    continue;
                }

                if (selectedType == Constants.REMOVE) {
                    grid[x][y] = null;
                }

                else if (selectedType == Constants.FIRE) {
                    if (getType(x, y) == Constants.WOOD) {
                        grid[x][y].setOnFire();
                    }
                }
                else if (grid[x][y] == null) {
                    Particle insertedParticle = createParticle(x, y);
                    grid[x][y] = insertedParticle;
                    particles.push(insertedParticle);
                }
            }
        }

//
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
//                grid[xIndex][yIndex].setOnFire();
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
    public static boolean strongerThan(int t1, int t2) {
        return t1 <= t2;
    }

    public void update() {

        if (inserting) {
            insert();
        }

        for (Iterator<Particle> iter = particles.iterator(); iter.hasNext();) {
            Particle p = iter.next();

            if (getParticle(p.getX(), p.getY()) != p) {
                iter.remove();
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

    public Particle[] getParticles() {
        Particle[] returnedArray = new Particle[particles.size()];
        particles.toArray(returnedArray);
        return returnedArray;
    }
    public int getSelectedType() {
        return selectedType;
    }
}
