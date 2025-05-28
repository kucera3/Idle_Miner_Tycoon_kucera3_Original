package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.concurrent.*;
import Workable.Miner;  // Assuming you have this package
import Materials.Wallet; // Assuming you have this package

public class IdleMinerUI {
    private JFrame frame;
    private JLabel statusLabel;
    private JLabel levelLabel;
    private JLabel moneyLabel;
    private JButton mineButton;
    private JButton upgradeButton;

    private ScheduledExecutorService executor;
    private Miner miner = new Miner();
    private Wallet wallet = new Wallet();

    public IdleMinerUI() {
        executor = Executors.newScheduledThreadPool(2);
        createUI();
        updateLabels();
    }

    private void createUI() {
        frame = new JFrame("Idle Miner Tycoon");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(3, 1));

        statusLabel = new JLabel("Status: Idle");
        moneyLabel = new JLabel();
        levelLabel = new JLabel();
        topPanel.add(statusLabel);
        topPanel.add(moneyLabel);
        topPanel.add(levelLabel);

        JPanel centerPanel = new JPanel();
        mineButton = new JButton("Mine Gold");
        mineButton.addActionListener(this::startMining);

        upgradeButton = new JButton("Upgrade Miner ($50)");
        upgradeButton.addActionListener(this::upgradeMiner);

        centerPanel.add(mineButton);
        centerPanel.add(upgradeButton);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(centerPanel, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private void updateLabels() {
        moneyLabel.setText("Money: $" + wallet.getBalance());
        levelLabel.setText("Miner Level: " + miner.getLevel());
    }

    private void startMining(ActionEvent e) {
        mineButton.setEnabled(false);
        upgradeButton.setEnabled(false);
        statusLabel.setText("Status: Mining...");

        // Simulate mining based on miner's mining time
        int miningTimeSeconds = miner.getMiningTime();

        executor.schedule(() -> {
            // Add money to wallet based on miner's income per mining cycle
            wallet.add(miner.getIncome());

            SwingUtilities.invokeLater(() -> {
                statusLabel.setText("Status: Idle");
                updateLabels();
                mineButton.setEnabled(true);
                upgradeButton.setEnabled(true);
            });
        }, miningTimeSeconds, TimeUnit.SECONDS);
    }

    private void upgradeMiner(ActionEvent e) {
        if (wallet.spend(50)) {
            miner.upgrade();
            statusLabel.setText("Miner upgraded! Mining time: " + miner.getMiningTime() + "s");
            updateLabels();
        } else {
            statusLabel.setText("Not enough money to upgrade!");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(IdleMinerUI::new);
    }
}

