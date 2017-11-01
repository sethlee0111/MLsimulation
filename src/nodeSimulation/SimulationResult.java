package nodeSimulation;

/**
 * Created by sethlee on 5/14/17.
 */
public class SimulationResult {
    private int syncTime;
    private int staleGoodness;
    private double dragDiff;
    private int staleWait;

    private int staleTime;

    public int getStaleTime() {
        return staleTime;
    }

    public void setStaleTime(int staleTime) {
        this.staleTime = staleTime;
    }

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

    public double getDragDiff() {
        return dragDiff;
    }

    public void setDragDiff(int dragDiff) {
        this.dragDiff = dragDiff;
    }
}
