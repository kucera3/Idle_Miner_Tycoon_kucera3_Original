import Materials.*;
import Workable.*;

public class GameManager {


    Storage shaftStorage = new Storage(50);
    Wallet wallet = new Wallet(0);

    Miner miner = new Miner(shaftStorage);
    ElevatorGuy elevator = new ElevatorGuy(shaftStorage, 20);
    Collector collector = new Collector(elevator.getElevatorStorage(), wallet);

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
