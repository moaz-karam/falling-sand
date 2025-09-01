package Main;

import java.awt.Toolkit;

public class Main {
    private final Menu menu;
    private final Panel panel;
    private final Frame frame;

    public Main() {
        menu = new Menu();
        panel = new Panel();
        frame = new Frame(panel);
        panel.requestFocusInWindow();
    }
    public static void main(String[] args) {
        Main main = new Main();
    }
}