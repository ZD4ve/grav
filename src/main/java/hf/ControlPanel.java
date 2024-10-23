package hf;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

public class ControlPanel extends JPanel {

    public ControlPanel(SimParameters simParams) {
        setPreferredSize(new Dimension(400, 600));
        setLayout(new BorderLayout(20,20));

        //Buttons
        JPanel buttonPanel = new JPanel();
        add(buttonPanel, BorderLayout.SOUTH);

        buttonPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets.set(5, 5, 5, 5);
        buttonPanel.add(new JButton("P"),gbc);
        buttonPanel.add(new JButton("R"),gbc);

        //Vector Inputs
        JPanel inputPanel = new JPanel();
        add(inputPanel, BorderLayout.CENTER);

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
