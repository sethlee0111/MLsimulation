package nodeSimulation;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import static java.lang.Math.abs;
import static nodeSimulation.MLsettings.LEARNING_MEAN_TIME;
import static nodeSimulation.MLsettings.TIME_DEVIATION_BY_NODES;

/**
 * Created by utri on 2017-04-17.
 */
public class SimpleNode implements Observer{
    SimpleMLsystem system;  // ML system that executes each nodes
    int nodeNum;
//    int infoDrag;   // a drawback caused by the difference between not updated infos between the nodes

    /* constants */
    Random rand = new Random();
    // According to the normal distribution
    ArrayList<Integer> iterationTimeArray = new ArrayList<>();
    private int previousTime = 0;   // time took processing (current - 1) iterations

    /* variables which changes through time */
    private int currentIteration = 0;

    private double waitTime; // when the node is bounded, this var records waiting time.

    public double getWaitTime() {
        return waitTime;
    }

    public SimpleNode(SimpleMLsystem observable, int nodeNum) {
        this.system = observable;
        this.nodeNum = nodeNum;
        observable.addObserver(this);
        initIterationTimeArray();
    }

    public int getNum() {
        return nodeNum;
    }

    public void initSimpleNode() {
        currentIteration = 0;
        previousTime = 0;
    }
    /**
     * change all conditions of ML according to the normal distribution
     */
    private void updateComponents() {

//        do {
//            iterationTime = (int) (rand.nextGaussian() * TIME_DEVIATION_BY_NODES + LEARNING_MEAN_TIME);
//        } while(iterationTime < MLsettings.MINIMUM_ITERATION_TIME);
        
    }

    /**
     * generate the data for costs of iterations of this Node
     * Set Hostile_flag ON to make means fluctuate throughout the ML
     * Set Failure_flag ON to make some of the nodes fail(very slow)
     */
    private void initIterationTimeArray() {
        int iterationTime = 0;
        waitTime = 0;
        if(!system.isPreset()) {
            for (int i = 0; i < system.getmLsettings().getIterNum(); i++) {
                // Node Fluctuation : Making Hostile Environment
                if (system.isHostile() && system.getHostileUpdate().contains(i)) {
                    system.getmLsettings().setDevTime(system.getRandomMeans().get(system.getHostileUpdate().indexOf(i)));
                }
                do {
                    if (!system.isFailure()) {   // non-failure, non-hostile mode
                        iterationTime = (int) (rand.nextGaussian() * (double) system.getmLsettings().getDevTime() +
                                system.getmLsettings().getMeanTime());
                    } else {    // failure mode
                        if (this.nodeNum % 20 == 0 && i < 1000 && 300 < i) {// every 30 nodes
                            iterationTime = 400;     // very slow
                        } else {
                            iterationTime = (int) (rand.nextGaussian() * (double) system.getmLsettings().getDevTime() +
                                    system.getmLsettings().getMeanTime());
                        }
                    }
                } while (iterationTime < 10);
                iterationTimeArray.add(iterationTime);
            }
        }
        else {  // preset mode
            for (int i = 0; i < system.getmLsettings().getIterNum(); i++) {
                iterationTimeArray.add((int)(rand.nextGaussian() * (double) system.getPresetDev(i) +
                                        system.getmLsettings().getMeanTime()));
            }
        }
    }

    /**
     * update method for observer pattern
     * @param obs
     * @param arg
     */
    public void update(Observable obs, Object arg) {
        if(obs instanceof SimpleMLsystem){
            updateComponents();
        }
    }

    /**
     * getter for current number of Iteration
     * @return currentIteration
     */
    public int getCurrentIteration() {
        return currentIteration;
    }

    /**
     * This node simulates doing one timestep
     * one iteration is consisted of many timeSteps, which is
     * this (Iteration) is (iterationTime) timeSteps
     */
    public void doTimeStep() {
        if (currentIteration < system.getmLsettings().getIterNum()) {
            //if bounded
            if (currentIteration > system.getSlowestIteration() + system.getmLsettings().getStaleness()) {
                previousTime = system.getClock();   // the iteration does not start so time took for current - 1 iteration is now
                // previousTime : time took for (current - 1)th iteration
                waitTime+=0.00001;
            } else if (system.getClock() - previousTime >= iterationTimeArray.get(currentIteration)) {
                system.notifyTime(this, iterationTimeArray.get(currentIteration));
                newIteration();
            }
        }
    }
    @Override
    public String toString() {
        String string = "Node #"+nodeNum;
        return string;
    }

    /**
     * initialization before the start of the new iteration
     */
    public void newIteration() {
        //updateComponents();
        previousTime = system.getClock();
        currentIteration++;
    }

    public int iterationDifference(SimpleNode sn2) {
        return abs(this.getCurrentIteration() - sn2.getCurrentIteration());
    }
}

//check if there is a node slower than c-s-1
//if so, isRunning = false;
