package UI;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

public class GamePanel extends JPanel {
    private Image minerImage;

    public GamePanel() {
        // Load image from resources or local file
        minerImage = new ImageIcon(getClass().getResource("/miner.png")).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw miner at position (100, 100)
        g.drawImage(minerImage, 100, 100, this);
    }
}

