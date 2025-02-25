package hf.engine;

/**
 * Simulator of a 3 body system
 */
public class Space {
    public static final double G = 6.67430e-11; // N⋅m^2⋅kg−2
    private SimParameters simParams;

    private Star aStar;
    private Star bStar;
    private Star cStar;

    /**
     * Inizialize the space with 3 stars
     */
    public Space() {
        simParams = SimParameters.getInstance();
        aStar = new Star(0);
        bStar = new Star(1);
        cStar = new Star(2);
    }

    /**
     * Runable method for parallel simulation
     */
    public void simulate() {
        while (true) {
            try {
                synchronized (simParams) {
                    simParams.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
            long prevTimeStamp = System.nanoTime();
            int divisons = 1;
            while (simParams.isRunning()) {
                long now = System.nanoTime();
                long dt = now - prevTimeStamp;
                tick(dt / 1e9, divisons);
                synchronized (simParams) {
                    simParams.notifyAll();
                }
                if (dt < 1e9 / 240) {
                    divisons *= 2;
                }
                if (dt > 1e9 / 60) {
                    divisons /= 2;
                }
                prevTimeStamp = now;
            }
        }
    }

    /**
     * Simulate the movement of the stars
     * 
     * @param dt       Time step in seconds
     * @param divisons number of divisions
     */
    private void tick(double dt, int divisons) {
        aStar.readParams();
        bStar.readParams();
        cStar.readParams();
        double dtDiv = dt / divisons;
        for (int i = 0; i < divisons; i++) {
            Vec2 accA = aStar.calculateAccelaration(bStar, cStar);
            Vec2 accB = bStar.calculateAccelaration(aStar, cStar);
            Vec2 accC = cStar.calculateAccelaration(aStar, bStar);
            aStar.tick(accA, dtDiv);
            bStar.tick(accB, dtDiv);
            cStar.tick(accC, dtDiv);
        }
        aStar.writeParams();
        bStar.writeParams();
        cStar.writeParams();
    }
}
