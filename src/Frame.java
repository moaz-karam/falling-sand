import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class Frame extends JFrame {

    public Frame(JPanel panel) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setTitle("Falling-Sand");
        add(panel);
        setUndecorated(true);
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

        if (gd.isFullScreenSupported()) {
            setUndecorated(true);
            setResizable(false);

            addFocusListener(new FocusListener() {
                @Override
                public void focusGained(FocusEvent e) {
                    setAlwaysOnTop(true);
                }

                @Override
                public void focusLost(FocusEvent e) {
                    setAlwaysOnTop(false);
                }
            });

            gd.setFullScreenWindow(this);
        } else {
            System.out.println("Full-screen exclusive mode not supported on this device.");
            setVisible(true); // Show in windowed mode if full-screen is not supported
        }
    }
}
