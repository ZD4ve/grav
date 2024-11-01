package hf.engine;

public class Star {
    SimParameters simParams;
    int index;
    Vec2 position;
    Vec2 velocity;

    public Star(int starIndex) {
        simParams = SimParameters.getInstance();
        index = starIndex;
        readParams();
    }

    /**
     * Calculates the acceleration on this star due to the gravitational forces
     * exerted by two other stars.
     * 
     * @implNote https://en.wikipedia.org/wiki/Three-body_problem
     * @param a First star
     * @param b Second star
     * @return Acceleration vector in m/s^2
     */
    public Vec2 calculateAccelaration(Star a, Star b) {
        Vec2 accA = this.position.sub(a.position)
                .scale(-Space.G * simParams.getMass() / cube(this.position.sub(a.position).amplitude()));
        Vec2 accB = this.position.sub(b.position)
                .scale(-Space.G * simParams.getMass() / cube(this.position.sub(b.position).amplitude()));
        return accA.add(accB);
    }

    public void tick(Vec2 acceleration, double dt) {
        velocity = this.velocity.add(acceleration.scale(dt));
        position = this.position.add(velocity.scale(dt));
    }

    public void readParams() {
        position = simParams.getPos(index);
        velocity = simParams.getVel(index);
    }

    public void writeParams() {
        simParams.setPos(index, position);
        simParams.setVel(index, velocity);
    }

    private static double cube(double x) {
        return x * x * x;
    }
}
