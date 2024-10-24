package hf.engine;

public class Star {
    SimParameters simParams;
    int posI;
    int velI;
    Vec2 position;
    Vec2 velocity;

    public Star(SimParameters simulationParameters, int positionIndex, int velocityIndex) {
        simParams = simulationParameters;
        posI = positionIndex;
        velI = velocityIndex;
        position = simParams.getVector(posI);
        velocity = simParams.getVector(velI);
    }
}
