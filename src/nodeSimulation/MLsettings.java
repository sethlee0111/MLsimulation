package nodeSimulation;

import java.util.ArrayList;

/**
 * Has constants for ML simulation
 */
public class MLsettings {
    public static final int LOCATION_MAX_X = 100;
    public static final int LOCATION_MAX_Y = 100;

    public static int LEARNING_MEAN_TIME = 100; // mean time for ML for overall nodes. follows Gaussian when applied
    public static int NUMBER_OF_NODES = 50;
    public static int TIME_DEVIATION_BY_NODES = 20;
    public static int TOTAL_ITERATION = 50;
    public static int MINIMUM_ITERATION_TIME = 5;

    /* constants for simple SSP Simulation */
    public static int OVERALL_STALENESS_BOUND = 3;

    private int meanTime;
    private int nodeNum;
    private int devTime;
    private int iterNum;
    private int minIter;
    private int staleness;

    private int sampleWindow;   // the number of recent iterations which will be sampled to obtain optimal staleness
    private ArrayList<Integer> gaussUpdateFreq = new ArrayList<>();

    private String xMode;    // x Axis of a graph
    private int minMode;
    private int maxMode;

    private ArrayList<Integer> dynamicList = new ArrayList<>();

    //of different series
    private int seriesNum;  // number of different Graphs

    public int getSampleWindow() {
        return sampleWindow;
    }

    public void setSampleWindow(int sampleWindow) {
        this.sampleWindow = sampleWindow;
    }

    public ArrayList<Integer> getGaussUpdateFreq() {
        return gaussUpdateFreq;
    }

    public void setGaussUpdateFreq(ArrayList<Integer> gaussUpdateFreq) {
        this.gaussUpdateFreq = gaussUpdateFreq;
    }

    private String seriesMode;
    private ArrayList<Integer> seriesArrayList;
//    private int seriesSet = 0;

    public ArrayList<Integer> getDynamicList() {
        return dynamicList;
    }

    public void setDynamicList(ArrayList<Integer> dynamicList) {
        this.dynamicList = dynamicList;
    }

    public MLsettings() {
        // Initial Settings
        meanTime = LEARNING_MEAN_TIME;
        nodeNum = NUMBER_OF_NODES;
        devTime = TIME_DEVIATION_BY_NODES;
        iterNum = TOTAL_ITERATION;
        minIter = MINIMUM_ITERATION_TIME;
        staleness = OVERALL_STALENESS_BOUND;

        xMode = "Staleness";
        minMode = 0;
        maxMode = 10;

        seriesMode = "Dev T";
        seriesNum = 1;
        seriesArrayList = new ArrayList<>();
        seriesArrayList.add(TIME_DEVIATION_BY_NODES);
        for(int i=1;i<5;i++) {
            seriesArrayList.add(-1);
        }
    }

    /**
     * set Variable(different series) by index(the number for certain series)
     */
    public void setSeries(int index) {
        // "Staleness", "Number of nodes", "Mean Time", "Dev. Time", "Num. of Iter."
        //if(xMode.equals("Staleness"))

        //@TODO make available to all variables and modes
        // "Dev T", "Mean T", "Stale", "Node #", "Iter #"
        if(seriesMode.equals("Dev T")) {
            devTime = seriesArrayList.get(index);
        }
        else if(seriesMode.equals("Mean T")) {
            meanTime = seriesArrayList.get(index);
        }
        else if(seriesMode.equals("Node #")) {
            nodeNum = seriesArrayList.get(index);
        }
        else if(seriesMode.equals("Iter #")) {
            iterNum = seriesArrayList.get(index);
        }

    }

    public int getStaleness() {
        return staleness;
    }

    public void setStaleness(int staleness) {
        if(staleness >= 0)
            this.staleness = staleness;
    }

    public int getSeriesNum() {
        return seriesNum;
    }

    MLsettings setSeriesNum(int seriesNum) {
        this.seriesNum = seriesNum;
        return this;
    }

    String getSeriesMode() {
        return seriesMode;
    }

    MLsettings setSeriesMode(String seriesMode) {
        this.seriesMode = seriesMode;
        return this;
    }

    ArrayList<Integer> getSeriesArrayList() {
        return seriesArrayList;
    }

    public MLsettings setSeriesArrayList(ArrayList<Integer> seriesArrayList) {
        this.seriesArrayList = seriesArrayList;
        return this;
    }

    int getMeanTime() {
        return meanTime;
    }

    MLsettings setMeanTime(int meanTime) {
        this.meanTime = meanTime;
        return this;
    }

    int getNodeNum() {
        return nodeNum;
    }

    MLsettings setNodeNum(int nodeNum) {
        this.nodeNum = nodeNum;
        return this;
    }

    public int getDevTime() {
        return devTime;
    }

    public MLsettings setDevTime(int devTime) {
        this.devTime = devTime;
        return this;
    }

    public int getIterNum() {
        return iterNum;
    }

    public MLsettings setIterNum(int iterNum) {
        this.iterNum = iterNum;
        return this;
    }

    public int getMinIter() {
        return minIter;
    }

    public MLsettings setMinIter(int minIter) {
        this.minIter = minIter;
        return this;
    }

    public String getxMode() {
        return xMode;
    }

    public MLsettings setxMode(String xMode) {
        this.xMode = xMode;
        return this;
    }

    public int getMinMode() {
        return minMode;
    }

    public MLsettings setMinMode(int minMode) {
        this.minMode = minMode;
        return this;
    }

    public int getMaxMode() {
        return maxMode;
    }

    public MLsettings setMaxMode(int maxMode) {
        this.maxMode = maxMode;
        return this;
    }
}
