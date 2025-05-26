package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.concurrent.*;
import Workable.*;
import Materials.*;

public class IdleMinerUI {
    private JFrame frame;
    private JLabel statusLabel;
    private JLabel moneyLabel;
    private JButton mineButton;
    private int money = 0;
    private ScheduledExecutorService executor;
    private Miner miner = new Miner();
    private Wallet wallet = new Wallet();
    public IdleMinerUI() {
        executor = Executors.newScheduledThreadPool(2);
        createUI();
    }
    private void upgradeMiner(ActionEvent e) {
        if (wallet.spend(50)) {
            miner.upgrade();
            moneyLabel.setText("Money: $" + wallet.getBalance());
            levelLabel.setText("Miner Level: " + miner.getLevel());
            statusLabel.setText("Miner upgraded! Mining time: " + miner.getMiningTime() + "s");
        } else {
            statusLabel.setText("Not enough money to upgrade!");
        }
    }

    private void createUI() {
        frame = new JFrame("Idle Miner Tycoon");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        statusLabel = new JLabel("Status: Idle");
        moneyLabel = new JLabel("Money: $0");
        topPanel.add(statusLabel);
        topPanel.add(moneyLabel);

        JPanel centerPanel = new JPanel();
        mineButton = new JButton("Mine Gold");
        mineButton.addActionListener(this::startMining);
        centerPanel.add(mineButton);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(centerPanel, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private void startMining(ActionEvent e) {
        mineButton.setEnabled(false);
        statusLabel.setText("Status: Mining...");

        executor.schedule(() -> {
            money += 10;
            SwingUtilities.invokeLater(() -> {
                statusLabel.setText("Status: Idle");
                moneyLabel.setText("Money: $" + money);
                mineButton.setEnabled(true);
            });
        }, 3, TimeUnit.SECONDS); // Simulate 3 seconds of mining
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(IdleMinerUI::new);
    }
}

