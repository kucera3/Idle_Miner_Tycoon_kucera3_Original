package Workable;
public abstract class Worker {
    protected boolean automated = false;
    protected int level = 1;
    protected int progress = 0;
    protected int progressToWork;

    public void setAutomated(boolean automated) {
        this.automated = automated;
    }

    public boolean isAutomated() {
        return automated;
    }

    public void setLevel(int level) {
        this.level = level;
        calculateProgressToWork();
    }

    protected abstract void calculateProgressToWork();

    public abstract void doWork();

    public abstract void click();
}

