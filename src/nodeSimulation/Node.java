package nodeSimulation;
/**
 * Created by Seth Lee on 2017-04-17.
 */

import java.util.Observable;
import java.util.Random;

/**
 * nodeSimulation.Node Class
 */
public class Node extends SimpleNode {
    private NodeLocation nodeLocation = new NodeLocation();
    Random random = new Random();

    public Node (SimpleMLsystem observable, int nodeNum) {
        super(observable, nodeNum);
    }
}
