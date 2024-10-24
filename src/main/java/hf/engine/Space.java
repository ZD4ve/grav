package hf.engine;

public class Space {
    public static final double G = 6.67430e-11; //N⋅m^2⋅kg−2
    
    Star aStar;
    Star bStar;
    Star cStar;
    SimParameters simParams;
    
    public Space(SimParameters simulationParameters) {
        simParams = simulationParameters;
        aStar = new Star(simParams, 0, 1);
        bStar = new Star(simParams, 2, 3);
        cStar = new Star(simParams, 4, 5);

        while (true) {
            try {
                synchronized (simParams) {
                    simParams.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            tick(1*60*60,100);

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
