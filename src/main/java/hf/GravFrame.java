package hf;

import javax.swing.*;
import java.awt.*;

public class GravFrame extends JFrame {
    public GravFrame(SimParameters simParams) {
        super();    

        setTitle("grav");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setResizable(true);
        setLayout(new BorderLayout(20,20));
        getContentPane().setBackground(ColorTheme.BG);

        add(new ControlPanel(simParams),BorderLayout.WEST);

        JPanel placeholder = new JPanel();
        placeholder.setBackground(ColorTheme.SP);
        add(placeholder,BorderLayout.CENTER);



        ImageIcon icon = new ImageIcon("resources/icon.png");
        setIconImage(icon.getImage());

        
        setVisible(true);
    }

}
