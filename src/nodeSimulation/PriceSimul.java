package nodeSimulation;

/**
 * Created by sethlee on 8/9/17.
 */
public class PriceSimul {
    private double mean;   // mean of a gaussian dist.
    private double dev;
    private MaxCentral maxCentral;

    public PriceSimul(double mean, double dev) {
        this.mean = mean;
        this.dev = dev;
        maxCentral = new MaxCentral(mean, dev);
    }

    /**
     * get estimated time for synchronous algorithm
     * @param i
     * @return Time
     */
    public double getSyncTime(int i, int n) {
        return (double)i * maxCentral.getApproxMaxCentral(n);
    }
}
