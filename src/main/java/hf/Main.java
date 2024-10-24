package hf;

import hf.engine.SimParameters;
import hf.engine.Space;
import hf.gui.GravFrame;

public class Main {
    public static void main(String[] args) {
        SimParameters simParams = new SimParameters();
        new GravFrame(simParams);
        Space sp = new Space(simParams);
        sp.simulate();
    }
}