package hf.gui;

import java.util.List;

import javax.swing.JComponent;
import javax.swing.Timer;

import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.util.Collections;

public class Renderer extends Timer {

    private static List<JComponent> renderQueue = Collections.synchronizedList(new ArrayList<>());

    public static void addComponent(JComponent c) {
        renderQueue.add(c);
    }

    public Renderer() {
        super(6, Renderer::paintAll);
        setInitialDelay(100);
        start();
    }

    private static void paintAll(ActionEvent e) {
        for (JComponent c : renderQueue) {
            c.repaint();
        }
    }

}
