package UI;


import Workable.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GamePanel extends JPanel {

    private Image minerImage;
    private Image miningMinerImage;
    private Image elevatorImage;
    private JButton elevatorButton;

    private int totalMoney = 0;

    private List<MineLevel> mineLevels = new ArrayList<>();
    private List<JButton> upgradeButtons = new ArrayList<>();
    private List<JButton> mineButtons = new ArrayList<>();
    private List<JButton> automateButtons = new ArrayList<>();

    private Elevator elevator;
    private JButton elevatorAutomateButton;

    private int elevatorX = 300;

    public GamePanel() {
        setLayout(null);

        // Load images
        minerImage = new ImageIcon(getClass().getResource("/miner.jpg")).getImage();
        miningMinerImage = new ImageIcon(getClass().getResource("/mining_miner.jpg")).getImage();
        elevatorImage = new ImageIcon(getClass().getResource("/elevator.jpg")).getImage();

        // Initialize mine levels
        mineLevels.add(new MineLevel(100));
        mineLevels.add(new MineLevel(250));
        mineLevels.add(new MineLevel(400));
        mineLevels.get(0).unlocked = true;

        // Initialize Elevator
        elevator = new Elevator(elevatorX, 0, elevatorImage.getWidth(null), elevatorImage.getHeight(null), elevatorImage);
        elevator.startMovingOnce(mineLevels);

        // Add elevator automate button
        elevatorAutomateButton = new JButton("Automate Elevator ($1500)");
        elevatorAutomateButton.setBounds(elevatorX + elevator.getWidth() + 10, 60, 240, 30);
        add(elevatorAutomateButton);

        elevatorButton = new JButton("Elevator");
        elevatorButton.setBounds(elevatorX + elevator.getWidth() + 10, 20, 100, 30);
        add(elevatorButton);

        elevatorButton.addActionListener(e -> {
            if (!elevator.isMoving()) {
                elevator.startMovingOnce(mineLevels);
            }
        });

        elevatorAutomateButton.addActionListener(e -> {
            if (!elevator.isAutomated()) {
                if (totalMoney >= 1500) {
                    totalMoney -= 1500;
                    elevator.startAutomation(mineLevels);
                    elevatorAutomateButton.setText("Stop Elevator Automation");
                } else {
                    JOptionPane.showMessageDialog(this, "Not enough money to automate elevator! Cost: $1500");
                }
            } else {
                elevator.stop();
                elevatorAutomateButton.setText("Automate Elevator ($1500)");
            }
            repaint();
        });

        // Initialize buttons for mine levels
        for (int i = 0; i < mineLevels.size(); i++) {
            MineLevel level = mineLevels.get(i);

            JButton upgradeButton = new JButton("Upgrade");
            upgradeButtons.add(upgradeButton);
            add(upgradeButton);

            JButton mineButton = new JButton("Mine");
            mineButtons.add(mineButton);
            add(mineButton);

            JButton automateButton = new JButton("Automate");
            automateButtons.add(automateButton);
            add(automateButton);

            final int index = i;

            upgradeButton.addActionListener(e -> {
                MineLevel lvl = mineLevels.get(index);
                int cost = (int) (30 * Math.pow(1.5, lvl.getLevel()));
                if (totalMoney >= cost) {
                    totalMoney -= cost;
                    lvl.upgradeLevel();
                    repaint();
                }
            });

            mineButton.addActionListener(e -> {
                MineLevel lvl = mineLevels.get(index);
                if (!lvl.unlocked || lvl.isMining()) return;

                lvl.setMining(true);
                repaint();

                Timer mineTimer = new Timer(lvl.getMiningTime(), ev -> {
                    lvl.addToBalance(lvl.moneyGenerated);
                    lvl.setMining(false);
                    repaint();
                    ((Timer) ev.getSource()).stop();
                });
                mineTimer.setRepeats(false);
                mineTimer.start();
            });

            automateButton.addActionListener(e -> {
                MineLevel lvl = mineLevels.get(index);
                if (!lvl.isAutomated()) {
                    if (totalMoney >= 500) {
                        totalMoney -= 500;
                        lvl.setMining(true);
                        lvl.startAutomation(() -> {
                            lvl.addToBalance(lvl.moneyGenerated);
                            repaint();
                        });
                    } else {
                        JOptionPane.showMessageDialog(this, "Not enough money to automate! Cost: $500");
                    }
                } else {
                    lvl.stopAutomation();
                    lvl.setMining(false);
                }
                repaint();
            });
        }
    }

    private List<Integer> getUnlockedYPositions() {
        List<Integer> stops = new ArrayList<>();
        for (MineLevel level : mineLevels) {
            if (level.unlocked) {
                stops.add(level.yPosition);
            }
        }
        stops.add(0); // add ground level
        return stops;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Background
        g.setColor(new Color(230, 230, 230));
        g.fillRect(0, 0, getWidth(), getHeight());

        // Draw miners and mine levels UI
        for (int i = 0; i < mineLevels.size(); i++) {
            MineLevel level = mineLevels.get(i);
            JButton upgradeButton = upgradeButtons.get(i);
            JButton mineButton = mineButtons.get(i);
            JButton automateButton = automateButtons.get(i);

            if (level.unlocked) {
                g.setColor(Color.GRAY);
                g.fillRect(0, level.yPosition + 60, getWidth(), 10);

                int minerWidth = 100;
                int minerHeight = 120;
                Image img = level.isMining() ? miningMinerImage : minerImage;
                g.drawImage(img, 100, level.yPosition, minerWidth, minerHeight, this);

                g.setColor(Color.BLUE);
                String text = "Balance: $" + level.getBalance() + "  Level: " + level.getLevel();
                g.drawString(text, 100, level.yPosition + minerHeight + 20);

                int buttonY = level.yPosition + minerHeight + 20;
                int upgradeCost = (int) (30 * Math.pow(1.5, level.getLevel()));
                upgradeButton.setText("Upgrade ($" + upgradeCost + ")");
                upgradeButton.setBounds(90, buttonY, 140, 30);
                upgradeButton.setVisible(true);

                mineButton.setBounds(210, buttonY, 100, 30);
                mineButton.setVisible(true);

                automateButton.setBounds(0, buttonY, 100, 30);
                automateButton.setText(level.isAutomated() ? "Stop" : "Automate ($500)");
                automateButton.setVisible(true);
            } else {
                upgradeButton.setVisible(false);
                mineButton.setVisible(false);
                automateButton.setVisible(false);
            }
        }

        // Draw Elevator
        elevator.draw(g, this);

        // Elevator carried money
        g.setColor(Color.RED);
        g.drawString("Elevator $: " + elevator.getCarriedMoney(), elevator.getX(), elevator.getY() + elevator.getHeight() + 20);

        // Total money display
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Money: $" + totalMoney, 20, 30);

        // Position elevator automate button (fixed)
        elevatorAutomateButton.setBounds(elevatorX + elevator.getWidth() + 10, 60, 240, 30);
    }
}

