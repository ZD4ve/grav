package hf.engine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.json.*;

public class SimParameters {
    public static final String FILE_EXTENSION = "grav";

    private static SimParameters instance;

    private final List<Vec2> vectors = Collections.synchronizedList(new ArrayList<>());
    private double mass = 1e16;// kg 1.989e30s
    private List<Vec2> stateBeforeStart;
    private boolean simRunning = false;

    public SimParameters() {
        if (instance != null) {
            throw new IllegalStateException("Already instantiated");
        }
        instance = this; // NOSONAR
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

    public synchronized void loadFromFile(File file) {
        try (FileInputStream is = new FileInputStream(file)) {
            JSONObject js = new JSONObject(new String(is.readAllBytes()));
            List<Vec2> newVectors = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                JSONObject star = js.getJSONObject("Star" + i);
                JSONArray pos = star.getJSONArray("pos");
                JSONArray vel = star.getJSONArray("vel");
                newVectors.add(new Vec2(pos.getDouble(0), pos.getDouble(1)));
                newVectors.add(new Vec2(vel.getDouble(0), vel.getDouble(1)));
            }
            vectors.clear();
            vectors.addAll(newVectors);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public synchronized void saveToFile(File file) {
        try (FileOutputStream is = new FileOutputStream(file)) {
            JSONObject js = new JSONObject();
            for (int i = 0; i < 3; i++) {
                JSONObject star = new JSONObject();
                var pos = new JSONArray(new double[] { getPos(i).x, getPos(i).y });
                var vel = new JSONArray(new double[] { getVel(i).x, getVel(i).y });
                star.put("pos", pos);
                star.put("vel", vel);
                js.put("Star" + i, star);
            }
            is.write(js.toString().getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Getters and setters
    // -------------------------------------------------------------------------------

    public static SimParameters getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Not instantiated");
        }
        return instance;
    }

    public synchronized double getMass() {
        return mass;
    }

    public synchronized boolean isRunning() {
        return simRunning;
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
}
