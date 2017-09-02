package nodeSimulation;

import java.util.Set;

/**
 * integratable function y=x
 * used for testing Integratable interface & Integration
 * Created by sethlee on 8/12/17.
 */
public class TestFunction implements Integratable{
    public double run(double x) {
        return x;
    }

    public static void main(String[] args) {
        TestFunction testFunction = new TestFunction();
        double res = SethIntegration.integrate(testFunction, 0, 10, 0.0001);
        System.out.printf("result : " + res);
    }
}
