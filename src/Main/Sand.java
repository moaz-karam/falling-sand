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
    private int findX(int sign, int bottom) {
        int point = x;

        for (int i = sign; i * sign <= speed; i += sign) {

            if (!ParticleHandler.validPoint(x + i, bottom)) {
                break;
            }

            int pType = ParticleHandler.getType(x + i, bottom);


            if (!ParticleHandler.strongerThan(pType, type)) {
                if (pType != type) {
                    point = x + i;
                    break;
                }
            }
            else {
                break;
            }

        }

        return point;
    }
    private int findY() {

        int point = y;

        for (int i = y + 1; i <= y + speed; i += 1) {

            if (!ParticleHandler.validPoint(x, i)) {
                break;
            }

            int pType = ParticleHandler.getType(x, i);

            if (!ParticleHandler.strongerThan(pType, type) && pType != type) {
                point = i;
            }
            else {
                break;
            }
        }

        return point;
    }
    public void update() {

        int bottom = findY();
        int xDirect = x;

        if (bottom - y < speed / 2 && ParticleHandler.validPoint(x, y + speed)) {

            int[] signs = {1, -1};

            Random random = new Random();

            int firstSign = random.nextInt(0, 2);
            int secondSign = 1 - firstSign;

            int firstDirect = findX(signs[firstSign], y + speed);
            int secondDirect = findX(signs[secondSign], y + speed);

            if (
                    Math.abs(x - firstDirect) < Math.abs(x - secondDirect)
                    && secondDirect != xDirect
            ) {
                xDirect = secondDirect;
                bottom = y + speed;
            }
            else
                if (firstDirect != xDirect)
                {
                xDirect = firstDirect;
                bottom = y + speed;
            }
        }

        putSand(xDirect, bottom);
    }
    public int getWaterAbsorbed() {
        return waterAbsorbed;
    }
    public void absorbWater(Water water) {
        ParticleHandler.remove(water);
        waterAbsorbed += 1;
    }
}
