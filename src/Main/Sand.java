package Main;

import java.awt.*;
import DataStructures.*;

public class Sand implements Particle {

    private int x;
    private int y;
    private int type;
    private Color color;
    private int waterAbsorbed;
    private int speed;

    public static final int WATER_CAPACITY = 1;


    public Sand(int x, int y) {
        this.x = x;
        this.y = y;

        type = Constants.SAND;
        color = Constants.TYPE_COLOR[type];
        waterAbsorbed = 0;
        speed = Constants.PARTICLE_SPEED;
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
        AStar pointFinder = new AStar(ParticleHandler.getParticle(xIndex, yIndex));
        return pointFinder.getPosition();
    }
    private void updateWater(int xIndex, int yIndex) {
        int[] newPoints = findNearestValidPoint(xIndex, yIndex);
        if (newPoints == null) {
            return;
        }
        int newX = newPoints[0];
        int newY = newPoints[1];

        Particle waterParticle = ParticleHandler.getParticle(xIndex, yIndex);
        ParticleHandler.setParticle(newX, newY, waterParticle);
    }
    private void putSand(int xIndex, int yIndex) {
        ParticleHandler.setParticle(xIndex, yIndex, this);
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
            if (ParticleHandler.strongerThan(bottomType, type)) {
                return;
            }

            if (bottomType == Constants.WATER) {
                updateWater(x, bottom);
            }
            putSand(x, bottom);
        }
        else if (checkRight && !ParticleHandler.strongerThan(bottomRightType, type)) {
            if (bottomRightType == Constants.WATER) {
                updateWater(right, bottom);
            }
            putSand(right, bottom);

        }
        else if (checkLeft && !ParticleHandler.strongerThan(bottomLeftType, type)) {
            if (bottomLeftType == Constants.WATER) {
                updateWater(left, bottom);
            }
            putSand(left, bottom);
        }
    }

    public int getWaterAbsorbed() {
        return waterAbsorbed;
    }
    public void absorbWater(Water water) {
        ParticleHandler.remove(water);
        waterAbsorbed += 1;
    }
}
