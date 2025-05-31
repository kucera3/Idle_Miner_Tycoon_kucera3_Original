package UI;

import javax.swing.*;

public class GameWindow extends JFrame {

    public GameWindow() {
        initializeWindow();
        addGamePanelWithScroll();
    }

    private void initializeWindow() {
        setTitle("Idle Miner Tycoon");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        setLayout(null);
    }

    private void addGamePanelWithScroll() {
        GamePanel gamePanel = new GamePanel();
        JScrollPane scrollPane = new JScrollPane(gamePanel);

        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBounds(0, 0, 980, 580);
        scrollPane.getVerticalScrollBar().setUnitIncrement(12);

        add(scrollPane);
    }
}

