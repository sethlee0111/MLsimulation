package nodeSimulation;

/**
 * Created by sethlee on 8/12/17.
 */
public class StaleBoundProbability {
    private double mean;
    private double dev;
    private int nodeNum;
    private int totalIter;

    public StaleBoundProbability(MLsettings mLsettings) {
        this.mean = mLsettings.getMeanTime();
        this.dev = mLsettings.getDevTime();
        this.nodeNum = mLsettings.getNodeNum();
        this.totalIter = mLsettings.getIterNum();
    }
    /**
     * get probability of getting into staleness bound
     * @param staleness
     * @return
     */
//    public double getProb(int iter, int staleness) {
//        RatioDist ratioDist = new RatioDist(mean, dev);
//        if(staleness >= iter)
//            return 0;
//
//        double frac1 = ((double)staleness / (iter - 1));
//        double frac2 = ((double)staleness / iter);
//        double term = ratioDist.probGreaterThan(1 - frac1);
//        double res = ratioDist.probLowerThan(1 - frac2) - ratioDist.probLowerThan(1 - frac1);
//
////        return res / term;
//        return ratioDist.probLowerThan(1 - frac2);
//    }
    // sync_ver_1
    public double getProb(int staleness) {
        double res;
        RatioDist ratioDist = new RatioDist(mean, dev);
        if(staleness < 0)
            return 1;
        else
            res = ratioDist.probLowerThan(1/((double)staleness+1));

        return res;
    }

    /**
     * obtain estimated execution time for (iter)th iteration when staleness is given.
     *
     * @param staleness
     * @return
     */
    public double getTime(int staleness) throws Exception{
        double res;
//        System.out.println("gettime");
        // get MaxCentral ready : to compute maximum and minimum
        MaxCentral maxCentral = new MaxCentral(0, dev);   // this is about the difference between two nodes,,so..

        MaxExpected maxExpected = new MaxExpected();
        double prob = getProb(staleness);
        prob *= nodeNum;
        // if probability exceeded 1,
        if(prob > 1)
            prob = 1;
        res = prob * (maxExpected.getExpected(0, dev, nodeNum) + mean) + (1 - prob) * mean;
//        System.out.println("Prob : " + prob);
//        res = prob * (maxCentral.getApproxMaxCentral(nodeNum) + mean) + (1 - prob) * mean;

//        res = (1 - prob) * maxCentral.getApproxMaxCentral(nodeNum) + (prob) * maxCentral.getApproxMinCentral(nodeNum);

        return res;
    }

    /**
     * obtain total time
     */
    public double getTotalTime(int staleness) {
        double totalTime = 0;
        try {
            totalTime = getTime(staleness) * (totalIter - (staleness + 1)) + mean * (staleness + 1);
//            totalTime = getTime(staleness) * totalIter;
            return totalTime;
        } catch (Exception e) {
            System.out.println("File Not Found Exception");
            return 0;
        }
    }

    public static void main(String[] args) {
        MLsettings mLsettings = new MLsettings();
        mLsettings.setMeanTime(50);
        mLsettings.setDevTime(18);
        mLsettings.setIterNum(30);
        mLsettings.setStaleness(1);
        mLsettings.setNodeNum(50);
        StaleBoundProbability staleBoundProbability = new StaleBoundProbability(mLsettings);
        for(int i = 0; i < 10; i++) {
            System.out.println("" + staleBoundProbability.getProb(i));
        }
//
//        PriceSimul priceSimul = new PriceSimul(mLsettings.getMeanTime(), mLsettings.getDevTime());
//        System.out.println(priceSimul.getSyncTime(mLsettings.getIterNum(), mLsettings.getNodeNum()) + "");
//        System.out.printf("" + (1 - Math.pow(1 - prob, 200)));
    }
}
