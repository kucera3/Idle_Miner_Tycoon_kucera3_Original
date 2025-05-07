package Workable;

import Materials.Material;
import Strategy.MiningStrategy;

public class Miner extends Worker implements Workable {

    private int level = 1;
    private int shaftDepth;
    private MiningStrategy miningStrategy;
    private int progress = 0;
    private int progressToMine = 100;
    private float speed;
    private int inventorySize;
    private boolean automated;
    private int carriedAmount;


    public Miner(int level, float speed, int inventorySize) {
        this.level = level;
        this.speed = speed;
        this.inventorySize = inventorySize;
        this.automated = false;
        this.carriedAmount = 0;
    }


    public Miner(int shaftDepth, MiningStrategy miningStrategy) {
        this.shaftDepth = shaftDepth;
        this.miningStrategy = miningStrategy;
    }

    public double mineOneCrate() {
        return miningStrategy.crateValue(shaftDepth, level);
    }


    @Override
    public void work() {
        if (carriedAmount < inventorySize) {
            carriedAmount += Math.min(inventorySize - carriedAmount, (int) speed);
        }
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

    @Override
    public void doWork() {
        if (automated) {
            progress++;
            if (progress >= progressToMine) {
                mineOneCrate();
                progress = 0;
            }
        }
    }

    @Override
    public void click() {
        mineOneCrate();
    }

    public int getOutput() {
        int output = carriedAmount;
        carriedAmount = 0;
        return output;
    }

    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }

    public int getCarriedAmount() { return carriedAmount; }
}

