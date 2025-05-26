package Workable;
public abstract class Worker {
    protected int progress = 0;
    protected int progressToWork; // e.g., frames or ticks needed
    protected boolean automated = false;
    protected boolean working = false; // track if currently working
    protected int level = 1;

    public boolean isAutomated() {
        return automated;
    }

    public void setLevel(int level) {
        this.level = level;
        calculateProgressToWork();
    }

    protected abstract void calculateProgressToWork();

    // Called every tick
    public void doWork() {
        if ((automated || working)) {
            progress++;
            if (progress >= progressToWork) {
                performWork();
                progress = 0;
                working = false;  // done working
            }
        }
    }

    // Starts a new work cycle (if not already working)
    public void startWork() {
        if (!working) {
            working = true;
            progress = 0;
        }
    }

    // Called internally when work is complete
    protected abstract void performWork();

    // Manual click triggers timed work
    public void click() {
        if (!automated && !working) {
            startWork();
        }
    }

    public void setAutomated(boolean automated) {
        this.automated = automated;
    }
}


