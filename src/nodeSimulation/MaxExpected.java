package nodeSimulation;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * reads calculated maximum expected values of N(0, 1) from n number of variables ( exp.txt )
 *
 * @author sethlee
 *
 */

public class MaxExpected {

    ArrayList<Double> expectedArrayList = new ArrayList<>();

    // constructor reads expected values from the file
    public MaxExpected() throws FileNotFoundException{

        // create a file instance
        java.io.File file = new java.io.File("exp.txt");

        // create a scanner for the file
        Scanner input = new Scanner(file);      // may throw a FileNotFoundException
        while(input.hasNext()) {
            expectedArrayList.add(input.nextDouble());
        }
    }

    /**
     * get expected MAXIMUM value for N(mean, dev^2)
     * @param mean
     * @param dev
     * @param num
     * @return expected maximum value
     */
    public double getExpected(double mean, double dev, int num) {
        if(num < expectedArrayList.size())
            return mean + dev * expectedArrayList.get(num);
        else
            return 0;
    }

    public static void main(String[] args) throws Exception{
        MaxExpected maxExpected = new MaxExpected();

        System.out.println("" + maxExpected.getExpected(50, 20, 50));
    }
}
