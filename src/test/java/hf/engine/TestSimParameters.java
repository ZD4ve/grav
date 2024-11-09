package hf.engine;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.json.JSONException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

class TestSimParameters {

    private SimParameters simParams;

    @BeforeEach
    void setUp() {
        SimParameters.resetInstance();
        simParams = new SimParameters();
    }

    @Test
    void Singleton() {
        assertThrows(IllegalStateException.class, SimParameters::new);
    }

    @Test
    void testRecenter() {
        // Arrange
        simParams.setPos(0, new Vec2(1, 1));
        simParams.setPos(1, new Vec2(2, 2));
        simParams.setPos(2, new Vec2(3, 3));

        // Act
        simParams.recenter();

        // Assert
        assertEquals(new Vec2(-1, -1), simParams.getPos(0));
        assertEquals(new Vec2(0, 0), simParams.getPos(1));
        assertEquals(new Vec2(1, 1), simParams.getPos(2));
    }

    @Test
    void testSaveToFile(@TempDir Path tempDir) throws IOException {
        // Arrange
        simParams.setPos(0, new Vec2(1, 2));
        simParams.setVel(0, new Vec2(3, 4));

        File file = tempDir.resolve("saveTest.grav").toFile();

        // Act
        simParams.saveToFile(file);

        // Assert
        String content = java.nio.file.Files.readString(file.toPath());
        assertTrue(content.contains("\"pos\":[1,2]"));
        assertTrue(content.contains("\"vel\":[3,4]"));
    }

    @Test
    void testLoadFromFile(@TempDir Path tempDir) throws IOException, JSONException {
        // Arrange
        File file = tempDir.resolve("loadTest.grav").toFile();
        String jsonContent = """
                {
                "Star0": {"pos": [1.0, 2.0], "vel": [3.0, 4.0]},
                "Star1": {"pos": [5.0, 6.0], "vel": [7.0, 8.0]},
                "Star2": {"pos": [9.0, 10.0], "vel": [11.0, 12.0]}
                }
                """;
        java.nio.file.Files.writeString(file.toPath(), jsonContent);

        // Act
        simParams.loadFromFile(file);

        // Assert
        assertEquals(new Vec2(1.0, 2.0), simParams.getPos(0));
        assertEquals(new Vec2(3.0, 4.0), simParams.getVel(0));
        assertEquals(new Vec2(5.0, 6.0), simParams.getPos(1));
        assertEquals(new Vec2(7.0, 8.0), simParams.getVel(1));
        assertEquals(new Vec2(9.0, 10.0), simParams.getPos(2));
        assertEquals(new Vec2(11.0, 12.0), simParams.getVel(2));
    }

}