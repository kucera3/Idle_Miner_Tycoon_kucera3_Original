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


}
