package Materials;

public class Wallet {
    private int balance;

    public Wallet(int initialAmount) {
        this.balance = initialAmount;
    }

    public Wallet() {

    }

    public int getBalance() {
        return balance;
    }

    public boolean canSpend(long amount) {
        return balance >= amount;
    }

    public boolean spend(long amount) {
        if (canSpend(amount)) {
            balance -= amount;
            return true;
        }
        return false;
    }

    public void add(long amount) {
        balance += amount;
    }
}