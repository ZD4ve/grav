package hf.gui;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;

public class ControlPanel extends JPanel {

    ArrayList<VectorInputPanel> vectorInputs = new ArrayList<>();

    public ControlPanel() {
        setPreferredSize(new Dimension(400, 600));
        setLayout(new BorderLayout(20, 20));
        setOpaque(false);

        // Buttons
        ButtonPanel buttons = new ButtonPanel();
        add(buttons, BorderLayout.SOUTH);

        // Vector Inputs
        JPanel inputPanel = new JPanel();
        add(inputPanel, BorderLayout.CENTER);

        inputPanel.setOpaque(false);
        inputPanel.setLayout(new GridBagLayout());

        vectorInputs.add(new VectorInputPanel(0));
        vectorInputs.add(new VectorInputPanel(1));
        vectorInputs.add(new VectorInputPanel(2));
        vectorInputs.add(new VectorInputPanel(3));
        vectorInputs.add(new VectorInputPanel(4));
        vectorInputs.add(new VectorInputPanel(5));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets.set(30, 10, 30, 10);
        gbc.gridx = 0;
        gbc.gridy = -1;
        for (int i = 0; i < 6; i++) {
            if (i % 2 == 0) {
                gbc.gridx = 0;
                gbc.gridy++;
            } else {
                gbc.gridx = 1;
            }
            inputPanel.add(vectorInputs.get(i), gbc);
        }
    }
}
