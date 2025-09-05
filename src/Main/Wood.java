package Main;

import java.awt.*;

public class Wood implements Particle {

    private int x;
    private int y;
    private int type;
    private Color color;
    private boolean onFire;
    private boolean burnTheSurroundings;
    private long firingTime;
    private long lastFireUpdate;
    private static final double VANISH_TIME = 0.5;
    private static final double FIRING_STEPS = 3;

    public Wood(int x, int y) {
        this.x = x;
        this.y = y;

        type = Constants.WOOD;
        color = Constants.TYPE_COLOR[type];
        onFire = false;
        burnTheSurroundings = false;
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
    public int getSpeed() {
        return 0;
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

        if (!onFire) {
            return;
        }

        int minX = x - 1;
        int maxX = x + 1;

        int minY = y - 1;
        int maxY = y + 1;

        if (minX < 0) {
            minX += 1;
        }
        if (minY < 0) {
            minY += 1;
        }

        for (int yIndex = minY; yIndex <= maxY; yIndex += 1) {
            for (int xIndex = minX; xIndex <= maxX; xIndex += 1) {
                if (!ParticleHandler.validPoint(xIndex, yIndex)) {
                    continue;
                }
                if (ParticleHandler.getType(xIndex, yIndex) == Constants.WATER) {
                    putOffFire();
                    return;
                }
            }
        }

        long now = System.nanoTime();


        if ((now - firingTime) / 1_000_000_000.0 >= VANISH_TIME) {
            ParticleHandler.remove(this);
        }

        else if (burnTheSurroundings && (now - firingTime) / 1_000_000_000.0 >= (VANISH_TIME / 8)) {
            for (int yIndex = minY; yIndex <= maxY; yIndex += 1) {
                for (int xIndex = minX; xIndex <= maxX; xIndex += 1) {

                    if (!ParticleHandler.validPoint(xIndex, yIndex)) {
                        continue;
                    }

                    color = color.brighter();
                    burnTheSurroundings = false;
                    if (ParticleHandler.getType(xIndex, yIndex) != 0) {
                        Particle p = ParticleHandler.getParticle(xIndex, yIndex);
                        if (p.getType() == Constants.WOOD) {
                            Wood wood = (Wood)p;
                            if (!wood.isOnFire()) {
                                wood.setOnFire();
                            }
                        } else {
                            p.setColor(p.getColor().darker());
                        }
                    }
                }
            }
        }
        else if ((now - lastFireUpdate) / 1_000_000_000.0 >= VANISH_TIME / FIRING_STEPS) {
            for (int yIndex = minY; yIndex <= maxY; yIndex += 1) {
                for (int xIndex = minX; xIndex <= maxX ; xIndex += 1) {

                    if (!ParticleHandler.validPoint(xIndex, yIndex)) {
                        continue;
                    }

                    lastFireUpdate = now;
                    if (ParticleHandler.getType(xIndex, yIndex) != 0) {
                        Particle p = ParticleHandler.getParticle(xIndex, yIndex);
                        p.setColor(p.getColor().darker());
                    }
                }
            }
        }



    }
    public boolean isOnFire() {
        return onFire;
    }
    public void setOnFire() {
        firingTime = System.nanoTime();
        lastFireUpdate = System.nanoTime();
        color = Constants.TYPE_COLOR[Constants.FIRE];
        onFire = true;
        burnTheSurroundings = true;
    }
    private void putOffFire() {
        color = Constants.TYPE_COLOR[Constants.WOOD];
        color = color.darker();
        onFire = false;
    }
}
