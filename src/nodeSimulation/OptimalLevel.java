package nodeSimulation;

import java.util.ArrayList;
import java.util.Collections;

/**
 * class for calculating optimal level of staleness from the given mean and dev
 * mind the calibration stage in processModel()
 */
public class OptimalLevel {

    private double delta;    // slope of the change in the entire cost
    private final int MAX_STALENESS = 40 + 2;   // maximum staleness level that will be allocated + 2
    private double diff;

    private StaleBoundProbability staleBoundProbability;
    private ArrayList<Integer> allocHistory = new ArrayList<>();
    private ArrayList<Double> stalenessList = new ArrayList<>();
    private ArrayList<Double> gradientList = new ArrayList<>();

    private void processModel(MLsettings mLsettings) {
        // calibration of deviation
//        mLsettings.setDevTime(mLsettings.getDevTime() - 10);
        this.staleBoundProbability = new StaleBoundProbability(mLsettings);

        stalenessList.clear();
        gradientList.clear();

        for(int s = 0; s < MAX_STALENESS; s++) {
            stalenessList.add(staleBoundProbability.getTotalTime(s));
        }
        for(int s = 1; s < MAX_STALENESS; s++) {
            gradientList.add(stalenessList.get(s-1) - stalenessList.get(s));
        }

        diff = 0;    // calculate difference to determine linearity
        for(int s = 0; s < MAX_STALENESS - 2; s++) {
            diff += gradientList.get(s) - gradientList.get(s + 1);
        }

        // delta = (stalenessList.get(0) - staleBoundProbability.getTotalTime(mLsettings.getIterNum()))/mLsettings.getIterNum();
        delta = (stalenessList.get(0) - stalenessList.get(MAX_STALENESS - 1))/mLsettings.getIterNum();
    }

    public ArrayList<Double> getStalenessList() {
        return stalenessList;
    }

    public ArrayList<Double> getGradientList() {
        return gradientList;
    }

    public int getOptimalLevel(MLsettings mLsettings) {
        processModel(mLsettings);
        int s;
        if(diff < 3) {  // function linear
            s = -1;
            System.out.println("------------> not allocated");
        }
        else {
            if(gradientList.get(0) > gradientList.get(1) + 1)
                s = 1;
            else {
                for(s=0; s<MAX_STALENESS-1; s++) {
                    if(gradientList.get(s) < gradientList.get(0) - 1)
                        break;
                }
            }
            allocHistory.add(s);
            System.out.println("------------> allocated level: " + s);
        }
        return s;
    }

    public double getOptimalHistoryMean() {
        Integer sum = 0;
        if(!allocHistory.isEmpty()) {
            for(Integer tmp : allocHistory) {
                sum += tmp;
            }
            return sum.doubleValue() / allocHistory.size();
        }
        return -1;
    }

    public static void main2(String[] args) {
        MLsettings mLsettings = new MLsettings();
        mLsettings.setMeanTime(100);
        mLsettings.setIterNum(30);
        mLsettings.setNodeNum(450);

        OptimalLevel optimalLevel;
//        ArrayList list = optimalLevel.getStalenessList();
//        System.out.println("" + list);

        for(int dev = 22; dev <=50; dev += 4) {
            mLsettings.setDevTime(dev);
            optimalLevel = new OptimalLevel();
            System.out.println("The optimal level is: " + optimalLevel.getOptimalLevel(mLsettings) + " when dev: " + dev);
            ArrayList list = optimalLevel.getStalenessList();
            System.out.println("" + list);
            ArrayList list2 = optimalLevel.getGradientList();
            System.out.println("" + list2);
        }
    }

    public static void main(String[] args) {
        test();
    }

    public static void test() {
        MLsettings mLsettings = new MLsettings();
        mLsettings.setMeanTime(99);
        mLsettings.setDevTime(35);
        mLsettings.setIterNum(150);
        mLsettings.setNodeNum(100);
        OptimalLevel optimalLevel = new OptimalLevel();

        System.out.println("The optimal level is: " + optimalLevel.getOptimalLevel(mLsettings));
        ArrayList list = optimalLevel.getStalenessList();
        System.out.println("" + list);
        ArrayList list2 = optimalLevel.getGradientList();
        System.out.println("" + list2);
    }
}
