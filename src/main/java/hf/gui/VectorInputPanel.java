package hf.gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

import hf.engine.*;

public class VectorInputPanel extends JPanel {
    transient SimParameters simParams;
    final int vectorIndex;

    JSpinner amplitude;
    JSpinner angle;
    Dial dial;

    public VectorInputPanel(int vectorIndex) {
        simParams = SimParameters.getInstance();
        this.vectorIndex = vectorIndex;
        setLayout(new GridBagLayout());
        Dimension panelSize = new Dimension(180, 60 + 2 + 8);
        setSize(panelSize);
        setMinimumSize(panelSize);
        setPreferredSize(panelSize);
        setMaximumSize(panelSize);
        setBorder(new LineBorder(ColorTheme.Blue, 4, true));
        setOpaque(false);

        JLabel label = new JLabel(
                "Star " + "αβγ".charAt(vectorIndex / 2) + (vectorIndex % 2 == 0 ? " pos" : " vel"));
        dial = new Dial(new Dimension(60, 60));
        amplitude = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 99999.0, 1.0));
        angle = new JSpinner(new SpinnerNumberModel(0.0, -360.0, 360.0, 5.0));

        amplitude.addChangeListener(e -> readInput());
        angle.addChangeListener(e -> readInput());
        amplitude.setToolTipText("Amplitude");
        angle.setToolTipText("Angle");

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
        Renderer.addComponent(dial);

        new Timer(100, e -> readData()).start();
    }

    private void readInput() {
        if (simParams.isRunning())
            return;
        double amp = (Double) amplitude.getValue();
        double ang = (Double) angle.getValue();
        simParams.setVector(vectorIndex, Vec2.fromPolar(ang, amp));
    }

    public void readData() {
        Vec2 v = simParams.getVector(vectorIndex);
        amplitude.setValue(v.amplitude());
        angle.setValue(v.angle());
    }

    public void lock() {
        amplitude.setEnabled(false);
        angle.setEnabled(false);
    }

    public void unlock() {
        amplitude.setEnabled(true);
        angle.setEnabled(true);
    }

    public class Dial extends JComponent {
        public Dial(Dimension size) {
            setSize(size);
            setMinimumSize(size);
            setPreferredSize(size);
            setMaximumSize(size);
            setOpaque(false);
            setBackground(ColorTheme.Background);
        }

        @Override
        public void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            Dimension size = this.getSize();
            Dimension rect = new Dimension(Math.min(size.width, size.height), Math.min(size.width, size.height));
            final int circle = 6;
            final int line = 3;
            final int margin = 4;

            g2d.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(
                    RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2d.setRenderingHint(
                    RenderingHints.KEY_FRACTIONALMETRICS,
                    RenderingHints.VALUE_FRACTIONALMETRICS_ON);

            g2d.setStroke(new BasicStroke(circle));
            g2d.setColor(ColorTheme.Yellow);
            g2d.drawOval(
                    circle / 2 + margin,
                    circle / 2 + margin,
                    rect.width - circle - 2 * margin,
                    rect.height - circle - 2 * margin);
            Vec2 v = simParams.getVector(vectorIndex).normalise().scale(rect.width / 2.0 - margin / 2.0);
            g2d.setStroke(new BasicStroke(line));
            g2d.setColor(ColorTheme.Blue);
            g2d.drawLine(
                    rect.width / 2,
                    rect.height / 2,
                    (int) (v.x + rect.width / 2.0),
                    (int) (v.y + rect.height / 2.0));
        }
    }

}
