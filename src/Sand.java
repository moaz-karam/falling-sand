import java.awt.*;

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
            Particle tempP = ParticleHandler.getParticle(x, bottom);

            if (ParticleHandler.strongerThan(type, bottomType)) {
                return;
            }

            if (bottomType == 0) {
                ParticleHandler.setParticle(x, y, null);
            }

            if (bottomType == Constants.WATER) {
                if (checkRight && ParticleHandler.getType(right, y) == 0) {
                    ParticleHandler.setParticle(right, y, tempP);
                    tempP.setX(right);
                    tempP.setY(y);
                    ParticleHandler.setParticle(x, y, null);
                }
                else if (checkLeft && ParticleHandler.getType(left, y) == 0) {
                    ParticleHandler.setParticle(left, y, tempP);
                    tempP.setX(left);
                    tempP.setY(y);
                    ParticleHandler.setParticle(x, y, null);
                }
                else {
                    ParticleHandler.setParticle(x, y, tempP);
                    tempP.setY(y);
                }
            }

            ParticleHandler.setParticle(x, bottom, this);
            setY(bottom);
        }
        else if (checkRight && !ParticleHandler.strongerThan(type, bottomRightType)) {
            Particle tempParticle = ParticleHandler.getParticle(right, bottom);

            switch (bottomRightType) {
                case 0:
                    ParticleHandler.setParticle(x, y, null);
                    break;
                case Constants.WATER:
                    if (ParticleHandler.getType(right, y) == 0) {
                        ParticleHandler.setParticle(right, y, tempParticle);
                        tempParticle.setY(y);
                        ParticleHandler.setParticle(x, y, null);
                    }
                    else {
                        ParticleHandler.setParticle(x, y, tempParticle);
                        tempParticle.setX(x);
                        tempParticle.setY(y);
                    }
                    break;
            }
            ParticleHandler.setParticle(right, bottom, this);
            setX(right);
            setY(bottom);
        }
        else if (checkLeft && !ParticleHandler.strongerThan(type, bottomLeftType)) {
            Particle tempParticle = ParticleHandler.getParticle(left, bottom);

            switch(bottomLeftType) {
                case 0:
                    ParticleHandler.setParticle(x, y, null);
                    break;
                case Constants.WATER:
                    if (ParticleHandler.getType(left, y) == 0) {
                        ParticleHandler.setParticle(left, y, tempParticle);
                        tempParticle.setY(y);
                        ParticleHandler.setParticle(x, y, null);
                    }
                    else {
                        ParticleHandler.setParticle(x, y, tempParticle);
                        tempParticle.setX(x);
                        tempParticle.setY(y);
                    }
                    break;
            }

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
