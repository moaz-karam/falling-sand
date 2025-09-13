package Main;

import java.awt.*;

public class Water implements Particle {

    private int x;
    private int y;
    private int type;
    private int speed;
    private double xVel;
    private Color color;

    public Water(int x, int y) {
        this.x = x;
        this.y = y;
        speed = Constants.PARTICLE_SPEED;
        xVel = 0;

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
    private int findXDirection(int sign, int yIndex) {
        int point = x;

        for (int i = 1; i <= speed; i += 1) {
            if (!ParticleHandler.validPoint(x + i * sign, yIndex)) {
                break;
            }
            int pType = ParticleHandler.getType(x + i * sign, yIndex);
            if (pType == type) {
                continue;
            }
            else if (pType < type) {
                point = x + i * sign;
            }
            else {
                break;
            }
        }

        return point;
    }
    private int findYDirection() {
        int point = y;

        for (int i = 1; i <= speed; i += 1) {
            if (!ParticleHandler.validPoint(x, y + i)) {
                break;
            }
            int pType = ParticleHandler.getType(x, y + i);
            if (pType == type) {
                continue;
            }
            else if (pType < type) {
                point = y + i;
            }
            else {
                break;
            }
        }

        return point;
    }
    public void update() {

        int bottom = findYDirection();
        int[] signs = {1, -1};
        int firstDirectionSign = (int)(System.nanoTime() % 2);
        int secondDirectionSign = 1 - firstDirectionSign;

        int firstDirection;
        int secondDirection;

        ParticleHandler.setParticle(x, y, null);
        if (bottom - y < speed / 2) {
            firstDirection = findXDirection(signs[firstDirectionSign], bottom);
            secondDirection = findXDirection(signs[secondDirectionSign], bottom);
            if (Math.abs(x - firstDirection) > Math.abs(x - secondDirection)) {
                xVel = x - firstDirection;
            }
            else {
                xVel = x - secondDirection;
            }
        }
        if (!ParticleHandler.validPoint(x - (int)xVel, bottom)) {
            xVel = 0;
        }
        else if (ParticleHandler.getType(x - (int)xVel, bottom) != 0) {
            xVel *= -0.15;
        }
        int newX = x - (int)xVel;
        if (ParticleHandler.getType(newX, bottom) == 0) {
            ParticleHandler.setParticle(newX, bottom, this);
        }
        else {
            ParticleHandler.setParticle(x, bottom, this);
        }

        if (xVel != 0) {
            int sign = (int)(xVel / Math.abs(xVel));
            xVel -= 0.5 * sign;
        }
    }

}