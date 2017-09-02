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

    public void setTotalIter(int totalIter) {
        this.totalIter = totalIter;
    }

    public void setNodeNum(int nodeNum) {
        this.nodeNum = nodeNum;
    }

    /**

     * get probability of getting into staleness bound
     * @param iter
     * @param staleness
     * @return
     */
    public double getProb(int iter, int staleness) {
        RatioDist ratioDist = new RatioDist(mean, dev);
        if(staleness >= iter)
            return 0;

        double frac1 = ((double)staleness / (iter - 1));
        double frac2 = ((double)staleness / iter);
        double term = ratioDist.probGreaterThan(1 - frac1);
        double res = ratioDist.probLowerThan(1 - frac2) - ratioDist.probLowerThan(1 - frac1);

//        return res / term;
        return ratioDist.probLowerThan(1 - frac2);
    }

    /**
     * obtain estimated execution time for (iter)th iteration when staleness is given.
     *
     * @param iter
     * @param staleness
     * @return
     */
    public double getTime(int iter, int staleness) {
        double res;
//        System.out.println("gettime");
        // get MaxCentral ready : to compute maximum and minimum
        MaxCentral maxCentral = new MaxCentral(mean, dev);
        double prob = getProb(iter, staleness);
        prob *= nodeNum;
        // if probability exceeded 1,
        if(prob > 1)
            prob = 1;
        res = prob * maxCentral.getApproxMaxCentral(nodeNum) + (1 - prob) * mean;
//        res = (1 - prob) * maxCentral.getApproxMaxCentral(nodeNum) + (prob) * maxCentral.getApproxMinCentral(nodeNum);

        return res;
    }

    /**
     * obtain total time
     */
    public double getTotalTime(int staleness) {
        double totalTime = 0;
        for(int i = 1; i <= totalIter; i++) {
            totalTime += getTime(i, staleness);
            System.out.printf("%.2f p\n", i/(double)totalIter * 100);
        }
        System.out.println("");
        return totalTime;
    }

    public static void main(String[] args) {
        MLsettings mLsettings = new MLsettings();
        mLsettings.setMeanTime(50);
        mLsettings.setDevTime(10);
        mLsettings.setIterNum(30);
        mLsettings.setStaleness(0);
        StaleBoundProbability staleBoundProbability = new StaleBoundProbability(mLsettings);
        for(int i = 0; i < 10; i++) {
            System.out.println("" + staleBoundProbability.getProb(15, i));
        }
//
//        PriceSimul priceSimul = new PriceSimul(mLsettings.getMeanTime(), mLsettings.getDevTime());
//        System.out.println(priceSimul.getSyncTime(mLsettings.getIterNum(), mLsettings.getNodeNum()) + "");
//        System.out.printf("" + (1 - Math.pow(1 - prob, 200)));
    }
}
