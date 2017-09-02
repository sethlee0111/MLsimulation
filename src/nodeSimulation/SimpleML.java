package nodeSimulation;

/**
 * Created by Seth Lee on 2017-04-17.
 */
public class SimpleML{
    public static void main(String[] args) {
//        PriceSimul priceSimul = new PriceSimul(MLsettings.LEARNING_MEAN_TIME, MLsettings.TIME_DEVIATION_BY_NODES);

        // Configure settings
        MLsettings mLsettings = new MLsettings();
        mLsettings.setMeanTime(50);
        mLsettings.setDevTime(1);
        mLsettings.setIterNum(30);
        mLsettings.setNodeNum(100);

        // conduct simulation by simulated nodes
        System.out.println("Starting Simulation..");
        simulationResult = simulation(mLsettings);

        // yield execution time by derived formulas
        System.out.println("Starting Calculation..");
        StaleBoundProbability staleBoundProbability = new StaleBoundProbability(mLsettings);
        double derivedTime =  staleBoundProbability.getTotalTime(3); // result 1450

        // print results
        System.out.println("[Result]\nSimulated Nodes: " + (simulationResult.getSyncTime() - simulationResult.getStaleGoodness())
                            + "\nFrom formulas: " + derivedTime);
//        System.out.printf("result\nsyncTime: " + priceSimul.getSyncTime(MLsettings.TOTAL_ITERATION, MLsettings.NUMBER_OF_NODES) +
//                "\nTime Took for sync: " + simulationResult.getSyncTime() +
//                "\nstaleGoodness : " + simulationResult.getStaleGoodness() +
//                            "\ndragDiff : " + simulationResult.getDragDiff());

    }
    public static SimulationResult simulationResult = new SimulationResult();

    public static SimulationResult simulation(MLsettings mLsettings) {
        int staleTotalTime, totalTime, staleInfoDrag, infoDrag, dragDiff;
        SimpleMLsystem simpleMLsystem;

        //with staleness
        simpleMLsystem = new SimpleMLsystem(mLsettings);
        simpleMLsystem.execute();
        staleTotalTime = simpleMLsystem.getClock();
        staleInfoDrag = simpleMLsystem.getTotalInfoDrag();

        // with 0 staleness : synchronous
        mLsettings.setStaleness(0);
        simpleMLsystem.setmLsettings(mLsettings);
        simpleMLsystem.initNodes();
        simpleMLsystem.execute();
        totalTime = simpleMLsystem.getClock();
        infoDrag = simpleMLsystem.getTotalInfoDrag();

        dragDiff = (staleInfoDrag - infoDrag) / (mLsettings.getNodeNum()*mLsettings.getMeanTime());

        simulationResult.setSyncTime(totalTime);    // set time took for synchronous parallel
        simulationResult.setStaleGoodness(totalTime - staleTotalTime); // set the advantage for using SSP
        simulationResult.setDragDiff(dragDiff);
//        System.out.printf("staleWin : " + staleWin +"\n");


        //System.out.println("");

        //System.out.println("Stale Goodness : " + staleWin);
        return simulationResult;
    }
}
