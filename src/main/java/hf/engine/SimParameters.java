package hf.engine;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SimParameters implements Serializable {
    private final List<Vec2> vectors = Collections.synchronizedList(new ArrayList<>());
    private transient List<Vec2> stateBeforeStart;
    private transient boolean simRunning = false;

    public SimParameters() {
        vectors.add(new Vec2(5, 13));
        vectors.add(new Vec2(8, -63));
        vectors.add(new Vec2(-9, 9));
        vectors.add(new Vec2(-53, -79));
        vectors.add(new Vec2(5, 7));
        vectors.add(new Vec2(0, 1));
    }

    public synchronized Vec2 getVector(int index) {
        return vectors.get(index);
    }

    public synchronized void setVector(int index, Vec2 v) {
        vectors.set(index, v);
    }

    public synchronized Vec2 getVel(int index) {
        return vectors.get(index * 2 + 1);
    }

    public synchronized void setVel(int index, Vec2 v) {
        vectors.set(index * 2 + 1, v);
    }

    public synchronized Vec2 getPos(int index) {
        return vectors.get(index * 2);
    }

    public synchronized void setPos(int index, Vec2 v) {
        vectors.set(index * 2, v);
    }

    public synchronized void startSim() {
        simRunning = true;
        notifyAll();
        stateBeforeStart = new ArrayList<>(vectors);
    }

    public synchronized void stopSim() {
        simRunning = false;
    }

    public synchronized void resetSim() {
        stopSim();
        try {
            this.wait(1000);// NOSONAR
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
        vectors.clear();
        vectors.addAll(stateBeforeStart);
    }

    public synchronized boolean isRunning() {
        return simRunning;
    }
}
