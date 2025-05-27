package UI;

import javax.swing.JFrame;

public class GameWindow extends JFrame {
    public GameWindow() {
        setTitle("Idle Miner Tycoon Replica");
        setSize(800, 600); // window size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // center window
        setResizable(false);
        add(new GamePanel());
    }
}
