import java.util.HashSet;
import java.util.Iterator;
import java.util.Stack;

public class ParticleHandler {

    private final Particle grid[][];
    private final int xPositions = (int)(Constants.SCREEN_WIDTH / Constants.PARTICLE_WIDTH);
    private final int yPositions = (int)(Constants.SCREEN_HEIGHT / Constants.PARTICLE_HEIGHT);
    private final Stack<Particle> particles;

    private boolean inserting;
    private double currentX;
    private double currentY;

    private int selectedType;

    class Particle {
        int x;
        int y;
        int type;

        public Particle(int x1, int y1, int t) {
            x = x1;
            y = y1;
            type = t;
        }
        public int getX() {
            return x;
        }
        public int getY() {
            return y;
        }
        public int getType() {
            return type;
        }

        public void setX(int x1) {
            x = x1;
        }
        public void setY(int y1) {
            y = y1;
        }
        public void setType(int t) {
            type = t;
        }
    }

    public ParticleHandler() {
        grid = new Particle[xPositions][yPositions];
        particles = new Stack<>();
        inserting = false;
        selectedType = Constants.SAND;
    }

    public void startInserting(double x, double y) {
        currentX = x;
        currentY = y;
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

    private void insert() {
        int xIndex = (int)(Math.floor(currentX / Constants.PARTICLE_WIDTH));
        int yIndex = (int)(Math.floor(currentY / Constants.PARTICLE_HEIGHT));

        if (xIndex < 0 || yIndex < 0 || xIndex >= xPositions || yIndex >= yPositions) {
            return;
        }

        if (grid[xIndex][yIndex] == null) {
            Particle insertedParticle = new Particle(xIndex, yIndex, selectedType);
            grid[xIndex][yIndex] = insertedParticle;
            particles.push(insertedParticle);
        }
    }

    private void updateSand(Particle p) {

        int pX = p.getX();
        int pY = p.getY();

        int bottom = pY + 1;
        int right = pX + 1;
        int left = pX - 1;

        boolean checkRight = false;
        boolean checkLeft = false;


        if (bottom >= yPositions) {
            return;
        }
        if (right < xPositions) {
            checkRight = true;
        }
        if (left >= 0) {
            checkLeft = true;
        }

        if (grid[pX][bottom] == null || grid[pX][bottom].getType() == Constants.WATER) {
            Particle tempP = grid[pX][bottom];
            grid[pX][bottom] = p;
            p.setY(bottom);

            if (tempP == null) {
                grid[pX][pY] = tempP;
            }

            else if (checkRight && grid[right][pY] == null) {
                grid[right][pY] = tempP;
                tempP.setX(right);
                tempP.setY(pY);
                grid[pX][pY] = null;
            }
            else if (checkLeft && grid[left][pY] == null) {
                grid[left][pY] = tempP;
                tempP.setX(left);
                tempP.setY(pY);
                grid[pX][pY] = null;
            }
            else {
                grid[pX][pY] = tempP;
                tempP.setY(pY);
            }
        }
        else if (checkRight && grid[right][bottom] == null) {
            grid[right][bottom] = p;
            grid[pX][pY] = null;

            p.setX(right);
            p.setY(bottom);
        }
        else if (checkLeft && grid[left][bottom] == null) {
            grid[left][bottom] = p;
            grid[pX][pY] = null;

            p.setX(left);
            p.setY(bottom);
        }
    }
    private void updateWater(Particle p) {
        int pX = p.getX();
        int pY = p.getY();

        int bottom = pY + 1;
        int[] directions = {pX + 1, pX - 1};
        int firstDirectionIndex = (int)(System.nanoTime() % 2);
        int secondDirectionIndex = 1 - firstDirectionIndex;

        int firstDirection = directions[firstDirectionIndex];
        int secondDirection = directions[secondDirectionIndex];

        if (bottom >= yPositions) {
            return;
        }

        if (grid[pX][bottom] == null) {
            grid[pX][bottom] = p;
            grid[pX][pY] = null;
            p.setY(bottom);
        }
        else if (firstDirection >= 0 && firstDirection < xPositions && grid[firstDirection][pY] == null) {
            grid[firstDirection][pY] = p;
            grid[pX][pY] = null;
            p.setX(firstDirection);
        }
        else if (secondDirection >= 0 && secondDirection < xPositions && grid[secondDirection][pY] == null) {
            grid[secondDirection][pY] = p;
            grid[pX][pY] = null;
            p.setX(secondDirection);
        }

    }
    public void update() {
        if (inserting) {
            insert();
        }
        for (Iterator<Particle> iter = getParticles(); iter.hasNext();) {
            Particle p = iter.next();
            switch (p.getType()) {
                case Constants.SAND:
                    updateSand(p);
                    break;
                case Constants.WATER:
                    updateWater(p);
                    break;
            }
        }
    }
    public Iterator<Particle> getParticles() {
        return particles.iterator();
    }
    public int getSelectedType() {
        return selectedType;
    }
}
