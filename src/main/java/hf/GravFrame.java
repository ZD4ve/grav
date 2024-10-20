package hf;

import javax.swing.*;
import java.awt.*;

public class GravFrame extends JFrame {
    public GravFrame() {
        this.setTitle("grav");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(1000, 600);
        this.setResizable(false);

        ImageIcon icon = new ImageIcon("src/main/resources/icon.png");
        this.setIconImage(icon.getImage());
        this.setBackground(new Color(0xE0E1E3));
        
        this.setVisible(true);
    }

}
