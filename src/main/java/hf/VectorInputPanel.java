package hf;

import java.awt.BasicStroke;
import java.awt.Button;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class VectorInputPanel extends JPanel {
    SimParameters p;
    final int vectorIndex;

    public VectorInputPanel(SimParameters simParams, int vectorIndex) {
        p = simParams;
        this.vectorIndex = vectorIndex;
        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(180, 62));
        
        Dial dial = new Dial();
        JLabel label = new JLabel("Vector " + vectorIndex);
        JTextField amplitude = new JTextField();
        JTextField angle = new JTextField();

        dial.setPreferredSize(new Dimension(60, 60));
        
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets.set(1,5,1,5);

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

    }

    public class Dial extends Canvas {
        @Override
        public void paint(Graphics g) {
            var parent = (VectorInputPanel) this.getParent();

            Graphics2D g2 = (Graphics2D) g;
            Dimension size = this.getSize();
            Dimension rect = new Dimension(Math.min(size.width, size.height), Math.min(size.width, size.height));
            final int thick = 4;
            final int thin = 2;

            g2.setStroke(new BasicStroke(thick));
            g2.setColor(Color.BLACK);
            g2.drawOval(thick / 2, thick / 2, rect.width - thick, rect.height - thick);

            Vec2 v = parent.p.getVector(vectorIndex).normalise();
            int lenght = rect.width / 2 - 2 * thick;
            g2.setStroke(new BasicStroke(thin));
            g.setColor(Color.RED);
            g2.drawLine(rect.width / 2, rect.height / 2,
                    (int) (v.x * lenght + rect.width / 2.0), (int) (v.y * lenght + rect.height / 2.0));
        }
    }

}
