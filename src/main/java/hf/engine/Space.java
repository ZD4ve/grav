package hf.engine;

public class Space {
    Star aStar;
    Star bStar;
    Star cStar;
    
    public Space(SimParameters simParams) {
        aStar = new Star(simParams, 0, 1);
        bStar = new Star(simParams, 2, 3);
        cStar = new Star(simParams, 4, 5);
    }

}
