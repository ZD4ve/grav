package hf.gui;

import javax.swing.*;
import java.awt.*;

import hf.engine.SimParameters;

public class GravFrame extends JFrame {
    public GravFrame(SimParameters simParams) {
        setTitle("grav");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setResizable(true);
        setLayout(new BorderLayout(20, 20));
        getContentPane().setBackground(ColorTheme.Background);

        add(new ControlPanel(simParams), BorderLayout.WEST);

        Observatory obs = new Observatory(simParams);
        add(obs, BorderLayout.CENTER);

        ImageIcon icon = new ImageIcon("resources/icon.png");
        setIconImage(icon.getImage());

        setVisible(true);

        new Renderer();
    }

}
