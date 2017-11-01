package nodeSimulation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by sethlee on 9/27/17.
 *
 * performance evaluation for Optimal SSP, by number of nodes
 * 'optimal level' refers to the best staleness level
 */
public class PerformanceEval_1 {
    public static void main(String[] args) {
        final int FIRST_NUM = 50;
        final int LAST_NUM = 501;
        final int STEP =  50;
        final int TESTS = 10;

        int tmp;

        ArrayList<Integer> slowCosts = new ArrayList<>();
        ArrayList<Integer> optCosts = new ArrayList<>();
        ArrayList<Integer> optCosts2 = new ArrayList<>();

        SimulationResult simulationResult;
        // configure the basic settings
        MLsettings mLsettings = new MLsettings();
        mLsettings.setMeanTime(100);
        mLsettings.setDevTime(30);
        mLsettings.setIterNum(100);
        mLsettings.setStaleness(4);

        for(int i=FIRST_NUM; i < LAST_NUM; i += STEP) {
            mLsettings.setNodeNum(i);   // change the number of nodes
            tmp = 0;
            for(int j=0; j<TESTS; j++) {
                simulationResult = SimpleML.simulation(mLsettings);    // conduct simulation
                tmp += simulationResult.getStaleTime();
                System.out.printf(".." + j);
            }
            slowCosts.add(tmp/TESTS);
//            slowCosts.add(simulationResult.getStaleTime());
            System.out.println("-> Simple :" + i + " done");
        }

        OptimalLevel optimalLevel = new OptimalLevel();
        int optLevel;

        for(int i=FIRST_NUM; i < LAST_NUM; i += STEP) {
            mLsettings.setNodeNum(i);   // change the number of nodes
            optLevel = optimalLevel.getOptimalLevel(mLsettings);    // get optimal level
            mLsettings.setStaleness(optLevel);  // set with optimal level
//            optCosts.add(simulationResult.getStaleTime()*100/slowCosts.get((i-FIRST_NUM)/STEP));
            tmp = 0;
            for(int j=0; j<TESTS; j++) {
                simulationResult = SimpleML.simulation(mLsettings);    // conduct simulation
                tmp += simulationResult.getStaleTime();
                System.out.printf(".." + j);
            }
            optCosts.add(tmp/TESTS);
//            optCosts.add(simulationResult.getStaleTime());
            System.out.println("-> Opt1 :" + i + " done | optlevel: " + optLevel);
        }

//        for(int i=FIRST_NUM; i < LAST_NUM; i += STEP) {
//            mLsettings.setNodeNum(i);   // change the number of nodes
//            optLevel = optimalLevel.getOptimalLevel(mLsettings);    // get optimal level
//            mLsettings.setStaleness(optLevel+1);  // set with optimal level
//            tmp = 0;
//            for(int j=0; j<TESTS; j++) {
//                simulationResult = SimpleML.simulation(mLsettings);    // conduct simulation
//                tmp += simulationResult.getStaleTime();
//                System.out.printf(".." + j);
//            }
//            optCosts2.add(tmp/TESTS);
////            optCosts2.add(simulationResult.getStaleTime()*100/slowCosts.get((i-FIRST_NUM)/STEP));
////            optCosts2.add(simulationResult.getStaleTime());
//            System.out.println("-> Opt2 :" + i + " done | optlevel: " + (optLevel+1));
//        }

        // write to file
        File file = new File("PerformEvalData_coffeeBean.txt");
        PrintStream ps;
        try {
            ps = new PrintStream(file);

            for(double temp : slowCosts) {
                ps.print(temp);
                ps.append(' ');
            }
            ps.println();
            for(double temp : optCosts) {
                ps.print(temp);
                ps.append(' ');
            }
            ps.println();
//            for(double temp : optCosts2) {
//                ps.print(temp);
//                ps.append(' ');
//            }
        } catch (FileNotFoundException e) {   }

        Iterator slowIter = slowCosts.iterator();
        Iterator optIter = optCosts.iterator();
//        Iterator optIter2 = optCosts2.iterator();

        for(int i=FIRST_NUM; i<LAST_NUM; i+=STEP) {
            System.out.printf("NodeNum: " + i + " | " + slowIter.next() + " | " + optIter.next() + "\n");
        }
    }
}
