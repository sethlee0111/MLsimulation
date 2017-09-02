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

    private int stalenessBound = MLsettings.OVERALL_STALENESS_BOUND;

    /* variables which changes through time */
    private int currentIteration = 0;

    public SimpleNode() {    }

    public SimpleNode(SimpleMLsystem observable, int nodeNum) {
        this.system = observable;
        this.nodeNum = nodeNum;
        observable.addObserver(this);
        //updateComponents();
        initIterationTimeArray();
    }

    public void initSimpleNode() {
        currentIteration = 0;
        previousTime = 0;
        initIterationTimeArray();
        rand = new Random();
    }
    /**
     * change all conditions of ML according to the normal distribution
     */
    private void updateComponents() {

//        do {
//            iterationTime = (int) (rand.nextGaussian() * TIME_DEVIATION_BY_NODES + LEARNING_MEAN_TIME);
//        } while(iterationTime < MLsettings.MINIMUM_ITERATION_TIME);
        
    }

    private void initIterationTimeArray() {
        int iterationTime;
        for(int i=0; i < MLsettings.TOTAL_ITERATION; i++) {
            //pick iteration time from gaussian dist. until it is larger than minimum iteration time
            //do {
                iterationTime = (int) (rand.nextGaussian() * TIME_DEVIATION_BY_NODES + LEARNING_MEAN_TIME);
            //} while(iterationTime < MLsettings.MINIMUM_ITERATION_TIME);
            iterationTimeArray.add(iterationTime);
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
        if (currentIteration < MLsettings.TOTAL_ITERATION) {
            //if bounded
            if (currentIteration > system.getSlowestIteration() + MLsettings.OVERALL_STALENESS_BOUND) {
//                System.out.printf("iter : " +currentIteration +"/ " + this + " is bounded\n");
                previousTime = system.getClock();   // the iteration does not start so time took for current - 1 iteration is now
            } else if (system.getClock() - previousTime >= iterationTimeArray.get(currentIteration)) {
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
