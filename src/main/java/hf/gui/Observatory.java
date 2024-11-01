package hf.gui;

import java.awt.*;
import javax.swing.JComponent;

import hf.engine.*;

public class Observatory extends JComponent {

    static final int MEMORY = 100;

    SimParameters simParams;
    Vec2[][] history = new Vec2[3][MEMORY];
    double historyIndex = 0;

    public Observatory(SimParameters simulationParameters) {
        simParams = simulationParameters;
        Renderer.addComponent(this);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(ColorTheme.Space);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(
                RenderingHints.KEY_FRACTIONALMETRICS,
                RenderingHints.VALUE_FRACTIONALMETRICS_ON);

        // trail
        g2d.setColor(ColorTheme.Trail);
        for (int p = 0; p < 3; p += 1) {
            for (int i = 0; i < MEMORY; i++) {
                if (history[p][i] != null) {
                    Vec2 pos = worldToScreen(history[p][i]);
                    g2d.fillOval((int) pos.x - 2, (int) pos.y - 2, 4, 4);
                }
            }
        }

        // stars
        g2d.setColor(ColorTheme.Yellow);
        for (int p = 0; p < 3; p++) {
            Vec2 pos = worldToScreen(simParams.getPos(p));
            g2d.fillOval(
                    (int) (pos.x - starRadius()), (int) (pos.y - starRadius()),
                    (int) starRadius() * 2, (int) starRadius() * 2);
            history[p][(int) historyIndex] = simParams.getPos(p);
        }

        // labels
        g2d.setColor(ColorTheme.Trail);
        for (int p = 0; p < 3; p++) {
            Vec2 pos = worldToScreen(simParams.getPos(p));
            g2d.drawLine(
                    (int) (pos.x + Math.cos(Math.PI / 3) * starRadius()),
                    (int) (pos.y - Math.sin(Math.PI / 3) * starRadius()),
                    (int) pos.x + 15, (int) pos.y - 30);
            g2d.drawLine((int) pos.x + 15, (int) pos.y - 30, (int) pos.x + 53, (int) pos.y - 30);
            g2d.drawString("Star " + "αβγ".toCharArray()[p], (int) pos.x + 17, (int) pos.y - 32);
        }

        if (simParams.isRunning()) {
            historyIndex = (historyIndex + 0.1) % MEMORY;
        }
    }

    Vec2 worldToScreen(Vec2 pos) {
        return new Vec2(pos.x + getWidth() / 2.0, pos.y + getHeight() / 2.0);
    }

    double starRadius() {
        return 12;
    }
}
