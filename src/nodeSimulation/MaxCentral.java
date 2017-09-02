package nodeSimulation;


import org.apache.commons.math3.distribution.NormalDistribution;

import static java.lang.Math.log;
import static java.lang.Math.sqrt;

/**
 * This is a class for getting an expected value of a maximum of a gaussian random variable.
 * Created by sethlee on 8/9/17.
 */
public class MaxCentral {
    // defining Euler-Mascheroni constant(gamma)
    final double EULER_MASCHERONI = 0.57721566490153286060651209008240243104215933593992;

    private double mean;   // mean of a gaussian dist.
    private double dev;
    private NormalDistribution normalDistribution;

    /**
     * generate N(0, 1)
     */
    public MaxCentral(double mean, double dev) {
        this.mean = mean;
        this.dev = dev;
        normalDistribution = new NormalDistribution(0, 1);
    }

    /**
     * result of inverse cumulative function
     * of a N(0, 1) gaussian distribution
     * @return
     */
    private double invCumulative(double input) {
        return normalDistribution.inverseCumulativeProbability(input);
    }

    public static void main(String[] args) {
        MaxCentral maxCentral = new MaxCentral(10,1);
        for(int i = 100; i < 2000; i += 100) {
            System.out.printf(i + " : " + maxCentral.getApproxMaxCentral(i) + "\n");
        }
//        MaxCentral maxCentral = new MaxCentral(3, 1);
//        System.out.printf("" + maxCentral.invCumulative(0.3));
    }

    /**
     * get the expected value of maximum X
     * when X is a gaussian random variable from N(0, 1)
     * @param n number of nodes
     * @return  expected value of max(X)
     * @TODO This appears to be negative. Fix it.
     */
    public double getMaxCentral(double n) {
        double result;
        result = sqrt(2)*((EULER_MASCHERONI - 1) * invCumulative(1-(1/n))
                - EULER_MASCHERONI * invCumulative(1 - (1/(n*Math.E))) );
        return result;
    }

    /**
     * get the approximated value of mean of maximum X
     * when X is a gaussian random variable from N(0, 1)
     * @param n number of nodes
     * @return expected value of max(X)
     */
    public double getApproxMaxCentral(double n) {
        double result;
        result = mean + dev * sqrt(2*log(n));

        return result;
    }

    public double getApproxMinCentral(double n) {
        double result;
        result = mean - dev * sqrt(2*log(n));

        return result;
    }

}
