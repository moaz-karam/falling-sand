package Main;

import java.awt.*;
import java.util.Random;

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
        if (ParticleHandler.getType(xIndex, yIndex) == Constants.WATER) {
            Particle water = ParticleHandler.getParticle(xIndex, yIndex);

            if (getWaterAbsorbed() < Sand.WATER_CAPACITY) {
                absorbWater((Water)water);
            }
            else {
                ParticleHandler.switchParticle(this, water);
            }
            speed = Constants.PARTICLE_WATER_SPEED;
        }
        else {
            ParticleHandler.setParticle(xIndex, yIndex, this);
            speed = Constants.PARTICLE_SPEED;
        }
    }
    private int[] findDiagonal(int startingY, int yDiff) {

        int bottomY = startingY;
        int bottomX = this.x;

        int rightX = this.x;
        int leftX = this.x;

        int rightY = startingY;
        int leftY = startingY;

        Random random = new Random();
        yDiff = random.nextInt(yDiff + 1) + 1;

        for (int i = 1; i <= yDiff; i += 1) {

            int left = x - i;
            int tempY = startingY + i;

            if (!ParticleHandler.validPoint(left, tempY)) {
                break;
            }
            int pType = ParticleHandler.getType(left, tempY);

            if (!ParticleHandler.strongerThan(pType, type)) {
                if (pType != Constants.SAND) {
                    leftX = left;
                    leftY = tempY;
                }
            }
            else {
                break;
            }

        }
        for (int i = 1; i <= yDiff; i += 1) {

            int right = x + i;
            int tempY = startingY + i;

            if (!ParticleHandler.validPoint(right, tempY)) {
                break;
            }
            int pType = ParticleHandler.getType(right, tempY);

            if (!ParticleHandler.strongerThan(pType, type)) {
                if (pType != Constants.SAND) {
                    rightY = tempY;
                    rightX = right;
                }
            }
            else {
                break;
            }
        }

        if (bottomX - leftX > rightX - bottomX) {
            bottomX = leftX;
            bottomY = leftY;
        }
        else {
            bottomX = rightX;
            bottomY = rightY;
        }

        return new int[] {bottomX, bottomY};
    }
    private int[] findPoint() {
        // find the bottom first
        int bottomX = this.x;
        int bottomY = this.y;
        for (int i = 1; i <= speed; i += 1) {

            if (!ParticleHandler.validPoint(x, y +i)) {
                break;
            }
            int pType = ParticleHandler.getType(x, y + i);
            if (!ParticleHandler.strongerThan(pType, type)) {
                if (pType != Constants.SAND) {
                    bottomY = y + i;
                }
            }
            else {
                break;
            }
        }
        if (bottomY - y < speed) {
            int[] temp = findDiagonal(bottomY, speed - (bottomY - y));
            bottomX = temp[0];
            bottomY = temp[1];
        }
        return new int[] {bottomX, bottomY};
    }
    public void update() {
        int[] temp = findPoint();
        putSand(temp[0], temp[1]);
    }
    public int getWaterAbsorbed() {
        return waterAbsorbed;
    }
    public void absorbWater(Water water) {
        ParticleHandler.remove(water);
        waterAbsorbed += 1;
    }
}
