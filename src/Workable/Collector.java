package Workable;

public class Collector extends Worker implements Workable{

    private int level;
    private String speed;
    private enum State {IDLE, GOING_TO_ELEVATOR, LOADING, GOING_TO_SELL, SELLING}
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
            case IDLE -> state = State.GOING_TO_ELEVATOR;
            case GOING_TO_ELEVATOR -> {
                // Assume instant arrival:
                state = State.LOADING;
            }
            case LOADING -> {
                if (currentLoad < capacity /* && elevator has ore */) {
                    currentLoad++;
                    System.out.println("Collector loaded one crate.");
                } else {
                    state = State.GOING_TO_SELL;
                }
            }
            case GOING_TO_SELL -> {
                // Instant arrival:
                state = State.SELLING;
            }
            case SELLING -> {
                System.out.println("Collector sold " + currentLoad + " crates.");
                currentLoad = 0;
                state = State.IDLE;
                // TODO: add money to player wallet
            }
        }
    }

    @Override
    public void click() {
        if (state == State.IDLE) {
            state = State.GOING_TO_ELEVATOR;
            System.out.println("Collector started trip.");
        }
    }
}
