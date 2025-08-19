package Main;



import java.awt.*;
import DataStructures.Dijkstra;

public class Sand implements Particle {

    private int x;
    private int y;
    private int type;
    private Color color;


    public Sand(int x, int y) {
        this.x = x;
        this.y = y;

        type = Constants.SAND;
        color = Constants.TYPE_COLOR[type];
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
    public Color getColor() {
        return color;
    }

    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
    public void setType(int t) {
        type = t;
    }
    public void setColor(Color c) {
        color = c;
    }
    private int[] findNearestValidPoint(int xIndex, int yIndex) {
        Dijkstra pointFinder = new Dijkstra(ParticleHandler.getParticle(xIndex, yIndex));
        return pointFinder.getPosition();
    }
    private void updateWater(int xIndex, int yIndex) {
        int[] newPoints = findNearestValidPoint(xIndex, yIndex);
        int newX = newPoints[0];
        int newY = newPoints[1];

        Particle waterParticle = ParticleHandler.getParticle(xIndex, yIndex);
        ParticleHandler.setParticle(xIndex, yIndex, null);
        ParticleHandler.setParticle(newX, newY, waterParticle);
        waterParticle.setX(newX);
        waterParticle.setY(newY);
    }
    public void update() {

        int bottom = y + 1;
        int right = x + 1;
        int left = x - 1;

        boolean checkRight = false;
        boolean checkLeft = false;

        int bottomRightType = -1;
        int bottomLeftType = -1;

        if (bottom >= ParticleHandler.yPositions) {
            return;
        }
        if (right < ParticleHandler.xPositions) {
            checkRight = true;
            bottomRightType = ParticleHandler.getType(right, bottom);
        }
        if (left >= 0) {
            checkLeft = true;
            bottomLeftType = ParticleHandler.getType(left, bottom);
        }

        int bottomType = ParticleHandler.getType(x, bottom);

        if (bottomType != Constants.SAND) {
            if (ParticleHandler.strongerThan(type, bottomType)) {
                return;
            }

            if (bottomType == Constants.WATER) {
                updateWater(x, bottom);
            }
            ParticleHandler.setParticle(x, y, null);
            ParticleHandler.setParticle(x, bottom, this);
            setY(bottom);
        }
        else if (checkRight && !ParticleHandler.strongerThan(type, bottomRightType)) {
            if (bottomRightType == Constants.WATER) {
                updateWater(right, bottom);
            }
            ParticleHandler.setParticle(x, y, null);
            ParticleHandler.setParticle(right, bottom, this);
            setX(right);
            setY(bottom);
        }
        else if (checkLeft && !ParticleHandler.strongerThan(type, bottomLeftType)) {
            if (bottomLeftType == Constants.WATER) {
                updateWater(left, bottom);
            }
            ParticleHandler.setParticle(x, y, null);
            ParticleHandler.setParticle(left, bottom, this);
            setX(left);
            setY(bottom);
        }
    }

    public void setOnFire() {
    }
    public boolean isOnFire() {
        return false;
    }
}
