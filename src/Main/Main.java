package Main;

public class Main {

    private Thread mainThread;
    private Window window;
    public Main() {
        window = new Window();
        window.run();
    }

    public static void main(String[] args) {
        new Main();
    }
}