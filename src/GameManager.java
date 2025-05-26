import Workable.*;

public class GameManager {
    private Wallet wallet = new Wallet(100); // Start with $100

    public void sellCrates(int amount) {
        long valuePerCrate = 10; // Example value
        wallet.add(amount * valuePerCrate);
        System.out.println("Sold " + amount + " crates for $" + (amount * valuePerCrate));
    }

    public boolean upgradeMiner(Miner miner) {
        long upgradeCost = 50;
        if (wallet.spend(upgradeCost)) {
            miner.setLevel(miner.getLevel() + 1);
            System.out.println("Miner upgraded to level " + miner.getLevel());
            return true;
        } else {
            System.out.println("Not enough money to upgrade miner.");
            return false;
        }
    }

    public long getMoney() {
        return wallet.getBalance();
    }
}
