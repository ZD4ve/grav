package hf;

import hf.engine.Space;
import hf.gui.GravFrame;

public class Main {
    public static void main(String[] args) {
        new GravFrame();
        new Space().simulate();
    }
}