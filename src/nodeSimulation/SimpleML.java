package nodeSimulation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Seth Lee on 2017-04-17.
 */
public class SimpleML{
    public static void main(String[] args) {
        try{NewTest();} catch(Exception e) {
            System.out.println("Exception occured: " + e + " at line " + e.getStackTrace()[0].getLineNumber());
        }
    }

    public static void main2(String[] args) {
//        PriceSimul priceSimul = new PriceSimul(MLsettings.LEARNING_MEAN_TIME, MLsettings.TIME_DEVIATION_BY_NODES);

        // Configure settings
        MLsettings mLsettings = new MLsettings();
        mLsettings.setMeanTime(50);
        mLsettings.setDevTime(30);
        mLsettings.setIterNum(100);
        mLsettings.setNodeNum(100);
        mLsettings.setStaleness(2);

        // conduct simulation by simulated nodes
        System.out.println("Starting Simulation..");
        for(int i = 10; i < 100; i+=10) {
            mLsettings.setNodeNum(i);
            simulationResult = simulation(mLsettings);
            System.out.println("[Result]\nSimulated Nodes: " + (simulationResult.getStaleTime()));
        }
        // yield execution time by derived formulas
//        System.out.println("Starting Calculation..");
//        StaleBoundProbability staleBoundProbability = new StaleBoundProbability(mLsettings);
//        double derivedTime =  staleBoundProbability.getTotalTime(10);

        // print results
//        System.out.println("[Result]\nSimulated Nodes: " + (simulationResult.getStaleTime())
//                            + "\nFrom formulas: " + derivedTime);

    }
    public static void NewTest() throws Exception {
        File file = new File("PerformEvalData_newTest.txt");
        PrintStream ps;

        ps = new PrintStream(file);

        int DasTime = 0;
        int time = 0;

        MLsettings mLsettings = new MLsettings();
        mLsettings.setMeanTime(200);
        mLsettings.setDevTime(25);
        mLsettings.setIterNum(100);
        mLsettings.setNodeNum(200);
        mLsettings.setSampleWindow(30);
        StaleBoundProbability staleBoundProbability;
        OptimalLevel optimalLevel = new OptimalLevel();

            for(int i=3; i<=5; i++) {
                time = 0;
                for(int j = 30; j <=50; j+=10) {
                    mLsettings.setDevTime(j);
                    staleBoundProbability = new StaleBoundProbability(mLsettings);
                    time += staleBoundProbability.getTotalTime(i);
                }
                System.out.println("----For Staleness : " + i);
                System.out.println(" E. cost: " + time);
        }

        for(int i = 30; i <=50; i+=10) {
            mLsettings.setDevTime(i);
            staleBoundProbability = new StaleBoundProbability(mLsettings);
            DasTime += staleBoundProbability.getTotalTime(optimalLevel.getOptimalLevel(mLsettings));
        }

        System.out.println("Total Cost for DAS: " + DasTime);
    }

    public static void DasTest() throws Exception {
        int totalTime;
        final int ITERNUM = 150;

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH.mm.ss");
        String timeStamp = sdf.format(cal.getTime());
        File file = new File("PerformEvalData_2_" + timeStamp + ".txt");

        PrintStream ps;

        ps = new PrintStream(file);

        MLsettings mLsettings = new MLsettings();
        mLsettings.setMeanTime(100);
        mLsettings.setDevTime(30);
        mLsettings.setIterNum(ITERNUM);
        mLsettings.setNodeNum(200);
        mLsettings.setSampleWindow(30);
        StaleBoundProbability staleBoundProbability = new StaleBoundProbability(mLsettings);

        ArrayList<Integer> hostileList = new ArrayList<>();
//        for(int i=20; i < ITERNUM; i+=50) {
//            hostileList.add(i);
//        }
        mLsettings.setGaussUpdateFreq(hostileList);

        SimpleMLsystem simpleMLsystem = new SimpleMLsystem();
        simpleMLsystem.Failure_Off();
        simpleMLsystem.Preset_On();
        simpleMLsystem.initialize(mLsettings);
        for(int i=5; i<=7; i++) {
            simpleMLsystem.getmLsettings().setStaleness(i);
            System.out.println("----For Staleness : " + i);
            simpleMLsystem.execute();
            totalTime = simpleMLsystem.getClock();
            System.out.println("[Result] Fixed_TEST - Cost: " + totalTime + " Wait Time: " +
                    simpleMLsystem.getWaitTime() + " Drag: " + simpleMLsystem.getTotalInfoDrag() +
                    " Shaded Drag: " + simpleMLsystem.getShadedInfoDrag());

            // write to file
            for(double temp : simpleMLsystem.getCostLogList()) {
                ps.print(temp);
                ps.append(' ');
            }
            ps.println();
        }

        //Setting for DAS
        simpleMLsystem.getmLsettings().setStaleness(5);
        simpleMLsystem.DAS_On();
        ArrayList<Integer> updateList = new ArrayList<>();
        for(int i=30; i < ITERNUM; i+=50) {
            updateList.add(i);
        }
        simpleMLsystem.setDynamicUpdate(updateList);
        simpleMLsystem.execute();
        totalTime = simpleMLsystem.getClock();
        // write to file
        for(double temp : simpleMLsystem.getCostLogList()) {
            ps.print(temp);
            ps.append(' ');
        }
        ps.println();

        System.out.println("[Result] DAS_TEST - Cost: " + totalTime + " Wait Time: " +
                simpleMLsystem.getWaitTime() + " Drag: " + simpleMLsystem.getTotalInfoDrag() +
                " Shaded Drag: " + simpleMLsystem.getShadedInfoDrag());
    }

    public static void HostileTest() throws Exception {
        int totalTime;
        final int ITERNUM = 500;

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH.mm.ss");
        String timeStamp = sdf.format(cal.getTime());
        File file = new File("PerformEvalData_2_" + timeStamp + ".txt");

        PrintStream ps;

        ps = new PrintStream(file);

        MLsettings mLsettings = new MLsettings();
        mLsettings.setMeanTime(100);
        mLsettings.setDevTime(40);
        mLsettings.setIterNum(ITERNUM);
        mLsettings.setNodeNum(200);
        mLsettings.setSampleWindow(30);
        StaleBoundProbability staleBoundProbability = new StaleBoundProbability(mLsettings);

        ArrayList<Integer> hostileList = new ArrayList<>();
        for(int i=100; i < ITERNUM; i+=100) {
            hostileList.add(i);
        }
        mLsettings.setGaussUpdateFreq(hostileList);

        SimpleMLsystem simpleMLsystem = new SimpleMLsystem();
        simpleMLsystem.Failure_On();
        simpleMLsystem.Hostile_On();
        simpleMLsystem.Preset_Off();
        simpleMLsystem.initialize(mLsettings);
        for(int i=10; i<=12; i++) {
            simpleMLsystem.getmLsettings().setStaleness(i);
            System.out.println("----For Staleness : " + i);
            simpleMLsystem.execute();
            totalTime = simpleMLsystem.getClock();
            System.out.println("[Result]Fixed_TEST - Cost: " + totalTime + " Wait Time: " +
                    simpleMLsystem.getWaitTime() + " Drag: " + simpleMLsystem.getTotalInfoDrag() +
                    " Shaded Drag: " + simpleMLsystem.getShadedInfoDrag());

            // write to file
            for(double temp : simpleMLsystem.getCostLogList()) {
                ps.print(temp);
                ps.append(' ');
            }
            ps.println();
        }

        //Setting for DAS
        simpleMLsystem.getmLsettings().setStaleness(5);
        simpleMLsystem.DAS_On();
        ArrayList<Integer> updateList = new ArrayList<>();
        for(int i=30; i < ITERNUM; i+=30) {
            updateList.add(i);
        }
        simpleMLsystem.setDynamicUpdate(updateList);
        simpleMLsystem.execute();
        totalTime = simpleMLsystem.getClock();
        // write to file
        for(double temp : simpleMLsystem.getCostLogList()) {
            ps.print(temp);
            ps.append(' ');
        }
        ps.println();

        System.out.println("[Result] DAS_TEST - Cost: " + totalTime + " Wait Time: " +
                simpleMLsystem.getWaitTime() + " Drag: " + simpleMLsystem.getTotalInfoDrag() +
                " Shaded Drag: " + simpleMLsystem.getShadedInfoDrag());
    }

    public static SimulationResult simulationResult = new SimulationResult();

    public static SimulationResult simulation(MLsettings mLsettings) {
        int staleTotalTime, totalTime, staleInfoDrag, infoDrag, dragDiff;
        SimpleMLsystem simpleMLsystem;

        //with staleness
        simpleMLsystem = new SimpleMLsystem(mLsettings);
        simpleMLsystem.execute();

        staleTotalTime = simpleMLsystem.getClock();
//        staleInfoDrag = simpleMLsystem.getTotalInfoDrag();

        // with 0 staleness : synchronous
//        mLsettings.setStaleness(0);
//        simpleMLsystem.setmLsettings(mLsettings);
//        simpleMLsystem.initNodes();
//        simpleMLsystem.execute();
//
//        totalTime = simpleMLsystem.getClock();
//        infoDrag = simpleMLsystem.getTotalInfoDrag();
//
//        dragDiff = (staleInfoDrag - infoDrag) / (mLsettings.getNodeNum()*mLsettings.getMeanTime());

//        simulationResult.setSyncTime(totalTime);    // set time took for synchronous parallel
//        simulationResult.setStaleGoodness(totalTime - staleTotalTime); // set the advantage for using SSP
//        simulationResult.setDragDiff(dragDiff);
        simulationResult.setStaleTime(staleTotalTime);
//        System.out.printf("staleWin : " + staleWin +"\n");


        //System.out.println("");

        //System.out.println("Stale Goodness : " + staleWin);
        return simulationResult;
    }
}
