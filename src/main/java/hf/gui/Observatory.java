package hf.gui;

import java.awt.*;
import hf.engine.*;

public class Observatory extends Canvas {

    static final int MEMORY = 100;

    SimParameters simParams;
    Vec2[][] history = new Vec2[3][MEMORY];

    int historyIndex = 0;

    public Observatory(SimParameters simulationParameters) {
        simParams = simulationParameters;
        simParams.onChange(() -> repaint(0, 0, getWidth(), getHeight()));
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(ColorTheme.SP);
        g2.fillRect(0, 0, getWidth(), getHeight());

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(
                RenderingHints.KEY_FRACTIONALMETRICS,
                RenderingHints.VALUE_FRACTIONALMETRICS_ON);

        g2.setColor(ColorTheme.PR);
        for (int p = 0; p < 3; p += 1) {
            for (int i = 0; i < MEMORY; i++) {
                if (history[p][i] != null) {
                    Vec2 pos = worldToScreen(history[p][i]);
                    g2.fillOval((int) pos.x - 3, (int) pos.y - 3, 6, 6);
                }
            }

        }
        for (int p = 0; p < 3; p++) {
            g2.setColor(ColorTheme.SE);
            Vec2 pos = worldToScreen(simParams.getPos(p));
            g2.fillOval((int) pos.x - 10, (int) pos.y - 10, 20, 20);
            history[p][historyIndex] = simParams.getPos(p);
        }
        historyIndex = (historyIndex + 1) % MEMORY;

    }

    Vec2 worldToScreen(Vec2 pos) {
        return new Vec2(pos.x + getWidth() / 2.0, pos.y + getHeight() / 2.0);
    }
}
