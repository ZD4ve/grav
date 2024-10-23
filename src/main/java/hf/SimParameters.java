package hf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SimParameters implements Serializable {
    private final List<Vec2> vectors = Collections.synchronizedList(new ArrayList<>());
    public synchronized Vec2 getVector(int index) {
        return vectors.get(index);
    }
    public SimParameters(){
        vectors.add(new Vec2(5,13));
        vectors.add(new Vec2(8,-63));
        vectors.add(new Vec2(-9,9));
        vectors.add(new Vec2(-53,-79));
        vectors.add(new Vec2(5,7));
        vectors.add(new Vec2(0,1));
    }
}
