package nodeSimulation;

import org.apache.commons.math3.distribution.NormalDistribution;

import static java.lang.Math.exp;
import static java.lang.Math.sqrt;

/**
 * Created by sethlee on 8/9/17.
 */
public class RatioDist implements Integratable{
    private double m;   // mean mu
    private double r;   // deviation
    //constants used for integrating pdf.
    final private double INFINITY = 70; //  -INFINITY is sufficient to get meaningful val.
    final private double SLICE_WIDTH = 0.01;   // width of a rectangle when integrating

    public RatioDist(double m, double r) {
        this.m = m;
        this.r = r;
    }

    /**
     * @param z
     * @return  value of the pdf for the ratio distribution with two N(0, 1)
     * this function is verified by cauchy distribution, since of their ratio distribution of
     * Gaussian distribution with zero mean is the cauchy distribution
     * also verified by getting P(z < 1) = 0.5
     */
    public double run(double z) {
        double res;

        NormalDistribution normalDistribution = new NormalDistribution();

        res = b(z) * d(z) / Math.pow(a(z), 3);  // 1st term
        res *= 1 / (sqrt(2 * Math.PI) * Math.pow(r, 2));    // 2nd term
        res *= normalDistribution.cumulativeProbability(b(z)/a(z))
                - normalDistribution.cumulativeProbability(-b(z)/a(z)); // 3rd term
        res += (1 / (Math.pow(a(z), 2) * Math.PI * Math.pow(r, 2))) * exp(-(c()/2));

        return res;
    }

    /**
     * obtain P( X/Y < x )
     * @param x
     * @return probability
     */
    public double probLowerThan(double x) {
        RatioDist ratioDist = new RatioDist(m, r);
        return SethIntegration.integrate(ratioDist, -INFINITY, x, SLICE_WIDTH);
    }

    /**
     * obtain P( x < X/Y )
     * @param x
     * @return probability
     */
    public double probGreaterThan(double x) {
        RatioDist ratioDist = new RatioDist(m, r);
        return SethIntegration.integrate(ratioDist, x, INFINITY, SLICE_WIDTH);
    }

    /**
     * function a(z)
     * @param z
     */
    public double a(double z) {
        double z_2 = Math.pow(z, 2);
        return sqrt((z_2 + 1) * (1 / (r * r)));
    }

    /**
     * function b(z)
     * @param z
     */
    public double b(double z) {
        return m / (r * r) * (z + 1);
    }

    /**
     * function c(z)
     */
    public double c() {
        return 2 * (m * m) / (r * r) ;
    }

    /**
     * function d(z)
     * @param z
     */
    public double d(double z) {
        double tmp = Math.pow(b(z), 2) - c() * Math.pow(a(z), 2);
        tmp /= 2 * Math.pow(a(z), 2);
        return exp(tmp);
    }


    public static void main(String[] args) {
        RatioDist ratioDist = new RatioDist(10, 1);
        System.out.printf("result : " + ratioDist.probLowerThan(0.5));
//        System.out.printf("" + ratioDist.run(1));
    }
}
