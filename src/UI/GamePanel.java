package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class GamePanel extends JPanel {

    private Image minerImage;
    private Image elevatorImage;
    private int totalMoney = 0;
    private int collectedMoney = 0;
    private final int UNLOCK_COST = 50;
    private final int UPGRADE_COST = 100;

    private int elevatorX = 300;
    private int elevatorY = 0;
    private int elevatorWidth;
    private int elevatorHeight;

    private List<MineLevel> mineLevels = new ArrayList<>();
    private int currentStopIndex = 0;
    private boolean isMoving = false;

    private Timer elevatorTimer;

    public GamePanel() {
        setLayout(null); // for absolute positioning

        minerImage = new ImageIcon(getClass().getResource("/miner.png")).getImage();
        elevatorImage = new ImageIcon(getClass().getResource("/elevator.png")).getImage();
        elevatorWidth = elevatorImage.getWidth(null);
        elevatorHeight = elevatorImage.getHeight(null);
        elevatorY = 0;

        // Initialize 3 levels
        mineLevels.add(new MineLevel(100));
        mineLevels.add(new MineLevel(250));
        mineLevels.add(new MineLevel(400));
        mineLevels.get(0).unlocked = true;

        // Unlock button
        JButton unlockButton = new JButton("Unlock Level");
        unlockButton.setBounds(600, 20, 150, 30);
        unlockButton.addActionListener(e -> unlockNextLevel());
        add(unlockButton);
        // Unlock with money button
        JButton unlockWithMoneyBtn = new JButton("Buy New Level ($50)");
        unlockWithMoneyBtn.setBounds(600, 60, 150, 30);
        unlockWithMoneyBtn.addActionListener(e -> unlockWithMoney());
        add(unlockWithMoneyBtn);

// Upgrade miners button
        JButton upgradeIncomeBtn = new JButton("Upgrade Income ($100)");
        upgradeIncomeBtn.setBounds(600, 100, 150, 30);
        upgradeIncomeBtn.addActionListener(e -> upgradeMineIncome());
        add(upgradeIncomeBtn);

        // Elevator click listener
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!isMoving && isElevatorClicked(e.getX(), e.getY())) {
                    startElevatorCycle();
                }
            }
        });

        // Timer to move elevator step-by-step
        elevatorTimer = new Timer(20, e -> updateElevator());
    }

    private boolean isElevatorClicked(int x, int y) {
        return x >= elevatorX && x <= elevatorX + elevatorWidth &&
                y >= elevatorY && y <=
                elevatorY + elevatorHeight;
    }
    private void unlockWithMoney() {
        if (totalMoney >= UNLOCK_COST) {
            for (MineLevel level : mineLevels) {
                if (!level.unlocked) {
                    level.unlocked = true;
                    totalMoney -= UNLOCK_COST;
                    repaint();
                    return;
                }
            }
        }
    }

    private void upgradeMineIncome() {
        if (totalMoney >= UPGRADE_COST) {
            for (MineLevel level : mineLevels) {
                if (level.unlocked) {
                    level.moneyGenerated += 10; // Increase by $10
                }
            }
            totalMoney -= UPGRADE_COST;
            repaint();
        }
    }

    private void unlockNextLevel() {
        for (MineLevel level : mineLevels) {
            if (!level.unlocked) {
                level.unlocked = true;
                repaint();
                return;
            }
        }
    }

    private void startElevatorCycle() {
        isMoving = true;
        currentStopIndex = 0;
        elevatorTimer.start();
    }

    private void updateElevator() {
        List<Integer> stops = getUnlockedYPositions();
        if (currentStopIndex >= stops.size()) {
            // Done visiting all stops
            elevatorTimer.stop();
            isMoving = false;
            return;
        }

        int targetY = stops.get(currentStopIndex);
        if (elevatorY < targetY) {
            elevatorY += 5;
        } else if (elevatorY > targetY) {
            elevatorY -= 5;
        } else {
            // Reached a stop
            stops = getUnlockedYPositions();
            int currentY = stops.get(currentStopIndex);

            // Check if it's a mine level (not surface)
            for (MineLevel level : mineLevels) {
                if (level.unlocked && level.yPosition == currentY) {
                    collectedMoney += level.moneyGenerated;
                }
            }

            currentStopIndex++;

            // Wait briefly before moving to the next stop
            try {
                Thread.sleep(300);
            } catch (InterruptedException ignored) {}

            // At the end of route (last stop is surface)
            if (currentStopIndex >= stops.size()) {
                totalMoney += collectedMoney;
                collectedMoney = 0;
            }
        }

        repaint();
    }

    private List<Integer> getUnlockedYPositions() {
        List<Integer> stops = new ArrayList<>();
        for (MineLevel level : mineLevels) {
            if (level.unlocked) {
                stops.add(level.yPosition);
            }
        }
        stops.add(0); // elevator always returns to surface
        return stops;
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Money: $" + totalMoney, 20, 30);
        super.paintComponent(g);

        // Draw background
        g.setColor(new Color(230, 230, 230));
        g.fillRect(0, 0, getWidth(), getHeight());

        // Draw mine levels and miners
        for (MineLevel level : mineLevels) {
            if (level.unlocked) {
                g.setColor(Color.GRAY);
                g.fillRect(0, level.yPosition + 60, getWidth(), 10);
                g.drawImage(minerImage, 100, level.yPosition, this);
            }
        }

        // Draw elevator
        g.drawImage(elevatorImage, elevatorX, elevatorY, this);
    }
}

