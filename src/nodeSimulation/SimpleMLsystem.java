package nodeSimulation;

import java.util.ArrayList;
import java.util.Observable;

/**
 * Created by utri on 2017-04-17.
 */
public class SimpleMLsystem extends Observable{
    ArrayList<SimpleNode> nodeArrayList = new ArrayList<>();
    private int slowestIteration;
    private int clock = 0;  // clock that measures current time
    private int totalInfoDrag = 0;  //calculates drag caused by slow updates
    private MLsettings mLsettings;

    public int getSlowestIteration() {
        return slowestIteration;
    }

    public int getClock() {
        return clock;
    }

    public SimpleMLsystem(MLsettings mLsettings) {
        this.mLsettings = mLsettings;
        MLsettings.LEARNING_MEAN_TIME = mLsettings.getMeanTime();
        MLsettings.MINIMUM_ITERATION_TIME = mLsettings.getMinIter();
        MLsettings.NUMBER_OF_NODES = mLsettings.getNodeNum();
        MLsettings.OVERALL_STALENESS_BOUND = mLsettings.getStaleness();
        MLsettings.TIME_DEVIATION_BY_NODES = mLsettings.getDevTime();
        MLsettings.TOTAL_ITERATION = mLsettings.getIterNum();

        for(int i=0; i<mLsettings.getNodeNum(); i++) {
            nodeArrayList.add(new SimpleNode(this, i));
        }
    }

    public void setmLsettings(MLsettings mLsettings) {
        this.mLsettings = mLsettings;
        MLsettings.LEARNING_MEAN_TIME = mLsettings.getMeanTime();
        MLsettings.MINIMUM_ITERATION_TIME = mLsettings.getMinIter();
        MLsettings.NUMBER_OF_NODES = mLsettings.getNodeNum();
        MLsettings.OVERALL_STALENESS_BOUND = mLsettings.getStaleness();
        MLsettings.TIME_DEVIATION_BY_NODES = mLsettings.getDevTime();
        MLsettings.TOTAL_ITERATION = mLsettings.getIterNum();
    }

    public void initNodes() {
        // if the number of nodes is changed,
        if(nodeArrayList.size() != mLsettings.getNodeNum()) {
            nodeArrayList.clear();
            for(int i=0; i<mLsettings.getNodeNum(); i++) {
                nodeArrayList.add(new SimpleNode(this, i));
            }
        }
        else {  // initialize nodes
            for (int i = 0; i < nodeArrayList.size(); i++) {
                nodeArrayList.get(i).initSimpleNode();
            }
        }
    }

    public int getTotalInfoDrag() {
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
                    totalInfoDrag += nodeArrayList.get(i).iterationDifference(nodeArrayList.get(j));
                }
            }
        }
//        System.out.printf("Time : " + clock + "\n");
        clock++;
    }

    public void execute() {
        // initialize
        totalInfoDrag = 0;
        clock = 0;
        do {
            doTimeStep();
        } while(slowestIteration < mLsettings.getIterNum());
    }

    public void conditionsChanged() {
        setChanged();
        notifyObservers();
    }
}
