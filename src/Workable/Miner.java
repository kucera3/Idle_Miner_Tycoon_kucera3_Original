package Workable;

import java.awt.*;
import Materials.*;

public class Miner extends Worker {
    private Image minerImage;
    private int x, y;
    private int width, height;
    private int income;
    private int baseMiningTime;

    public Miner() {
        this.level = 1;
        this.income = 20;
        this.baseMiningTime = 3;
    }

    public Miner(Image minerImage, int x, int y, int width, int height, int income) {
        this.minerImage = minerImage;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.income = income;
        this.level = 1;
        this.baseMiningTime = 3;
    }


    public int getLevel() {
        return level;
    }

    public int getIncome() {
        return income;
    }

    /**
     * Mining time decreases as level increases, min 1 second.
     * Example: baseMiningTime = 3 seconds,
     * level 1 -> 3s, level 2 -> 2s, level 3 -> 1s (minimum).
     */
    public int getMiningTime() {
        int time = baseMiningTime - (level - 1);
        return Math.max(time, 1);
    }

    /**
     * Upgrades miner:
     * - increase level by 1
     * - increase income by 10 each upgrade (example)
     */
    public void upgrade() {
        level++;
        income = (int) Math.ceil(income * 1.2);
        calculateProgressToWork();
    }

    @Override
    public boolean isAutomated() {
        return super.isAutomated();
    }

    @Override
    public void setLevel(int level) {
        this.level = level;
        super.setLevel(level);
    }

    @Override
    protected void calculateProgressToWork() {
        // assuming progressToWork is in ticks or seconds, you can do:
        progressToWork = Math.max(baseMiningTime - (level - 1), 1);
    }
    @Override
    public void doWork() {
        super.doWork();
    }

    @Override
    public void startWork() {
        super.startWork();
    }

    @Override
    protected void performWork() {}

    @Override
    public void click() {
        super.click();
    }

    @Override
    public void setAutomated(boolean automated) {
        super.setAutomated(automated);
    }

    public void draw(Graphics g, Component observer) {
        g.drawImage(minerImage, x, y, width, height, observer);
    }
    public void increaseIncome(int amount) {
        income += amount;
    }

    public int getY() {
        return y;
    }

    public void setY(int newY) {
        y = newY;
    }

    public Image getMinerImage() {
        return minerImage;
    }

    public void setMinerImage(Image minerImage) {
        this.minerImage = minerImage;
    }
}
