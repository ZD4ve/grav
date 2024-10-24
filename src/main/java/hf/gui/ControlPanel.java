package hf.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import hf.engine.SimParameters;

public class ControlPanel extends JPanel {
    SimParameters simParams;
    JButton play;
    JButton reset;

    transient Icon playIcon = new ImageIcon("resources/play.png");
    transient Icon pauseIcon = new ImageIcon("resources/pause.png");
    ArrayList<VectorInputPanel> vectorInputs = new ArrayList<>();

    public ControlPanel(SimParameters simParams) {
        super();
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

        Icon resetIcon = new ImageIcon("resources/undo.png");
        play = new JButton(playIcon);
        reset = new JButton(resetIcon);
        play.addActionListener(e -> playPressed());
        reset.addActionListener(e -> resetPressed());
        reset.setEnabled(false);

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
    }

    private void playPressed() {
        if (simParams.isRunning()) {
            // STOP
            simParams.stopSim();
            reset.setEnabled(false);
            play.setIcon(playIcon);
            vectorInputs.forEach(v -> {
                v.readData();
                v.unlock();
            });
        } else {
            // START
            simParams.startSim();
            reset.setEnabled(true);
            play.setIcon(pauseIcon);
            vectorInputs.forEach(v -> v.lock());
            new Thread(this::watchForChanges).start();
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

    private void watchForChanges() {
        while (simParams.isRunning()) {
            try {
                Thread.sleep(40);
                synchronized (simParams) {
                    simParams.wait();
                }
                SwingUtilities.invokeLater(() -> vectorInputs.forEach(v -> v.readData()));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        }
    }
}
