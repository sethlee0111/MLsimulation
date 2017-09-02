package nodeSimulation;

/**
 * Created by sethlee on 8/12/17.
 */
public class SethIntegration {
    public static double integrate(Integratable integratable, double lower, double upper, double pieceWidth) {
        if(pieceWidth > upper - lower)
            return 0;

        double res = 0;
        for(double i = lower; i <= upper; i += pieceWidth) {
            res += pieceWidth * integratable.run(i + pieceWidth / 2);   // the area of one rectangle
        }
        return res;
    }
}
