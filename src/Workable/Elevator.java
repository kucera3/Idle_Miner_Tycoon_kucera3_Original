package Workable;

import UI.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Elevator {
    private Image elevatorImage;
    private int x, y;
    private int width, height;

    private int currentStopIndex = 0;
    private boolean moving = false;
    private boolean automated = false;

    private List<MineLevel> mineLevels; // reference to levels to collect money from

    private int elevatorCarriedMoney = 0;

    private Timer elevatorTimer;

    private final int MOVE_SPEED = 5;  // pixels per timer tick

    public Elevator(int x, int y, int width, int height, Image elevatorImage) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.elevatorImage = elevatorImage;

        elevatorTimer = new Timer(20, e -> updateElevator());
    }

    public void draw(Graphics g, Component observer) {
        g.drawImage(elevatorImage, x, y, width, height, observer);
    }

    public boolean isClicked(int clickX, int clickY) {
        return clickX >= x && clickX <= x + width && clickY >= y && clickY <= y + height;
    }

    public int getY() {
        return y;
    }

    public int getCarriedMoney() {
        return elevatorCarriedMoney;
    }

    public void startMovingOnce(List<MineLevel> mineLevels) {
        if (moving) return; // prevent starting if already moving
        this.mineLevels = mineLevels;
        this.automated = false;
        this.moving = true;
        this.currentStopIndex = 0;
        elevatorTimer.start();
    }

    public void startAutomation(List<MineLevel> mineLevels) {
        if (moving && automated) return; // already running automated
        this.mineLevels = mineLevels;
        this.automated = true;
        this.moving = true;
        this.currentStopIndex = 0;
        elevatorTimer.start();
    }

    public void stop() {
        moving = false;
        automated = false;
        elevatorTimer.stop();
        elevatorCarriedMoney = 0;
    }

    private void updateElevator() {
        if (mineLevels == null || mineLevels.isEmpty()) {
            elevatorTimer.stop();
            moving = false;
            return;
        }

        List<Integer> stops = getUnlockedYPositions();

        if (stops.isEmpty()) {
            elevatorTimer.stop();
            moving = false;
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
            // Elevator reached stop
            for (MineLevel level : mineLevels) {
                if (level.unlocked && level.yPosition == targetY) {
                    elevatorCarriedMoney += level.getBalance();
                    level.resetBalance();
                }
            }

            if (!automated) {
                // One run done, stop elevator
                elevatorTimer.stop();
                moving = false;
                return;
            }

            // Automated mode: cycle stops up and down
            if (currentStopIndex == stops.size() - 1) {
                // At bottom, reverse direction
                reverseStops(stops);
            }
            currentStopIndex++;
            if (currentStopIndex >= stops.size()) currentStopIndex = 0;
        }
    }

    // Helper method: get Y positions of unlocked levels + ground level (0)
    private List<Integer> getUnlockedYPositions() {
        java.util.ArrayList<Integer> stops = new java.util.ArrayList<>();
        for (MineLevel level : mineLevels) {
            if (level.unlocked) {
                stops.add(level.yPosition);
            }
        }
        stops.add(0);  // add ground level at 0 so elevator can return
        stops.sort(Integer::compareTo);
        return stops;
    }

    // This reverses the stops list so elevator goes back up after reaching bottom
    private void reverseStops(List<Integer> stops) {
        java.util.Collections.reverse(stops);
        currentStopIndex = 0;
    }

    public boolean isMoving() {
        return moving;
    }

    public boolean isAutomated() {
        return automated;
    }

    // Setters and getters for position (optional)
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public int getX() { return x; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
}




