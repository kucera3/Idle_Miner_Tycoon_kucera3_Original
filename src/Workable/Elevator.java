package Workable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents an elevator in the game that transports mined resources.
 */
public class Elevator {
    private final Image elevatorImage;
    private int x, y;
    private final int width, height;
    private List<MineLevel> mineLevels;
    private List<Integer> stops = new ArrayList<>();
    private int currentStopIndex = 0;
    private boolean goingDown = false;
    private boolean returningUp = false;
    private boolean moving = false;
    private boolean automated = false;
    private int elevatorCarriedMoney = 0;
    private Component repaintObserver;
    private Timer elevatorTimer;
    private final int MOVE_SPEED = 5;
    private int playerBalance = 0;

    /**
     * Constructs an Elevator instance.
     */
    public Elevator(int x, int y, int width, int height, Image elevatorImage) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.elevatorImage = elevatorImage;
        elevatorTimer = new Timer(20, e -> updateElevator());
    }

    /**
     * Draws the elevator on screen.
     */
    public void draw(Graphics g, Component observer) {
        g.drawImage(elevatorImage, x, y, width, height, observer);
    }

    /**
     * Checks if a click occurred within the bounds of the elevator.
     */
    public boolean isClicked(int clickX, int clickY) {
        return clickX >= x && clickX <= x + width && clickY >= y && clickY <= y + height;
    }

    public void startMovingOnce(List<MineLevel> mineLevels) {
        if (moving) return;
        this.mineLevels = mineLevels;
        this.automated = false;
        this.moving = true;
        this.returningUp = false;
        updateStopsFromMineLevels();
        currentStopIndex = 0;
        goingDown = false;
        elevatorCarriedMoney = 0;
        elevatorTimer.start();
    }

    public void startAutomation(List<MineLevel> mineLevels) {
        if (moving && automated) return;
        this.mineLevels = mineLevels;
        this.automated = true;
        this.moving = true;
        this.returningUp = false;
        updateStopsFromMineLevels();
        currentStopIndex = 0;
        goingDown = false;
        elevatorCarriedMoney = 0;
        elevatorTimer.start();
    }

    public void stop() {
        moving = false;
        automated = false;
        returningUp = false;
        elevatorTimer.stop();
        elevatorCarriedMoney = 0;
    }

    /**
     * Updates the elevator’s list of stops based on unlocked mine levels.
     */
    private void updateStopsFromMineLevels() {
        stops.clear();
        for (MineLevel level : mineLevels) {
            if (level.unlocked) {
                stops.add(level.yPosition);
            }
        }
        stops.add(0);
        Collections.sort(stops);
        y = stops.get(0);
    }

    /**
     * Moves the elevator between stops and handles resource collection logic.
     */
    private void updateElevator() {
        if (!moving || stops.isEmpty()) {
            elevatorTimer.stop();
            moving = false;
            returningUp = false;
            return;
        }

        int targetY = stops.get(currentStopIndex);

        if (y < targetY) {
            y += MOVE_SPEED;
            if (y > targetY) y = targetY;
        } else if (y > targetY) {
            y -= MOVE_SPEED;
            if (y < targetY) y = targetY;
        } else {
            if (!automated && !returningUp) {
                for (MineLevel level : mineLevels) {
                    if (level.unlocked && level.yPosition == targetY) {
                        elevatorCarriedMoney += level.getBalance();
                        level.resetBalance();
                    }
                }

                if (currentStopIndex == stops.size() - 1) {
                    returningUp = true;
                    goingDown = false;
                    currentStopIndex--;
                } else {
                    currentStopIndex++;
                }
            } else if (!automated && returningUp) {
                if (currentStopIndex > 0) {
                    currentStopIndex--;
                } else {
                    playerBalance += elevatorCarriedMoney;
                    elevatorCarriedMoney = 0;
                    elevatorTimer.stop();
                    moving = false;
                    returningUp = false;
                }
            } else if (automated) {
                for (MineLevel level : mineLevels) {
                    if (level.unlocked && level.yPosition == targetY) {
                        elevatorCarriedMoney += level.getBalance();
                        level.resetBalance();
                    }
                }
                if (!goingDown) {
                    if (currentStopIndex < stops.size() - 1) {
                        currentStopIndex++;
                    } else {
                        goingDown = true;
                        currentStopIndex--;
                    }
                } else {
                    if (currentStopIndex > 0) {
                        currentStopIndex--;
                    } else {
                        currentStopIndex = 1;
                        goingDown = false;
                    }
                }
            }
        }

        if (repaintObserver != null) {
            SwingUtilities.invokeLater(() -> repaintObserver.repaint());
        }
    }

    public int collectCarriedMoney() {
        int collected = elevatorCarriedMoney;
        elevatorCarriedMoney = 0;
        return collected;
    }

    public void setRepaintObserver(Component observer) {
        this.repaintObserver = observer;
    }

    // --- Getters and Setters with short descriptions ---

    public boolean isMoving() { return moving; }
    public boolean isAutomated() { return automated; }
    public int getY() { return y; }
    public int getX() { return x; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public Image getElevatorImage() { return elevatorImage; }
    public List<MineLevel> getMineLevels() { return mineLevels; }
    public List<Integer> getStops() { return stops; }
    public int getCurrentStopIndex() { return currentStopIndex; }
    public boolean isGoingDown() { return goingDown; }
    public boolean isReturningUp() { return returningUp; }
    public int getCarriedMoney() { return elevatorCarriedMoney; }
    public Component getRepaintObserver() { return repaintObserver; }
    public Timer getElevatorTimer() { return elevatorTimer; }
    public int getMOVE_SPEED() { return MOVE_SPEED; }
    public int getPlayerBalance() { return playerBalance; }

    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public void setMineLevels(List<MineLevel> mineLevels) { this.mineLevels = mineLevels; }
    public void setStops(List<Integer> stops) { this.stops = stops; }
    public void setCurrentStopIndex(int currentStopIndex) { this.currentStopIndex = currentStopIndex; }
    public void setGoingDown(boolean goingDown) { this.goingDown = goingDown; }
    public void setReturningUp(boolean returningUp) { this.returningUp = returningUp; }
    public void setMoving(boolean moving) { this.moving = moving; }
    public void setAutomated(boolean automated) { this.automated = automated; }
    public void setElevatorCarriedMoney(int elevatorCarriedMoney) { this.elevatorCarriedMoney = elevatorCarriedMoney; }
    public void setElevatorTimer(Timer elevatorTimer) { this.elevatorTimer = elevatorTimer; }
    public void setPlayerBalance(int balance) { this.playerBalance = balance; }
}




