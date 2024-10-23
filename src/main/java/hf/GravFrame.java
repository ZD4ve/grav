package hf;

import javax.swing.*;
import java.awt.*;

public class GravFrame extends JFrame {
    public GravFrame(SimParameters simParams) {
        setTitle("grav");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setResizable(true);
        setLayout(new BorderLayout(20,20));

        add(new ControlPanel(simParams),BorderLayout.WEST);

        JPanel placeholder = new JPanel();
        placeholder.setBackground(Color.ORANGE);
        add(placeholder,BorderLayout.CENTER);



        ImageIcon icon = new ImageIcon("src/main/resources/icon.png");
        setIconImage(icon.getImage());
        setBackground(new Color(0xE0E1E3));

        
        setVisible(true);
    }

}
