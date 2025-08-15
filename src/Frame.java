import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame {

    public Frame(JPanel panel) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setTitle("Falling-Sand");
        add(panel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
