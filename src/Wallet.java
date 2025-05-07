import java.util.Observable;

public class Wallet extends Observable {
    private double money;
    public void add(double amt) {
        money += amt;
        setChanged();
        notifyObservers(money);
    }
}
