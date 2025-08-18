import java.util.HashSet;
import java.util.Iterator;
import java.util.Stack;

public class ParticleHandler {

    public static final int xPositions = (int)(Constants.SCREEN_WIDTH / Constants.PARTICLE_WIDTH);
    public static final int yPositions = (int)(Constants.SCREEN_HEIGHT / Constants.PARTICLE_HEIGHT);
    private static final Particle[][] grid = new Particle[xPositions][yPositions];
    private final Stack<Particle> particles;
    private static final HashSet<Particle> particlesToBeRemoved = new HashSet<>();

    private boolean inserting;

    private int selectedType;

    private double mouseX;
    private double mouseY;

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
    public int getMouseX() {
        int x = (int)(Math.floor(mouseX / Constants.PARTICLE_WIDTH));
        return (int) (x * Constants.PARTICLE_WIDTH);
    }
    public int getMouseY() {
        int y = (int)(Math.floor(mouseY / Constants.PARTICLE_HEIGHT));
        return (int)(y * Constants.PARTICLE_HEIGHT);
    }
    public static void remove(Particle p) {
        particlesToBeRemoved.add(p);
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


        if (xIndex < 0 || yIndex < 0 || xIndex >= xPositions || yIndex >= yPositions) {
            return;
        }


        if (selectedType == Constants.REMOVE) {
            if (grid[xIndex][yIndex] != null) {
                remove(grid[xIndex][yIndex]);
            }
        }

        else if (selectedType == Constants.FIRE) {
            if (getType(xIndex, yIndex) == Constants.WOOD) {
                grid[xIndex][yIndex].setOnFire();
            }
        }
        else if (grid[xIndex][yIndex] == null) {
            Particle insertedParticle = createParticle(xIndex, yIndex);
            grid[xIndex][yIndex] = insertedParticle;
            particles.push(insertedParticle);
        }
    }
    public static int getType(int xIndex, int yIndex) {
        if (grid[xIndex][yIndex] == null) {
            return 0;
        }
        return grid[xIndex][yIndex].getType();
    }
    public static Particle getParticle(int xIndex, int yIndex) {
        return grid[xIndex][yIndex];
    }
    public static void setParticle(int x, int y, Particle p) {
        grid[x][y] = p;
    }
    public static boolean strongerThan(int t1, int t2) {
        return t1 <= t2;
    }

    public void update() {

        if (inserting) {
            insert();
        }
//        System.out.println();
//        for (int i = 0; i < yPositions; i += 1) {
//            for (int j = 0; j < xPositions; j += 1) {
//                System.out.print(getType(j, i));
//            }
//            System.out.println();
//        }
        for (Iterator<Particle> iter = getParticles(); iter.hasNext();) {
            Particle p = iter.next();
            if (particlesToBeRemoved.contains(p)) {
                iter.remove();
                particlesToBeRemoved.remove(p);
                grid[p.getX()][p.getY()] = null;
                continue;
            }
            p.update();
        }
    }

    public Iterator<Particle> getParticles() {
        return particles.iterator();
    }
    public int getSelectedType() {
        return selectedType;
    }
}
