package hf.engine;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SimParameters implements Serializable {
    private final List<Vec2> vectors = Collections.synchronizedList(new ArrayList<>());
    private boolean simRunning = false;
    public SimParameters(){
        vectors.add(new Vec2(5,13));
        vectors.add(new Vec2(8,-63));
        vectors.add(new Vec2(-9,9));
        vectors.add(new Vec2(-53,-79));
        vectors.add(new Vec2(5,7));
        vectors.add(new Vec2(0,1));
    }
    public synchronized Vec2 getVector(int index) {
        return vectors.get(index);
    }
    public synchronized void setVector(int index, Vec2 v) {
        vectors.set(index, v);
    }
    public synchronized void startSim() {
        simRunning = true;
        //TODO
    }
    public synchronized void stopSim() {
        simRunning = false;
        //TODO
    }
    public boolean isSimRunning() {
        return simRunning;
    }
}
