package Workable;

public class ElevatorGuy extends Worker implements Workable {

    private int level = 1;
    private String speed;
    private enum State {IDLE, GOING_DOWN, LOADING, GOING_UP, UNLOADING}
    private State state = State.IDLE;
    private int capacity = 10;
    private int currentLoad = 0;
    private int progress = 0;
    private int progressToMove = 100;

    @Override
    public void work() {

    }

    @Override
    public void automate() {

    }

    @Override
    public boolean isAutomated() {
        return false;
    }

    @Override
    public void doWork() {
        if (!automated) return;

        progress++;
        if (progress < progressToMove) return;

        progress = 0;

        switch (state) {
            case IDLE -> state = State.GOING_DOWN;
            case GOING_DOWN -> {
                // Logic to go down one step or arrive
                // For simplicity, assume instant arrival:
                state = State.LOADING;
            }
            case LOADING -> {
                if (currentLoad < capacity /* && shaft has ore */) {
                    currentLoad++; // pick one crate
                    System.out.println("Elevator loaded one crate.");
                } else {
                    state = State.GOING_UP;
                }
            }
            case GOING_UP -> {
                // Assume instant arrival up:
                state = State.UNLOADING;
            }
            case UNLOADING -> {
                System.out.println("Elevator unloaded " + currentLoad + " crates.");
                currentLoad = 0;
                state = State.IDLE;
            }
        }
    }

    @Override
    public void click() {
        if (state == State.IDLE) {
            state = State.GOING_DOWN;
            System.out.println("Elevator started trip.");
        }
    }
}
