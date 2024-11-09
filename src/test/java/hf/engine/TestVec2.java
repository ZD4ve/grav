package hf.engine;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class TestVec2 {

    @Test
    void testFromPolar() {
        Vec2 vec = Vec2.fromPolar(45, Math.sqrt(2));
        assertEquals(1, vec.x, 1e-9);
        assertEquals(1, vec.y, 1e-9);
    }

    @Test
    void testAdd() {
        Vec2 vec1 = new Vec2(1, 2);
        Vec2 vec2 = new Vec2(3, 4);
        Vec2 result = vec1.add(vec2);
        assertEquals(new Vec2(4, 6), result);
    }

    @Test
    void testScale() {
        Vec2 vec = new Vec2(1, 2);
        Vec2 result = vec.scale(2);
        assertEquals(new Vec2(2, 4), result);
    }

    @Test
    void testNormalise() {
        Vec2 vec = new Vec2(3, 4);
        Vec2 result = vec.normalise();
        assertEquals(new Vec2(0.6, 0.8), result);
    }

    @Test
    void testAmplitude() {
        Vec2 vec = new Vec2(3, 4);
        assertEquals(5, vec.amplitude(), 1e-9);
    }

    @Test
    void testAngle() {
        Vec2 vec = new Vec2(-1, 1);
        assertEquals(135, vec.angle(), 1e-9);
    }
}