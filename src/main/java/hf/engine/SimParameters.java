package hf.engine;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SimParameters implements Serializable {
    private final List<Vec2> vectors = Collections.synchronizedList(new ArrayList<>());
    private final double mass = 1e16;// kg 1.989e30s

    private transient List<Vec2> stateBeforeStart;
    private transient boolean simRunning = false;

    public SimParameters() {
        vectors.add(Vec2.fromPolar(0, 100));
        vectors.add(Vec2.fromPolar(90, 50));
        vectors.add(Vec2.fromPolar(120, 100));
        vectors.add(Vec2.fromPolar(210, 50));
        vectors.add(Vec2.fromPolar(240, 100));
        vectors.add(Vec2.fromPolar(330, 50));
    }

    public synchronized void recenter() {
        Vec2 sum = new Vec2(0, 0);
        for (int i = 0; i < vectors.size() / 2; i++) {
            sum = sum.add(getPos(i));
        }
        Vec2 offset = sum.scale(-2.0 / vectors.size());
        for (int i = 0; i < vectors.size() / 2; i++) {
            setPos(i, getPos(i).add(offset));
        }
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

    public synchronized double getMass() {
        return mass;
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
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
        vectors.clear();
        vectors.addAll(stateBeforeStart);
    }

    public synchronized boolean isRunning() {
        return simRunning;
    }
}
