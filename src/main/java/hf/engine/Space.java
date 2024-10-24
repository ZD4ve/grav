package hf.engine;

public class Space {
    public static final double G = 6.67430e-11; //N⋅m^2⋅kg−2
    
    Star aStar;
    Star bStar;
    Star cStar;
    
    public Space(SimParameters simParams) {
        aStar = new Star(simParams, 0, 1);
        bStar = new Star(simParams, 2, 3);
        cStar = new Star(simParams, 4, 5);
    }

}
