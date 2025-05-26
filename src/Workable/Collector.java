package Workable;

import Materials.*;


public class Collector extends Worker implements Workable{

    private int level;
    private String speed;
    private enum State {IDLE, GOING_TO_ELEVATOR, LOADING, GOING_TO_SELL, SELLING}
    private State state = State.IDLE;
    private int capacity = 10;
    private int currentLoad = 0;
    private int progress = 0;
    private int progressToMove = 100;

    private  Storage elevatorStorage;
    private  Wallet wallet;
    private final int valuePerCrate = 10;
    public Collector(Storage elevatorStorage, Wallet wallet) {
        this.elevatorStorage = elevatorStorage;
        this.wallet = wallet;
        calculateProgressToWork();
    }
    private void collectAndSell() {
        int collected = elevatorStorage.removeCrates(elevatorStorage.getCrates());
        wallet.add(collected * valuePerCrate);
        System.out.println("Collector sold " + collected + " crates for $" + (collected * valuePerCrate));
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
                collectAndSell();
                progress = 0;
            }
        }
    }

    @Override
    public void click() {
        collectAndSell();
    }
}
