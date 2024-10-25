package hf.gui;

import javax.swing.*;
import java.awt.*;
import hf.engine.*;

public class Observatory extends Canvas {

    SimParameters simParams;

    public Observatory(SimParameters simulationParameters) {
        simParams = simulationParameters;
        new Thread(this::watchForChanges).start();
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(ColorTheme.SP);
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.setColor(ColorTheme.SE);

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(
                RenderingHints.KEY_FRACTIONALMETRICS,
                RenderingHints.VALUE_FRACTIONALMETRICS_ON);

        for (int i = 0; i < 3; i += 1) {
            Vec2 pos = simParams.getPos(i);
            g2.fillOval((int) pos.x + getWidth() / 2, (int) pos.y + getHeight() / 2, 20, 20);
        }

    }

    private void watchForChanges() {
        while (true) {
            try {
                Thread.sleep(40);
                synchronized (simParams) {
                    simParams.wait();
                }
                SwingUtilities.invokeLater(() -> repaint(0, 0, getWidth(), getHeight()));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        }
    }

}
