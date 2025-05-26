

public class Wallet {
    private long balance;

    public Wallet() {
        this.balance = 0;
    }

    public Wallet(long startingBalance) {
        this.balance = startingBalance;
    }

    public long getBalance() {
        return balance;
    }

    public void add(long amount) {
        if (amount < 0) return;
        balance += amount;
    }

    public boolean spend(long amount) {
        if (amount > balance || amount < 0) return false;
        balance -= amount;
        return true;
    }

    public boolean canAfford(long amount) {
        return balance >= amount;
    }
}
