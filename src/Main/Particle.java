package Main;

import java.awt.*;

public interface Particle {

    public int getX();
    public int getY();
    public int getType();
    public Color getColor();

    public void setX(int x);
    public void setY(int y);
    public void setType(int t);

    public void setColor(Color c);
    public void update();
    public void setOnFire();
    public boolean isOnFire();
}
