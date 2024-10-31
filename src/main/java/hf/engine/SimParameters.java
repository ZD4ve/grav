package hf.engine;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.SwingUtilities;

public class SimParameters implements Serializable {
    private final List<Vec2> vectors = Collections.synchronizedList(new ArrayList<>());
    private final double mass = 1e14;// kg 1.989e30s

    private transient List<Vec2> stateBeforeStart;
    private transient boolean simRunning = false;
    private transient List<Runnable> runOnChange;

    public SimParameters() {
        vectors.add(new Vec2(5, 13));
        vectors.add(new Vec2(8, -63));
        vectors.add(new Vec2(-9, 9));
        vectors.add(new Vec2(-53, -79));
        vectors.add(new Vec2(5, 7));
        vectors.add(new Vec2(0, 1));
        new Thread(this::changeListener).start();

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
        notifyAll();
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
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
        vectors.clear();
        vectors.addAll(stateBeforeStart);
        notifyAll();
    }

    public synchronized boolean isRunning() {
        return simRunning;
    }

    public void onChange(Runnable task) {
        if (runOnChange == null) {
            runOnChange = Collections.synchronizedList(new ArrayList<>());
        }
        runOnChange.add(task);
    }

    private void changeListener() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
        if (runOnChange == null) {
            runOnChange = Collections.synchronizedList(new ArrayList<>());
        }
        while (true) {
            try {
                synchronized (this) {
                    this.wait();
                }
                Thread.sleep(40);
                runOnChange.forEach(SwingUtilities::invokeLater);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        }

    }
}
