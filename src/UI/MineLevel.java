package UI;

public class MineLevel {
    public int yPosition;
    public boolean unlocked;
    public int moneyGenerated;

    public MineLevel(int yPosition) {
        this.yPosition = yPosition;
        this.unlocked = false;
        this.moneyGenerated = 10; // Default income per level
    }
}
