package hf.engine;

/**
 * Star in the simulation
 */
public class Star {
    private SimParameters simParams;
    private final int index;
    Vec2 position;
    Vec2 velocity;

    /**
     * Constructor
     * 
     * @param starIndex Index of the star in SimParameters
     */
    public Star(int starIndex) {
        simParams = SimParameters.getInstance();
        index = starIndex;
        readParams();
    }

    /**
     * Calculates the acceleration on this star due to the gravitational forces
     * exerted by two other stars using cached information
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

    /**
     * Updates the position and velocity
     * 
     * @param acceleration
     * @param dt
     */
    public void tick(Vec2 acceleration, double dt) {
        velocity = this.velocity.add(acceleration.scale(dt));
        position = this.position.add(velocity.scale(dt));
    }

    /**
     * Cache the pos and vel from SimParameters
     */
    public void readParams() {
        position = simParams.getPos(index);
        velocity = simParams.getVel(index);
    }

    /**
     * Writeback the pos and vel to SimParameters
     */
    public void writeParams() {
        simParams.setPos(index, position);
        simParams.setVel(index, velocity);
    }

    /**
     * Calculate the cube of a number
     */
    private static double cube(double x) {
        return x * x * x;
    }
}
