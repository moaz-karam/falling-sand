package Main;

import java.awt.*;

public class Constants {

    public static final double PARTICLE_WIDTH = 4;
    public static final double PARTICLE_HEIGHT = 4;
    public static final int PARTICLE_SPEED = 4;
    public static final int PARTICLE_WATER_SPEED = 2;

    public static final double MOUSE_WIDTH = PARTICLE_WIDTH * 4;
    public static final double MOUSE_HEIGHT = PARTICLE_HEIGHT * 4;

    public static final double FRAMES_PER_SECOND = 120;

    public static final double SCREEN_WIDTH = 800;
    public static final double SCREEN_HEIGHT = 600;


//    public static final double SCREEN_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
//    public static final double SCREEN_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().getHeight();;


    public static final int WATER = 1;
    public static final int SAND = 2;
    public static final int WOOD = 3;
    public static final int FIRE = 4;
    public static final int REMOVE = 5;

    public static final Color[] TYPE_COLOR =
            {Color.black,
            new Color(15,94,156),
            new Color(194, 178, 128),
            new Color(150, 111, 51),
            new Color(255, 64, 0),
            Color.white};
}
