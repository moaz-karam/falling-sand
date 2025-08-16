import java.awt.*;

public class Constants {

    public static final double PARTICLE_WIDTH = 15;
    public static final double PARTICLE_HEIGHT = 15;

    public static final double FRAMES_PER_SECOND = 80;

    public static final double SCREEN_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    public static final double SCREEN_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().getHeight();

    public static final int SAND = 1;
    public static final int WATER = 2;
    public static final int WOOD = 3;
    public static final int FIRE = 4;

    public static final Color[] TYPE_COLOR =
            {Color.black,
            Color.yellow,
            Color.cyan,
            new Color(150, 111, 51),
            new Color(255, 64, 0)};
}
