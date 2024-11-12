package hf.engine;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestStar {
    Star a;
    Star b;
    Star c;

    @BeforeEach
    void initParams() {
        SimParameters.resetInstance();
        new SimParameters();
        a = new Star(0);
        b = new Star(1);
        c = new Star(2);
    }

    @Test
    void testCalculateAccelaration() {
        // Arrange
        a.position = new Vec2(-1, 1);
        b.position = new Vec2(1, 1);
        c.position = new Vec2(1, -1);

        // Act
        Vec2 acceleration = b.calculateAccelaration(a, c);

        // Assert
        assertEquals(new Vec2(-1, -1).normalise(), acceleration.normalise());
    }
}
