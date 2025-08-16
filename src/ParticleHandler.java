import java.util.HashSet;
import java.util.Iterator;
import java.util.Stack;

public class ParticleHandler {

    private final Particle[][] grid;
    private final int xPositions = (int)(Constants.SCREEN_WIDTH / Constants.PARTICLE_WIDTH);
    private final int yPositions = (int)(Constants.SCREEN_HEIGHT / Constants.PARTICLE_HEIGHT);
    private final Stack<Particle> particles;
    private final HashSet<Particle> particlesToBeRemoved;

    private boolean inserting;
    private double currentX;
    private double currentY;

    private int selectedType;

    class Particle {
        int x;
        int y;
        int type;
        int previousType;
        long typeUpdateTime;

        static final double UPDATE_FIRE_TIME = 0.25;

        public Particle(int x1, int y1, int t) {
            x = x1;
            y = y1;
            type = t;
            previousType = 0;
            typeUpdateTime = 0;
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
            typeUpdateTime = System.nanoTime();
            previousType = type;
            type = t;
        }
        public int getPreviousType() {
            return previousType;
        }
        public long getTypeUpdateTime() {
            return typeUpdateTime;
        }
        public void setTypeUpdateTime(long i) {
            typeUpdateTime = i;
        }
    }

    public ParticleHandler() {
        grid = new Particle[xPositions][yPositions];
        particles = new Stack<>();
        particlesToBeRemoved = new HashSet<>();
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
    public void selectWood() {
        selectedType = Constants.WOOD;
    }
    public void selectFire(){selectedType = Constants.FIRE;}
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
        else if (selectedType == Constants.FIRE && grid[xIndex][yIndex].getType() == Constants.WOOD) {
            grid[xIndex][yIndex].setType(Constants.FIRE);
        }
    }

    private int getType(int xIndex, int yIndex) {
        /*
        returns the type of the particle under the passed particle
        if it is null it returns 0
        * */

        if (grid[xIndex][yIndex] == null) {
            return 0;
        }
        return grid[xIndex][yIndex].getType();
    }
    private boolean strongerThanSand(int type) {
        switch (type) {
            case Constants.WOOD, Constants.FIRE, Constants.SAND:
                return true;
            default:
                return false;
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

        int bottomRightType = -1;
        int bottomLeftType = -1;

        if (bottom >= yPositions) {
            return;
        }
        if (right < xPositions) {
            checkRight = true;
            bottomRightType = getType(right, bottom);
        }
        if (left >= 0) {
            checkLeft = true;
            bottomLeftType = getType(left, bottom);
        }

        int bottomType = getType(pX, bottom);

        if (bottomType != Constants.SAND) {
            Particle tempP = grid[pX][bottom];

            if (strongerThanSand(bottomType)) {
                return;
            }

            grid[pX][bottom] = p;
            p.setY(bottom);

            if (bottomType == 0) {
                grid[pX][pY] = tempP;
            }

            if (bottomType == Constants.WATER) {
                if (checkRight && getType(right, pY) == 0) {
                    grid[right][pY] = tempP;
                    tempP.setX(right);
                    tempP.setY(pY);
                    grid[pX][pY] = null;
                }
                else if (checkLeft && getType(left, pY) == 0) {
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
        }
        else if (checkRight && !strongerThanSand(bottomRightType)) {
            Particle tempParticle = grid[right][bottom];

            switch (bottomRightType) {
                case 0:
                    grid[pX][pY] = null;
                    break;
                case Constants.WATER:
                    if (getType(right, pY) == 0) {
                        grid[right][pY] = tempParticle;
                        tempParticle.setY(pY);
                        grid[pX][pY] = null;
                    }
                    else {
                        grid[pX][pY] = tempParticle;
                        tempParticle.setX(pX);
                        tempParticle.setY(pY);
                    }
                    break;
            }
            grid[right][bottom] = p;
            p.setX(right);
            p.setY(bottom);
        }
        else if (checkLeft && !strongerThanSand(bottomLeftType)) {
            Particle tempParticle = grid[left][bottom];

            switch(bottomLeftType) {
                case 0:
                    grid[pX][pY] = null;
                    break;
                case Constants.WATER:
                    if (getType(left, pY) == 0) {
                        grid[left][pY] = tempParticle;
                        tempParticle.setY(pY);
                        grid[pX][pY] = null;
                    }
                    else {
                        grid[pX][pY] = tempParticle;
                        tempParticle.setX(pX);
                        tempParticle.setY(pY);
                    }
                    break;
            }

            grid[left][bottom] = p;
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


        if (bottom < yPositions && grid[pX][bottom] == null) {
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
    private void updateFire(Particle p) {

        long now = System.nanoTime();

        if ((now - p.getTypeUpdateTime()) / 1_000_000_000.0 < Particle.UPDATE_FIRE_TIME) {
            return;
        }

        int pX = p.getX();
        int pY = p.getY();

        int minY = pY - 1;
        int maxY = pY + 1;
        int minX  = pX - 1;
        int maxX = pX + 1;

        if (minY < 0) {
            minY += 1;
        }
        if (minX < 0) {
            minX += 1;
        }

        for (int y = minY; y <= maxY && y < yPositions; y += 1) {
            for (int x = minX; x <= maxX && x < xPositions; x += 1) {
                if (getType(x, y) == Constants.WATER) {

                    if (p.getPreviousType() == 0) {
                        particlesToBeRemoved.add(p);
                    }
                    else {
                        p.setType(p.getPreviousType());
                        p.setTypeUpdateTime(0);
                    }

                    return;
                }
            }
        }

        for (int y = minY; y <= maxY && y < yPositions; y += 1) {
            for (int x = minX; x <= maxX && x < xPositions; x += 1) {
                if (getType(x, y) == Constants.WOOD) {
                    grid[x][y].setType(Constants.FIRE);
                }
            }
        }

        particlesToBeRemoved.add(p);
    }
    public void update() {
        if (inserting) {
            insert();
        }
        for (Iterator<Particle> iter = getParticles(); iter.hasNext();) {
            Particle p = iter.next();
            if (particlesToBeRemoved.contains(p)) {
                iter.remove();
                particlesToBeRemoved.remove(p);
                grid[p.getX()][p.getY()] = null;
                continue;
            }
            switch (p.getType()) {
                case Constants.SAND:
                    updateSand(p);
                    break;
                case Constants.WATER:
                    updateWater(p);
                    break;
                case Constants.FIRE:
                    updateFire(p);
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
