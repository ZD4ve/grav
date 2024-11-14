package hf.engine;

import java.io.*;
import java.util.*;
import org.json.*;

/**
 * Thread safe Singleton, communicates with the simulation and the GUI
 */
public class SimParameters {
    public static final String FILE_EXTENSION = "grav";

    /**
     * Singleton instance
     */
    private static SimParameters instance;

    final List<Vec2> vectors = Collections.synchronizedList(new ArrayList<>());
    private double mass = 1e16;// kg 1.989e30s
    private List<Vec2> stateBeforeStart;
    private boolean simRunning = false;

    /**
     * Initialize the singleton instance.
     * Should be called only once.
     */
    public SimParameters() {
        if (instance != null) {
            throw new IllegalStateException("Singleton class already instantiated");
        }
        instance = this; // NOSONAR
        vectors.add(Vec2.fromPolar(0, 100));
        vectors.add(Vec2.fromPolar(90, 50));
        vectors.add(Vec2.fromPolar(120, 100));
        vectors.add(Vec2.fromPolar(210, 50));
        vectors.add(Vec2.fromPolar(240, 100));
        vectors.add(Vec2.fromPolar(330, 50));
    }

    /**
     * Recenter the stars to the center of mass
     * Assumes the mass of all stars is the same
     */
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

    /**
     * Start the simulation
     * Notify all waiting threads
     */
    public synchronized void startSim() {
        simRunning = true;
        stateBeforeStart = new ArrayList<>(vectors);
        notifyAll();
    }

    /**
     * Stop the simulation
     */
    public synchronized void stopSim() {
        simRunning = false;
    }

    /**
     * Reset the simulation to the state before start
     */
    public synchronized void resetSim() {
        stopSim();
        try {
            this.wait(250);// NOSONAR
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
        vectors.clear();
        vectors.addAll(stateBeforeStart);
    }

    /**
     * Load the simulation parameters from a file into the singleton instance
     * 
     * @param file File to load from
     * @throws IOException   if file not found
     * @throws JSONException if file is not a valid configuration
     */
    public synchronized void loadFromFile(File file) throws IOException, JSONException {
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
            throw e;
        }
    }

    /**
     * Save the simulation parameters to a file
     * 
     * @param file File to save to
     */
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Getters and setters
    // -------------------------------------------------------------------------------

    /**
     * Get the singleton instance
     * 
     * @return Singleton instance
     * @throws IllegalStateException if the instance is not initialized
     */
    public static SimParameters getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Singleton class not instantiated");
        }
        return instance;
    }

    /**
     * ONLY FOR TESTING PURPOSES
     */
    static void resetInstance() {
        instance = null;
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
