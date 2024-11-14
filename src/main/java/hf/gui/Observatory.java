package hf.gui;

import java.awt.*;
import javax.swing.JComponent;

import hf.engine.*;

/**
 * Renderer of the current state of the simulation
 * Right side
 */
public class Observatory extends JComponent {
    private static final int MEMORY = 100;
    private static final double STAR_RADIUS = 12;

    private transient SimParameters simParams;
    private Vec2[][] history = new Vec2[3][MEMORY];
    private double historyIndex = 0;

    public Observatory() {
        simParams = SimParameters.getInstance();
        Renderer.addComponent(this);
    }

    /**
     * Paints the current state of the simulation
     */
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
                    (int) (pos.x - STAR_RADIUS), (int) (pos.y - STAR_RADIUS),
                    (int) STAR_RADIUS * 2, (int) STAR_RADIUS * 2);
            history[p][(int) historyIndex] = simParams.getPos(p);
        }

        // labels
        g2d.setColor(ColorTheme.Trail);
        for (int p = 0; p < 3; p++) {
            Vec2 pos = worldToScreen(simParams.getPos(p));
            g2d.drawLine(
                    (int) (pos.x + Math.cos(Math.PI / 3) * STAR_RADIUS),
                    (int) (pos.y - Math.sin(Math.PI / 3) * STAR_RADIUS),
                    (int) pos.x + 15, (int) pos.y - 30);
            g2d.drawLine((int) pos.x + 15, (int) pos.y - 30, (int) pos.x + 53, (int) pos.y - 30);
            g2d.drawString("Star " + "αβγ".charAt(p), (int) pos.x + 17, (int) pos.y - 32);
        }

        if (simParams.isRunning()) {
            historyIndex = (historyIndex + 0.1) % MEMORY;
        }
    }

    private Vec2 worldToScreen(Vec2 pos) {
        return new Vec2(pos.x + getWidth() / 2.0, pos.y + getHeight() / 2.0);
    }
}
