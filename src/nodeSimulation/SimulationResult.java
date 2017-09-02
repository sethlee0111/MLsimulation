package nodeSimulation;

/**
 * Created by sethlee on 5/14/17.
 */
public class SimulationResult {
    private int syncTime;
    private int staleGoodness;
    private int dragDiff;
    private int staleWait;

    public int getStaleWait() {
        return staleWait;
    }

    public int getSyncTime() {
        return syncTime;
    }

    public void setSyncTime(int syncTime) {
        this.syncTime = syncTime;
    }

    public void setStaleWait(int staleWait) {
        this.staleWait = staleWait;
    }

    public int getStaleGoodness() {
        return staleGoodness;
    }

    public void setStaleGoodness(int staleGoodness) {
        this.staleGoodness = staleGoodness;
    }

    public int getDragDiff() {
        return dragDiff;
    }

    public void setDragDiff(int dragDiff) {
        this.dragDiff = dragDiff;
    }
}
