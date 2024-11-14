package hf.gui;

import java.util.List;

import javax.swing.JComponent;
import javax.swing.Timer;

import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.util.Collections;

/**
 * Timer for rendering components every frame
 */
public class Renderer extends Timer {

    private static List<JComponent> renderQueue = Collections.synchronizedList(new ArrayList<>());

    /**
     * Add a component to the render queue
     * 
     * @param c Component with custom paint
     */
    public static void addComponent(JComponent c) {
        renderQueue.add(c);
    }

    /**
     * Create and start the renderer
     */
    public Renderer() {
        super(6, Renderer::paintAll);
        setInitialDelay(100);
        start();
    }

    /**
     * Force the repaint of all components in the render queue
     */
    private static void paintAll(ActionEvent e) {
        for (JComponent c : renderQueue) {
            c.repaint();
        }
    }

}
