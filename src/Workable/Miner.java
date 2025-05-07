package Workable;

import Materials.Material;

public class Miner implements Workable {

    private int level;
    private float speed;            // How fast this miner works (e.g., items/sec)
    private int inventorySize;      // Max capacity of material before unloading
    private boolean automated;      // Whether automation is purchased
    private int carriedAmount;      // Current material amount carried

    public Miner(int level, float speed, int inventorySize) {
        this.level = level;
        this.speed = speed;
        this.inventorySize = inventorySize;
        this.automated = false;
        this.carriedAmount = 0;
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

    public int getOutput() {
        int output = carriedAmount;
        carriedAmount = 0;
        return output;
    }

    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }

    public int getCarriedAmount() { return carriedAmount; }
}

