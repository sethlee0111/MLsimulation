package nodeSimulation;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.integration.SimpsonIntegrator;
import org.apache.commons.math3.distribution.CauchyDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;

import static java.lang.Math.exp;
import static java.lang.Math.sqrt;

/**
 * Created by sethlee on 8/9/17.
 */
public class RatioDist implements UnivariateFunction{
    private double m;   // mean mu
    private double r;   // deviation
    //constants used for integrating pdf.
    final private double INFINITY = 100; //  -INFINITY is sufficient to get meaningful val.
    final private double SLICE_WIDTH = 0.1;   // width of a rectangle when integrating

    public RatioDist(double m, double r) {
        this.m = m;
        this.r = r;
    }

    public void setR(double r) {
        this.r = r;
    }

    /**
     * @param z
     * @return  value of the pdf for the ratio distribution with two N(0, 1)
     * this function is verified by cauchy distribution, since of their ratio distribution of
     * Gaussian distribution with zero mean is the cauchy distribution
     * also verified by getting P(z < 1) = 0.5
     */
    public double value(double z) {
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
        SimpsonIntegrator simpsonIntegrator = new SimpsonIntegrator();
        return simpsonIntegrator.integrate(10000000, ratioDist, -INFINITY, x);
    }

    /**
     * obtain P( x < X/Y )
     * @param x
     * @return probability
     */
    public double probGreaterThan(double x) {
        RatioDist ratioDist = new RatioDist(m, r);
        SimpsonIntegrator simpsonIntegrator = new SimpsonIntegrator();
        return simpsonIntegrator.integrate(10000000, ratioDist, x, INFINITY);
    }

    /**
     * function a(z)
     * @param z
     */
    public double a(double z) {
        double z_2 = Math.pow(z, 2);
        return sqrt(z_2 + 1) * (1 / r);
    }

    /**
     * function b(z)
     * @param z
     */
    public double b(double z) {
        return (m / (r * r)) * (z + 1);
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
        RatioDist ratioDist = new RatioDist(0, 1);
        CauchyDistribution cauchyDistribution = new CauchyDistribution();
        double m;
        for(double s=1; s < 20; s++) {
            m = 1/s;
//            System.out.println("m : " + m);
            System.out.println("Rresult : " + ratioDist.probLowerThan(m));
//            System.out.println("Cresult : " + cauchyDistribution.cumulativeProbability(m));
//            System.out.println("================================================");

        }
//        double diff = ratioDist.probLowerThan(0.142) - ratioDist.probLowerThan(0.115);
//        System.out.println("diff: " + diff);
////        System.out.printf("" + ratioDist.run(1));
//        CauchyDistribution cauchyDistribution = new CauchyDistribution();
//        System.out.println(": " + (cauchyDistribution.cumulativeProbability(0.142) - cauchyDistribution.cumulativeProbability(0.125)));
    }
}
