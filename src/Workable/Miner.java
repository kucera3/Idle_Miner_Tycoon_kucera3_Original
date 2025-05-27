package Workable;

import Materials.*;
public class Miner extends Worker implements Workable {

    private int level = 1;
    private double miningTime = 3.0;
    // How fast this miner works (e.g., items/sec)
    private int inventorySize;      // Max capacity of material before unloading

    private int carriedAmount;      // Current material amount carried
    private Storage shaftStorage;
    protected boolean automated = false;


    public Miner(Storage shaftStorage) {
        this.shaftStorage = shaftStorage;
        calculateProgressToWork();
    }

    public Miner(int level, double miningTime, int inventorySize, int carriedAmount, Storage shaftStorage, boolean automated) {
        this.level = level;
        this.miningTime = miningTime;
        this.inventorySize = inventorySize;
        this.carriedAmount = carriedAmount;
        this.shaftStorage = shaftStorage;
        this.automated = automated;
    }

    public void upgrade() {
        level++;
        miningTime = Math.max(0.5, miningTime - 0.5);
    }

    @Override
    protected void calculateProgressToWork() {
        // Example: Base time is 100 ticks, reduced by 10% per level
        progressToWork = (int)(100 / (1 + 0.1 * (level - 1)));
    }
    private void mineOneCrate() {
        if (shaftStorage.addCrate()) {
            System.out.println("Miner mined and stored a crate.");
        } else {
            System.out.println("Shaft storage full, miner can't deposit crate.");
        }
    }

    @Override
    public void setAutomated(boolean automated) {
        super.setAutomated(automated);
    }

    @Override
    public void doWork() {

    }

    @Override
    protected void performWork() {
        if (shaftStorage.addCrate()) {
            System.out.println("Miner mined and stored a crate.");
        } else {
            System.out.println("Shaft storage full, miner can't deposit crate.");
        }
    }

    @Override
    public void click() {

    }

    @Override
    public void work() {

    }

    public void work(Material material) {
        work();
    }

    @Override
    public void automate() {
        this.automated = true;
    }

    @Override
    public boolean isAutomated() {
        return automated;
    }

    public int getOutput() {
        int output = carriedAmount;
        carriedAmount = 0;
        return output;
    }

    public int getLevel() { return level; }
    public double getMiningTime() { return miningTime; }

    public void setLevel(int level) {
        this.level = level;
        calculateProgressToWork();
    }

    public int getCarriedAmount() { return carriedAmount; }
}

