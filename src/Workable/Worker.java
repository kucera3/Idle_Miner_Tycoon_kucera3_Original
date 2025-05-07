package Workable;
public abstract class Worker {
    protected boolean automated = false;

    public void setAutomated(boolean automated) {
        this.automated = automated;
    }

    public boolean isAutomated() {
        return automated;
    }

    // Called each game tick, e.g., every 100ms
    public abstract void doWork();

    // Called on player click
    public abstract void click();
}

