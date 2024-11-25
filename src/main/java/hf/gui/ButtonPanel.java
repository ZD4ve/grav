package hf.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import hf.engine.*;

/**
 * Panel with buttons for controlling the simulation
 * Bottom left corner
 */
public class ButtonPanel extends JPanel {
    private transient SimParameters simParams;
    private JButton play;
    private JButton reset;
    private JButton recenter;
    private JButton save;
    private JButton load;

    private transient Icon playIcon = new ImageIcon("src/main/resources/play.png");
    private transient Icon pauseIcon = new ImageIcon("src/main/resources/pause.png");

    public ButtonPanel() {
        simParams = SimParameters.getInstance();
        setOpaque(false);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets.set(5, 5, 5, 5);

        load = new JButton(new ImageIcon("src/main/resources/folder.png"));
        save = new JButton(new ImageIcon("src/main/resources/save.png"));
        play = new JButton(playIcon);
        reset = new JButton(new ImageIcon("src/main/resources/undo.png"));
        recenter = new JButton(new ImageIcon("src/main/resources/recenter.png"));

        load.addActionListener(this::loadFile);
        save.addActionListener(this::saveFile);
        play.addActionListener(this::playPauseEvent);
        reset.addActionListener(this::resetEvent);
        recenter.addActionListener(e -> simParams.recenter());

        load.setToolTipText("Load configuration from file");
        save.setToolTipText("Save configuration to file");
        play.setToolTipText("Start/Stop simulation");
        reset.setToolTipText("Reset simulation to state before start");
        recenter.setToolTipText("Recenter stars around the center of mass");

        reset.setEnabled(false);

        add(load, gbc);
        add(save, gbc);
        add(play, gbc);
        add(reset, gbc);
        add(recenter, gbc);

    }

    /**
     * Start/Stop simulation
     */
    private void playPauseEvent(ActionEvent e) {
        var parent = (ControlPanel) getParent();
        if (simParams.isRunning()) {
            // STOP
            simParams.stopSim();
            play.setIcon(playIcon);
            load.setEnabled(true);
            save.setEnabled(true);
            reset.setEnabled(false);
            recenter.setEnabled(true);
            parent.unlockVectorInputs();
        } else {
            // START
            parent.lockVectorInputs();
            load.setEnabled(false);
            save.setEnabled(false);
            reset.setEnabled(true);
            recenter.setEnabled(false);
            play.setIcon(pauseIcon);
            simParams.startSim();
        }
    }

    /**
     * Reset simulation
     */
    private void resetEvent(ActionEvent e) {
        var parent = (ControlPanel) getParent();
        simParams.resetSim();
        play.setIcon(playIcon);
        load.setEnabled(true);
        save.setEnabled(true);
        reset.setEnabled(false);
        recenter.setEnabled(true);
        parent.unlockVectorInputs();
    }

    /**
     * Show open file dialog and load configuration from file
     */
    private void loadFile(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Star configuration", SimParameters.FILE_EXTENSION);
        chooser.setFileFilter(filter);
        chooser.setAcceptAllFileFilterUsed(false);

        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                simParams.loadFromFile(chooser.getSelectedFile());
            } catch (Exception exc) {
                JOptionPane.showMessageDialog(this,
                        "Error importing from file\n" + exc.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }

        }
    }

    /**
     * Show save file dialog and save configuration to file
     */
    private void saveFile(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Star configuration", SimParameters.FILE_EXTENSION);
        chooser.setFileFilter(filter);
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setSelectedFile(new File("horoscope." + SimParameters.FILE_EXTENSION));

        int returnVal = chooser.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            simParams.saveToFile(chooser.getSelectedFile());
        }
    }

}
