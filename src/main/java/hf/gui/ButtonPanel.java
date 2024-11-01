package hf.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import hf.engine.*;

public class ButtonPanel extends JPanel {
    transient SimParameters simParams;
    JButton play;
    JButton reset;
    JButton recenter;
    JButton save;
    JButton load;

    transient Icon playIcon = new ImageIcon("resources/play.png");
    transient Icon pauseIcon = new ImageIcon("resources/pause.png");

    public ButtonPanel() {
        simParams = SimParameters.getInstance();
        setOpaque(false);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets.set(5, 5, 5, 5);

        load = new JButton(new ImageIcon("resources/folder.png"));
        save = new JButton(new ImageIcon("resources/save.png"));
        play = new JButton(playIcon);
        reset = new JButton(new ImageIcon("resources/undo.png"));
        recenter = new JButton(new ImageIcon("resources/recenter.png"));

        load.addActionListener(this::loadFile);
        save.addActionListener(this::saveFile);
        play.addActionListener(this::playPauseEvent);
        reset.addActionListener(this::resetEvent);
        recenter.addActionListener(e -> simParams.recenter());

        reset.setEnabled(false);

        add(load, gbc);
        add(save, gbc);
        add(play, gbc);
        add(reset, gbc);
        add(recenter, gbc);

    }

    private void playPauseEvent(ActionEvent e) {
        var parent = (ControlPanel) getParent();
        if (simParams.isRunning()) {
            // STOP
            simParams.stopSim();
            load.setEnabled(true);
            save.setEnabled(true);
            reset.setEnabled(false);
            recenter.setEnabled(true);

            play.setIcon(playIcon);
            parent.vectorInputs.forEach(v -> {
                v.readData();
                v.unlock();
            });
        } else {
            // START
            load.setEnabled(false);
            save.setEnabled(false);
            reset.setEnabled(true);
            recenter.setEnabled(false);
            play.setIcon(pauseIcon);
            parent.vectorInputs.forEach(v -> v.lock());
            simParams.startSim();
        }
    }

    private void resetEvent(ActionEvent e) {
        var parent = (ControlPanel) getParent();
        simParams.resetSim();
        reset.setEnabled(false);
        play.setIcon(playIcon);
        parent.vectorInputs.forEach(v -> {
            v.readData();
            v.unlock();
        });
    }

    private void loadFile(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Star configuration", SimParameters.FILE_EXTENSION);
        chooser.setFileFilter(filter);
        chooser.setAcceptAllFileFilterUsed(false);

        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            simParams.loadFromFile(chooser.getSelectedFile());
        }
    }

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
