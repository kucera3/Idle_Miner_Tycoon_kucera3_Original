package Materials;

public class Storage {
    private int crates;
    private final int capacity;

    public Storage(int capacity) {
        this.capacity = capacity;
        this.crates = 0;
    }

    public int getCrates() {
        return crates;
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean addCrate() {
        if (crates < capacity) {
            crates++;
            return true;
        }
        return false;
    }

    public int removeCrates(int amount) {
        int removed = Math.min(amount, crates);
        crates -= removed;
        return removed;
    }

    public boolean isEmpty() {
        return crates == 0;
    }

    public boolean isFull() {
        return crates >= capacity;
    }

    public static class Wallet {
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
}
