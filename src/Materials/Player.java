package Materials;

public class Player {
    private Wallet wallet;

    public Player() {
        this.wallet = new Wallet(1000); // starting money
    }

    public Wallet getWallet() {
        return wallet;
    }
}
