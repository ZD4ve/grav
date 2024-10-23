package hf;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class ControlPanel extends JPanel {

    public ControlPanel(SimParameters simParams) {
        super();
        setPreferredSize(new Dimension(400, 600));
        setLayout(new BorderLayout(20,20));
        setOpaque(false);

        //Buttons
        JPanel buttonPanel = new JPanel();
        add(buttonPanel, BorderLayout.SOUTH);
        
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets.set(5, 5, 5, 5);

        Icon playIcon = new ImageIcon("resources/play.png");
        JButton play = new JButton(playIcon);
        Icon resetIcon = new ImageIcon("resources/undo.png");
        JButton reset = new JButton(resetIcon);

        buttonPanel.add(play,gbc);
        buttonPanel.add(reset,gbc);

        //Vector Inputs
        JPanel inputPanel = new JPanel();
        add(inputPanel, BorderLayout.CENTER);

        inputPanel.setOpaque(false);
        inputPanel.setLayout(new GridBagLayout());


        gbc = new GridBagConstraints();
        gbc.insets.set(30,10,30,10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new VectorInputPanel(simParams, 0), gbc);
        gbc.gridx = 1;
        inputPanel.add(new VectorInputPanel(simParams, 1), gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new VectorInputPanel(simParams, 2), gbc);
        gbc.gridx = 1;
        inputPanel.add(new VectorInputPanel(simParams, 3), gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(new VectorInputPanel(simParams, 4), gbc);
        gbc.gridx = 1;
        inputPanel.add(new VectorInputPanel(simParams, 5), gbc);
    }
}
