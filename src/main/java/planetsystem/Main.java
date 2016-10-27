package planetsystem;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;

public class Main {

    public void start() {
        new Main.Window().setVisible(true);
    }

    private class Window extends JFrame {

        private final String title = "PlanetarySystem";

        public Window() {
            setTitle(title);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setCursor(getToolkit().createCustomCursor(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB), new Point(), null));
            setUndecorated(true);
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            setBounds(0, 0, screenSize.width, screenSize.height);
            add(new Graphics(screenSize.width, screenSize.height));
            pack();
        }
    }

    public static void main(String[] args) {
        new Main().start();
    }
}
