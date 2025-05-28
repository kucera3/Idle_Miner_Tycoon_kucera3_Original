package UI;

import javax.swing.*;
import java.awt.*;

public class MineLevel {
    public int yPosition;
    public boolean unlocked;
    public int moneyGenerated;
    private int balance;
    private int level;  // new
    private JButton upgradeButton;
    private boolean isMining = false;
    private Image currentMinerImage;
    private Image defaultImage;
    private Image miningImage;
    private int miningTime;
    private boolean automated = false;
    private Timer autoMineTimer;

    public boolean isAutomated() {
        return automated;
    }

    public void setAutomated(boolean automated) {
        this.automated = automated;
        if (!automated && autoMineTimer != null) {
            autoMineTimer.stop();
            autoMineTimer = null;
        }
    }

    public void startAutomation(Runnable onMine) {
        if (automated) return; // already automated
        automated = true;
        autoMineTimer = new Timer(miningTime, e -> onMine.run());
        autoMineTimer.setRepeats(true);
        autoMineTimer.start();
    }

    public void stopAutomation() {
        automated = false;
        if (autoMineTimer != null) {
            autoMineTimer.stop();
            autoMineTimer = null;
        }
    }

    public void setUpgradeButton(JButton button) {
        this.upgradeButton = button;
    }

    public JButton getUpgradeButton() {
        return upgradeButton;
    }



    public MineLevel(int yPosition) {
        this.yPosition = yPosition;
        this.unlocked = false;
        this.moneyGenerated = 20;
        this.balance = 0;
        this.level = 1;
        this.miningTime = 8000;
        defaultImage = new ImageIcon(getClass().getResource("/miner.jpg")).getImage();
        miningImage = new ImageIcon(getClass().getResource("/mining_miner.jpg")).getImage();
        currentMinerImage = defaultImage;
    }
    public boolean isMining() {
        return isMining;
    }

    public void setMining(boolean mining) {
        this.isMining = mining;
        currentMinerImage = mining ? miningImage : defaultImage;
    }

    public Image getCurrentMinerImage() {
        return currentMinerImage;
    }

    public int getBalance() {
        return balance;
    }

    public void addToBalance(int amount) {
        balance += amount;
    }

    public void resetBalance() {
        balance = 0;
    }

    public int getLevel() {
        return level;
    }

    public void upgradeLevel() {
        level++;
        moneyGenerated = (int) Math.ceil(moneyGenerated * 1.2);
        miningTime = (int) Math.ceil(miningTime * 0.8);
    }
    public int getMiningTime() {
        return miningTime;
    }
}