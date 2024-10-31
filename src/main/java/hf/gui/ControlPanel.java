package hf.gui;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;

import hf.engine.SimParameters;

public class ControlPanel extends JPanel {
    SimParameters simParams;
    JButton play;
    JButton reset;
    JButton recenter;

    transient Icon playIcon = new ImageIcon("resources/play.png");
    transient Icon pauseIcon = new ImageIcon("resources/pause.png");
    transient Icon resetIcon = new ImageIcon("resources/undo.png");
    transient Icon recenterIcon = new ImageIcon("resources/recenter.png");
    ArrayList<VectorInputPanel> vectorInputs = new ArrayList<>();

    public ControlPanel(SimParameters simParams) {
        this.simParams = simParams;
        setPreferredSize(new Dimension(400, 600));
        setLayout(new BorderLayout(20, 20));
        setOpaque(false);

        // Buttons
        JPanel buttonPanel = new JPanel();
        add(buttonPanel, BorderLayout.SOUTH);

        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets.set(5, 5, 5, 5);

        play = new JButton(playIcon);
        reset = new JButton(resetIcon);
        recenter = new JButton(recenterIcon);
        play.addActionListener(e -> playPressed());
        reset.addActionListener(e -> resetPressed());
        recenter.addActionListener(e -> simParams.recenter());
        reset.setEnabled(false);

        buttonPanel.add(recenter, gbc);
        buttonPanel.add(play, gbc);
        buttonPanel.add(reset, gbc);

        // Vector Inputs
        JPanel inputPanel = new JPanel();
        add(inputPanel, BorderLayout.CENTER);

        inputPanel.setOpaque(false);
        inputPanel.setLayout(new GridBagLayout());

        vectorInputs.add(new VectorInputPanel(simParams, 0));
        vectorInputs.add(new VectorInputPanel(simParams, 1));
        vectorInputs.add(new VectorInputPanel(simParams, 2));
        vectorInputs.add(new VectorInputPanel(simParams, 3));
        vectorInputs.add(new VectorInputPanel(simParams, 4));
        vectorInputs.add(new VectorInputPanel(simParams, 5));

        gbc = new GridBagConstraints();
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

        new Thread(this::updateInputsOnChange, "Input Updater").start();

    }

    private void updateInputsOnChange() {
        while (true) {
            try {
                synchronized (this) {
                    this.wait();
                }
                SwingUtilities.invokeLater(() -> vectorInputs.forEach(v -> v.readData()));
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }

    private void playPressed() {
        if (simParams.isRunning()) {
            // STOP
            simParams.stopSim();
            reset.setEnabled(false);
            recenter.setEnabled(true);
            play.setIcon(playIcon);
            vectorInputs.forEach(v -> {
                v.readData();
                v.unlock();
            });
        } else {
            // START
            simParams.startSim();
            reset.setEnabled(true);
            recenter.setEnabled(false);
            play.setIcon(pauseIcon);
            vectorInputs.forEach(v -> v.lock());
        }
    }

    private void resetPressed() {
        simParams.resetSim();
        reset.setEnabled(false);
        play.setIcon(playIcon);
        vectorInputs.forEach(v -> {
            v.readData();
            v.unlock();
        });
    }

}
