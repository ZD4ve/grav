package hf.engine;

public class Space {
    public static final double G = 6.67430e-11; // N⋅m^2⋅kg−2
    SimParameters simParams;

    Star aStar;
    Star bStar;
    Star cStar;

    public Space(SimParameters simulationParameters) {
        simParams = simulationParameters;
        aStar = new Star(simParams, 0);
        bStar = new Star(simParams, 1);
        cStar = new Star(simParams, 2);
    }

    public void simulate() {
        while (true) {
            try {
                synchronized (simParams) {
                    simParams.wait();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
            long prevTimeStamp = System.currentTimeMillis();
            while (simParams.isRunning()) {
                long now = System.currentTimeMillis();
                tick((now - prevTimeStamp) / 1000.0, 10000);
                prevTimeStamp = now;
                synchronized (simParams) {
                    simParams.notifyAll();
                }
            }
        }
    }

    public void tick(double dt, int divisons) {
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
