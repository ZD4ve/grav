package hf.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Main frame for the simulator
 */
public class GravFrame extends JFrame {
    public GravFrame() {
        setTitle("grav");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setResizable(true);
        setLayout(new BorderLayout(20, 20));
        getContentPane().setBackground(ColorTheme.Background);

        add(new ControlPanel(), BorderLayout.WEST);

        Observatory obs = new Observatory();
        add(obs, BorderLayout.CENTER);

        ImageIcon icon = new ImageIcon("src/main/resources/icon.png");
        setIconImage(icon.getImage());
        System.setProperty("sun.java2d.opengl", "true");

        setVisible(true);

        new Renderer();
    }

}
