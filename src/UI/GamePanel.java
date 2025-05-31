package UI;

import Builder.UIButtonBuilder;
import Workable.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * GamePanel manages the visual components of the mining game.
 * It handles rendering, interactions, and dynamic updates of the game state.
 */
public class GamePanel extends JPanel {

    private Image minerImage;
    private Image miningMinerImage;
    private Image elevatorImage;
    private Image backgroundImage;

    private int totalMoney = 0;
    int[] unlockCost = {1000};

    private final List<MineLevel> mineLevels = new ArrayList<>();
    private final List<JButton> upgradeButtons = new ArrayList<>();
    private final List<JButton> mineButtons = new ArrayList<>();
    private final List<JButton> automateButtons = new ArrayList<>();
    Font smallFont = new Font("Arial", Font.PLAIN, 8);

    private Elevator elevator;
    private JButton elevatorButton;
    private JButton unlockMineButton;

    private final int elevatorX = 360;
    private final int MINE_SECTION_HEIGHT = 180;

    /**
     * Constructs the GamePanel, initializes mine levels,
     * buttons, and the elevator component.
     */
    public GamePanel() {
        setLayout(null);

        minerImage = new ImageIcon(getClass().getResource("/miner.png")).getImage();
        miningMinerImage = new ImageIcon(getClass().getResource("/mining_miner.png")).getImage();
        elevatorImage = new ImageIcon(getClass().getResource("/elevator.png")).getImage();
        backgroundImage = new ImageIcon(getClass().getResource("/background.jpg")).getImage();

        int initialY = 100;
        int spacing = 150;

        for (int i = 0; i < 20; i++) {
            mineLevels.add(new MineLevel(initialY + i * spacing));
        }
        mineLevels.get(0).unlocked = true;

        elevator = new Elevator(elevatorX, 0, elevatorImage.getWidth(null), elevatorImage.getHeight(null), elevatorImage);
        elevator.setRepaintObserver(this);

        setupElevatorButton();
        setupUnlockMineButton();
        setupMineLevelButtons();

        updatePreferredSize();
    }

    /**
     * Initializes and adds the elevator activation button.
     */
    private void setupElevatorButton() {
        elevatorButton = new UIButtonBuilder("Elevator")
                .setBounds(elevatorX + elevator.getWidth() + 10, 20, 140, 30)
                .build();
        elevatorButton.addActionListener(e -> {
            if (!elevator.isMoving()) {
                elevator.startMovingOnce(mineLevels);
            } else {
                System.out.println("Elevator already moving.");
            }
        });
        add(elevatorButton);
    }

    /**
     * Initializes and adds the unlock mine button.
     */
    private void setupUnlockMineButton() {
        unlockMineButton = new JButton("Unlock Mine ($" + unlockCost[0] + ")");
        unlockMineButton.setBounds(elevatorX + elevator.getWidth() + 10, 60, 160, 30);
        unlockMineButton.addActionListener(e -> {
            if (totalMoney >= unlockCost[0]) {
                boolean unlockedOne = false;
                for (MineLevel mine : mineLevels) {
                    if (!mine.unlocked) {
                        mine.unlocked = true;
                        totalMoney -= unlockCost[0];
                        unlockCost[0] += 1200;
                        unlockMineButton.setText("Unlock Mine ($" + unlockCost[0] + ")");
                        unlockedOne = true;
                        updatePreferredSize();
                        repaint();
                        break;
                    }
                }
                if (!unlockedOne) {
                    JOptionPane.showMessageDialog(this, "No more mines to unlock!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Not enough money to unlock mine!");
            }
        });
        add(unlockMineButton);
    }

    /**
     * Initializes and adds upgrade, mine, and automate buttons for each mine level.
     */
    private void setupMineLevelButtons() {
        for (int i = 0; i < mineLevels.size(); i++) {
            MineLevel level = mineLevels.get(i);
            final int index = i;

            JButton upgradeButton = new UIButtonBuilder("Upgrade").build();
            upgradeButton.setFont(smallFont);
            upgradeButton.addActionListener(e -> {
                MineLevel lvl = mineLevels.get(index);
                int cost = (int) (30 * Math.pow(1.5, lvl.getLevel()));
                if (totalMoney >= cost) {
                    totalMoney -= cost;
                    lvl.upgradeLevel();
                    repaint();
                } else {
                    JOptionPane.showMessageDialog(this, "Not enough money to upgrade!");
                }
            });
            upgradeButtons.add(upgradeButton);
            add(upgradeButton);

            JButton mineButton = new UIButtonBuilder("Mine").build();
            mineButton.setFont(smallFont);
            mineButton.addActionListener(e -> {
                MineLevel lvl = mineLevels.get(index);
                if (!lvl.unlocked || lvl.isMining()) return;

                lvl.setMining(true);
                repaint();
                new Timer(1000, evt -> {
                    int moneyFromElevator = elevator.collectCarriedMoney();
                    if (moneyFromElevator > 0) {
                        totalMoney += moneyFromElevator;
                        repaint();
                    }
                }).start();

                Timer mineTimer = new Timer(lvl.getMiningTime(), ev -> {
                    int moneyFromElevator = elevator.collectCarriedMoney();
                    if (moneyFromElevator > 0) {
                        totalMoney += moneyFromElevator;
                        repaint();
                    }
                    lvl.addToBalance(lvl.moneyGenerated);
                    lvl.setMining(false);
                    repaint();
                    ((Timer) ev.getSource()).stop();
                });
                mineTimer.setRepeats(false);
                mineTimer.start();
            });
            mineButtons.add(mineButton);
            add(mineButton);

            JButton automateButton = new UIButtonBuilder("Automate ($500)").build();
            automateButton.setFont(smallFont);
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
                        automateButton.setText("Stop");
                    } else {
                        JOptionPane.showMessageDialog(this, "Not enough money to automate! Cost: $500");
                    }
                } else {
                    lvl.stopAutomation();
                    lvl.setMining(false);
                    automateButton.setText("Automate ($500)");
                }
                repaint();
            });
            automateButtons.add(automateButton);
            add(automateButton);
        }
    }

    /**
     * Adjusts the preferred size of the panel based on the number of unlocked mine levels.
     */
    public void updatePreferredSize() {
        int unlockedCount = (int) mineLevels.stream().filter(m -> m.unlocked).count();
        int height = unlockedCount * MINE_SECTION_HEIGHT + 300;
        setPreferredSize(new Dimension(400, height));
        revalidate();
    }

    /**
     * Paints the component, rendering the background, mine levels,
     * elevator, and monetary information.
     * @param g the Graphics context
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(new Color(230, 230, 230));
        g.fillRect(0, 0, getWidth(), getHeight());
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

        int visibleMineIndex = 0;
        for (int i = 0; i < mineLevels.size(); i++) {
            MineLevel level = mineLevels.get(i);
            JButton upgradeButton = upgradeButtons.get(i);
            JButton mineButton = mineButtons.get(i);
            JButton automateButton = automateButtons.get(i);

            if (level.unlocked) {
                int yPosition = visibleMineIndex * MINE_SECTION_HEIGHT + 240;
                level.yPosition = yPosition;

                int minerWidth = 100;
                int minerHeight = 120;
                Image img = level.isMining() ? miningMinerImage : minerImage;
                g.drawImage(img, 100, yPosition, minerWidth, minerHeight, this);

                g.setColor(Color.WHITE);
                String text = "Balance: $" + level.getBalance() + "  Level: " + level.getLevel();
                g.drawString(text, 100, yPosition + minerHeight + 15);

                int buttonY = yPosition + minerHeight + 20;
                int upgradeCost = (int) (30 * Math.pow(1.5, level.getLevel()));

                upgradeButton.setText("Upgrade ($" + upgradeCost + ")");
                upgradeButton.setBounds(120, buttonY, 100, 30);
                upgradeButton.setVisible(true);

                mineButton.setBounds(230, buttonY, 100, 30);
                mineButton.setVisible(true);

                automateButton.setBounds(10, buttonY, 100, 30);
                automateButton.setText(level.isAutomated() ? "Stop" : "Automate ($500)");
                automateButton.setVisible(true);

                visibleMineIndex++;
            } else {
                upgradeButton.setVisible(false);
                mineButton.setVisible(false);
                automateButton.setVisible(false);
            }
        }

        elevator.draw(g, this);

        g.setColor(Color.WHITE);
        g.drawString("Elevator $: " + elevator.getCarriedMoney(), elevator.getX(), elevator.getY() + elevator.getHeight() + 20);

        g.setColor(Color.WHITE);
        g.drawString("Total Money: $" + totalMoney, 20, 20);
    }
}

