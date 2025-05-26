package Workable;

import Materials.*;

public class ElevatorGuy extends Worker implements Workable {

    private int level = 1;
    private String speed;
    private enum State {IDLE, GOING_DOWN, LOADING, GOING_UP, UNLOADING}
    private State state = State.IDLE;
    private int capacity = 10;
    private int currentLoad = 0;
    private int progress = 0;
    private int progressToMove = 100;
    private Storage shaftStorage;
    private Storage elevatorStorage;

    public ElevatorGuy(Storage shaftStorage, int elevatorCapacity) {
        this.shaftStorage = shaftStorage;
        this.elevatorStorage = new Storage(elevatorCapacity);
        calculateProgressToWork();
    }
    private void transport() {
        int toLoad = elevatorStorage.getCapacity() - elevatorStorage.getCrates();
        int loaded = shaftStorage.removeCrates(toLoad);
        for (int i = 0; i < loaded; i++) {
            elevatorStorage.addCrate();
        }
        System.out.println("Elevator transported " + loaded + " crates.");
    }

    @Override
    public void work() {

    }

    @Override
    public void automate() {

    }

    @Override
    public boolean isAutomated() {
        return false;
    }

    @Override
    protected void calculateProgressToWork() {
        // Example: Base time is 100 ticks, reduced by 5% per level
        progressToWork = (int)(100 / (1 + 0.05 * (level - 1)));
    }

    @Override
    public void doWork() {
        if (automated) {
            progress++;
            if (progress >= progressToWork) {
                transport();
                progress = 0;
            }
        }
    }
    public Storage getElevatorStorage() {
        return elevatorStorage;
    }


    @Override
    public void click() {
        transport();
    }
}
