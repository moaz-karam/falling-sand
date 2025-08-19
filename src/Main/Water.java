package Main;

import java.awt.*;

public class Water implements Particle {

    private int x;
    private int y;
    private int type;
    private Color color;

    public Water(int x, int y) {
        this.x = x;
        this.y = y;

        type = Constants.WATER;
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
    public void update() {

        int bottom = y + 1;
        int[] directions = {x + 1, x - 1};
        int firstDirectionIndex = (int)(System.nanoTime() % 2);
        int secondDirectionIndex = 1 - firstDirectionIndex;

        int firstDirection = directions[firstDirectionIndex];
        int secondDirection = directions[secondDirectionIndex];


        if (bottom < ParticleHandler.yPositions && ParticleHandler.getType(x, bottom) == 0) {
            ParticleHandler.setParticle(x, y, null);
            ParticleHandler.setParticle(x, bottom, this);
            setY(bottom);
        }
        else if (firstDirection >= 0 && firstDirection < ParticleHandler.xPositions
                && ParticleHandler.getType(firstDirection, y) == 0) {
            ParticleHandler.setParticle(x, y, null);
            ParticleHandler.setParticle(firstDirection, y, this);
            setX(firstDirection);
        }
        else if (secondDirection >= 0 && secondDirection < ParticleHandler.xPositions
                && ParticleHandler.getType(secondDirection, y) == 0) {
            ParticleHandler.setParticle(x, y, null);
            ParticleHandler.setParticle(secondDirection, y, this);
            setX(secondDirection);
        }
    }
    public void setOnFire() {
    }
    public boolean isOnFire() {
        return false;
    }

}
