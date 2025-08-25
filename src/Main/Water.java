package Main;

import java.awt.*;

public class Water implements Particle {

    private int x;
    private int y;
    private int type;
    private int speed;
    private Color color;

    public Water(int x, int y) {
        this.x = x;
        this.y = y;
        speed = Constants.PARTICLE_SPEED;

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
    private int findXDirection(int sign) {
        int point = x;

        for (int i = sign * speed; i * sign > 0; i += sign * -1) {
            if (!ParticleHandler.validPoint(x + i, y)) {
                continue;
            }
            int pointType = ParticleHandler.getType(x + i, y);
            if (pointType == 0 && (x + i) * sign > point * sign) {
                point = x + i;
            }
            else if (ParticleHandler.strongerThan(pointType, type)) {
                point = x;
            }
        }

        return point;
    }
    private int findYDirection(int sign) {
        int point = y;
        for (int i = sign * speed; i * sign > 0; i += sign * -1) {
            if (!ParticleHandler.validPoint(x, y + i)) {
                continue;
            }
            int pointType = ParticleHandler.getType(x, y + i);
            if (pointType == 0 && (y + i) * sign > point * sign) {
                point = y + i;
            }
            else if (ParticleHandler.strongerThan(pointType, type)) {
                point = y;
            }
        }

        return point;
    }
    private int findDirection(int coordinate, int sign) {
        if (coordinate == Constants.X_COORDINATE) {
            return findXDirection(sign);
        }
        else if (coordinate == Constants.Y_COORDINATE) {
            return findYDirection(sign);
        }
        return 0;
    }
    public void update() {

        int bottom = findDirection(Constants.Y_COORDINATE, 1);
        int[] signs = {1, -1};
        int firstDirectionSign = (int)(System.nanoTime() % 2);
        int secondDirectionSign = 1 - firstDirectionSign;

        int firstDirection = findDirection(Constants.X_COORDINATE, signs[firstDirectionSign]);
        int secondDirection = findDirection(Constants.X_COORDINATE, signs[secondDirectionSign]);

        if (bottom - y >= 2) {
            ParticleHandler.setParticle(x, y, null);
            ParticleHandler.setParticle(x, bottom, this);
            setY(bottom);
        }
        else if (firstDirection != x) {
            ParticleHandler.setParticle(x, y, null);
            ParticleHandler.setParticle(firstDirection, y, this);
            setX(firstDirection);
        }
        else if (secondDirection != x) {

            ParticleHandler.setParticle(x, y, null);
            ParticleHandler.setParticle(secondDirection, y, this);
            setX(secondDirection);
        }
    }

}
