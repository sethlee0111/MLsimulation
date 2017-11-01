package nodeSimulation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;
import java.util.Random;

/**
 * Created by utri on 2017-04-17.
 *
 * Usage for DAS : 1. Enable DAS mode by calling DAS_On()
 *                 2. By MLsettings, input when the allocation will take place
 *                 3. if MLsettings is not set, the whole ML will run as Bulk Synchronous Parallel
 */
public class SimpleMLsystem extends Observable{
    private int[][] timeTable; // table for keeping time took for each iteration, for each nodes
    private int timeTableIndex = 0; // for keeping index for timeTable
    private int[][] approxTable; // table for keeping approximated mean and deviation

    private OptimalLevel optimalLevel = new OptimalLevel();

    private ArrayList<SimpleNode> nodeArrayList = new ArrayList<>();
    private ArrayList<Integer> dynamicUpdate = new ArrayList<>();   // List for updating staleness
    private ArrayList<Integer> hostileUpdate= new ArrayList<>();   // List for updating staleness
    private ArrayList<Integer> randomMeans= new ArrayList<>();   // List for updating staleness

    private ArrayList<Integer> logList = new ArrayList<>();
    private ArrayList<Double> costLogList = new ArrayList<>();

    public ArrayList<Integer> getLogList() {
        return logList;
    }

    public ArrayList<Double> getCostLogList() {
        return costLogList;
    }

    private final double DRAG_SCALE = 0.0001;

    private boolean DAS_flag = false;   // flag for implementing Dynamic Allocation of Staleness
    private boolean Hostile_flag = false;
    private boolean failure_flag = false;
    private boolean preset_flag = false;
    private boolean isFinished = false;
    private MLsettings condition = new MLsettings();

    private int slowestIteration;
    private int clock = 0;  // clock that measures current time
    private double totalInfoDrag = 0;  //calculates drag caused by slow updates
    private double shadedInfoDrag = 0;  //calculates drag caused by slow updates

    public double getShadedInfoDrag() {
        return shadedInfoDrag;
    }

    private MLsettings mLsettings;

    public void DAS_On() {DAS_flag = true;}
    public void DAS_Off() {DAS_flag = false;}
    public void Hostile_On() {Hostile_flag = true;}
    public void Hostile_Off() {Hostile_flag = false;}
    public void Failure_On() {failure_flag = true;}
    public void Failure_Off() {failure_flag = false;}
    public void Preset_On() {preset_flag = true;}
    public void Preset_Off() {preset_flag = false;}

    public boolean isFailure() {return failure_flag;}

    public boolean isHostile() {
        return Hostile_flag;
    }

    public boolean isPreset() {return preset_flag;}

    public ArrayList<Integer> getRandomMeans() {
        return randomMeans;
    }

    public ArrayList<Integer> getHostileUpdate() {
        return hostileUpdate;
    }

    public int getSlowestIteration() {
        return slowestIteration;
    }

    public int getClock() {
        return clock;
    }

    public void notifyEnd() {isFinished = true;}

    public SimpleMLsystem(MLsettings mLsettings) {
        initialize(mLsettings);
    }

    public SimpleMLsystem() {   }

    public void initialize(MLsettings mLsettings) {
        setmLsettings(mLsettings);
        isFinished = false;
        for(int i=0; i<mLsettings.getNodeNum(); i++) {
            nodeArrayList.add(new SimpleNode(this, i));
        }
//        if(DAS_flag) {
        timeTable = new int[mLsettings.getNodeNum()][mLsettings.getSampleWindow()]; // initialize timeTable
        for (int i = 0; i < mLsettings.getNodeNum(); i++) {
            Arrays.fill(timeTable[i], -1);     // fill all elements with -1
        }
        approxTable = new int[2][mLsettings.getIterNum()];
//        }
    }

    public void setDynamicUpdate(ArrayList<Integer> dynamicUpdate) {
        this.dynamicUpdate = dynamicUpdate;
    }

    public void setmLsettings(MLsettings mLsettings) {
        Random rand = new Random();

        this.mLsettings = mLsettings;
        condition.setNodeNum(mLsettings.getNodeNum());
        condition.setIterNum(mLsettings.getIterNum());
        dynamicUpdate = mLsettings.getDynamicList();
        hostileUpdate = mLsettings.getGaussUpdateFreq();
        if(!dynamicUpdate.isEmpty())    // automatically power on DAS
            DAS_On();
        if(!hostileUpdate.isEmpty())
            Hostile_On();
        if(Hostile_flag) {
            for(int i=0; i < mLsettings.getIterNum() ; i++) {
                if(hostileUpdate.contains(i)) {
                    randomMeans.add(rand.nextInt(10) + 20);
                }
            }
        }
    }

    public MLsettings getmLsettings() {
        return mLsettings;
    }

    public void initNodes() {
        for(SimpleNode node : nodeArrayList) {
            node.initSimpleNode();
        }
        logList.clear();
        costLogList.clear();
    }

    public double getTotalInfoDrag() {
        return totalInfoDrag;
    }

    public void doTimeStep() {
        // get the slowest iteration
        slowestIteration = mLsettings.getIterNum();
        for(int i=0; i<nodeArrayList.size(); i++) {
            if (slowestIteration > nodeArrayList.get(i).getCurrentIteration())
                slowestIteration = nodeArrayList.get(i).getCurrentIteration();
        }
        // perform time steps
        for(int i=0; i<nodeArrayList.size(); i++) {
            nodeArrayList.get(i).doTimeStep();
            // compute information drag
            for(int j=0; j<mLsettings.getNodeNum(); j++) {
                if(i != j) {
                    totalInfoDrag += nodeArrayList.get(i).iterationDifference(nodeArrayList.get(j)) * DRAG_SCALE;
                }
            }
            shadedInfoDrag += (nodeArrayList.get(i).getCurrentIteration() - slowestIteration) * DRAG_SCALE;
        }
        clock++;
    }

    public void execute() {
        // initialize
        totalInfoDrag = 0;
        shadedInfoDrag = 0;
        clock = 0;
        slowestIteration = 0;
        initNodes();
        do {
            doTimeStep();
            // DAS
            if (DAS_flag && dynamicUpdate.contains(slowestIteration)) {
                int[] condArr;
                condArr = getApproxGaussian();
                condition.setMeanTime(condArr[0]);
                condition.setDevTime(condArr[1]);
                condition.setIterNum(mLsettings.getIterNum() - slowestIteration);
                mLsettings.setStaleness(optimalLevel.getOptimalLevel(condition)); // update staleness
                System.out.println("At slowest iteration " + slowestIteration);
                dynamicUpdate.remove(new Integer(slowestIteration));
            }
            // logging current cost for data output
            if (slowestIteration % 10 == 0 && !logList.contains(slowestIteration)) {
                logList.add(slowestIteration);
                costLogList.add(this.getTotalInfoDrag());
            }
//            System.out.println("slow: " + slowestIteration + " iter: " + mLsettings.getIterNum());
        } while(slowestIteration < mLsettings.getIterNum());
//        } while (!isFinished);
        if(DAS_flag)
            System.out.println("Mean of Allocated Optimal Levels : " + optimalLevel.getOptimalHistoryMean());
    }

    /**
     * a node notifies its time took for a single iteration when it is done.
     * the time is then saved in the timeTable
     * @param node
     * @param time
     */
    public void notifyTime(SimpleNode node, int time) {
        if(DAS_flag) {
            timeTable[node.getNum()][node.getCurrentIteration() % mLsettings.getSampleWindow()] = time;
        }
    }

    public int[] getApproxGaussian() {
        int avg, dev;
        int[][] gaussTable;
        int nodeCnt = 0;
        int sampleCnt = 0;
        // search for the lastly completed timetable
        // after break; J-1 is the lastly completed column of iteration in timeTable
        gaussTable = new int[2][mLsettings.getSampleWindow()];
        // fill in the mean, dev table
        for(int j=0; j<mLsettings.getSampleWindow(); j++) {
            avg = 0; dev = 0;
            nodeCnt = 0;
            for(int i=0; i<mLsettings.getNodeNum(); i++) {
                if(timeTable[i][j] > 0) {
                    avg += timeTable[i][j];
                    nodeCnt++;
                }
//                else {
//                    System.out.println("0 time in timetable!");
//                }
//                System.out.printf(" " + timeTable[i][j]);
            }
//            System.out.println();
            if(nodeCnt < 30 )
                break;
            avg /= nodeCnt;
            gaussTable[0][j] = avg; // mean
            // get dev
            for(int i=0; i<mLsettings.getNodeNum(); i++) {
                dev += (avg - timeTable[i][j]) * (avg - timeTable[i][j]);
            }
            dev /= (nodeCnt-1); // get VAR
            dev = (int) Math.sqrt(dev);
            gaussTable[1][j] = dev; // dev

            sampleCnt++;
        }

        // get the final dev and mean
        int[] res = new int[2];
        avg = 0; dev = 0;
        for(int i=0; i<mLsettings.getSampleWindow(); i++) {
            avg += gaussTable[0][i];
            dev += gaussTable[1][i];
//            System.out.println("avg: " + gaussTable[0][i] + " dev: " + gaussTable[1][i]);
        }
        avg /= sampleCnt;
        dev /= sampleCnt;
        System.out.println("Final avg: " + avg + " dev: " + dev);
        res[0] = avg;
        res[1] = dev;
        return res;
    }

    public void conditionsChanged() {
        setChanged();
        notifyObservers();
    }

    /**
     * return total waiting time for all nodes
     */
    public double getWaitTime() {
        double total = 0;
        for(SimpleNode node : nodeArrayList) {
            total += node.getWaitTime();
        }
        return total;
    }

    /**
     *
     * @param iter
     * @return time
     * @TODO this is incomplete
     */
    public int getPresetDev_(int iter) {
        if(0 <= iter && iter < 60)
            return 15;
        else if(60 <= iter && iter < 120)
            return 20;
        else if(120 <= iter && iter < 180)
            return 25;
        else if(180 <= iter && iter < 240)
            return 30;
        else if(240 <= iter && iter < 250)
            return 30;
        else    // default
            return 30;
    }
    public int getPresetDev(int iter) {
        if(0 <= iter && iter < 50)
            return 15;
        else if(50 <= iter && iter < 100)
            return 25;
        else if(100 <= iter && iter <= 150)
            return 35;
        else    // default
            return 30;
    }
}
