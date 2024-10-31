package hf.gui;

import java.util.List;

import javax.swing.Timer;

import java.util.ArrayList;
import java.awt.Canvas;
import java.awt.event.ActionEvent;
import java.util.Collections;

public class Renderer extends Timer {

    private static List<Canvas> target = Collections.synchronizedList(new ArrayList<>());

    public static void addCanvas(Canvas c) {
        target.add(c);
    }

    public Renderer() {
        super(40, Renderer::paintAll);
        setInitialDelay(100);
        start();
        for (Canvas canvas : target) {
            canvas.createBufferStrategy(2);
        }
    }

    private static void paintAll(ActionEvent e) {
        for (Canvas canvas : target) {
            canvas.repaint();
        }
    }

}
