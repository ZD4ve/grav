package hf.gui;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.RenderingHints;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.LineBorder;

import hf.engine.SimParameters;
import hf.engine.Vec2;

public class VectorInputPanel extends JPanel {
    SimParameters p;
    final int vectorIndex;

    JSpinner amplitude;
    JSpinner angle;
    Dial dial;

    public VectorInputPanel(SimParameters simParams, int vectorIndex) {
        super();

        p = simParams;
        this.vectorIndex = vectorIndex;
        setLayout(new GridBagLayout());
        Dimension panelSize = new Dimension(180, 60 + 2 + 8);
        setSize(panelSize);
        setMinimumSize(panelSize);
        setPreferredSize(panelSize);
        setMaximumSize(panelSize);
        setBorder(new LineBorder(ColorTheme.PR, 4, true));
        setOpaque(false);

        JLabel label = new JLabel("Star " + (vectorIndex / 2 + 1) + (vectorIndex % 2 == 0 ? " pos" : " vel"));
        dial = new Dial(new Dimension(60, 60));
        amplitude = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 99999.0, 1.0));
        angle = new JSpinner(new SpinnerNumberModel(0.0, -180.0, 180.0, 5.0));

        amplitude.addChangeListener(e -> readInput());
        angle.addChangeListener(e -> readInput());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets.set(1, 5, 1, 5);

        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 3;
        add(dial, gbc);

        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.gridheight = 1;
        gbc.gridwidth = 2;
        gbc.gridx = 1;
        gbc.gridy = 0;

        add(label, gbc);
        gbc.gridy = 1;
        add(amplitude, gbc);
        gbc.gridy = 2;
        add(angle, gbc);

        readData();
    }

    private void readInput() {
        double amp = (Double) amplitude.getValue();
        double ang = (Double) angle.getValue();
        p.setVector(vectorIndex, Vec2.fromPolar(ang, amp));
        dial.repaint(0, 0, dial.getWidth(), dial.getHeight());
    }

    public void readData() {
        Vec2 v = p.getVector(vectorIndex);
        amplitude.setValue(v.amplitude());
        angle.setValue(v.angle());
        dial.repaint(0, 0, dial.getWidth(), dial.getHeight());
    }

    public void lock() {
        amplitude.setEnabled(false);
        angle.setEnabled(false);
    }

    public void unlock() {
        amplitude.setEnabled(true);
        angle.setEnabled(true);
    }

    public class Dial extends Canvas {
        public Dial(Dimension size) {
            super();
            setSize(size);
            setMinimumSize(size);
            setPreferredSize(size);
            setMaximumSize(size);
            setOpaque(false);
            setBackground(ColorTheme.BG);
        }

        @Override
        public void paint(Graphics g) {
            var parent = (VectorInputPanel) this.getParent();

            Graphics2D g2 = (Graphics2D) g;
            Dimension size = this.getSize();
            Dimension rect = new Dimension(Math.min(size.width, size.height), Math.min(size.width, size.height));
            final int circle = 6;
            final int line = 3;
            final int margin = 4;

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(
                    RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setRenderingHint(
                    RenderingHints.KEY_FRACTIONALMETRICS,
                    RenderingHints.VALUE_FRACTIONALMETRICS_ON);

            g2.setStroke(new BasicStroke(circle));
            g2.setColor(ColorTheme.SE);
            g2.drawOval(
                    circle / 2 + margin,
                    circle / 2 + margin,
                    rect.width - circle - 2 * margin,
                    rect.height - circle - 2 * margin);

            Vec2 v = parent.p.getVector(vectorIndex).normalise();
            int lenght = rect.width / 2 - margin / 2;
            g2.setStroke(new BasicStroke(line));
            g.setColor(ColorTheme.PR);
            g2.drawLine(
                    rect.width / 2,
                    rect.height / 2,
                    (int) (v.x * lenght + rect.width / 2.0),
                    (int) (v.y * lenght + rect.height / 2.0));
        }
    }

}
