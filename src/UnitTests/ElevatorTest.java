package UnitTests;

import Workable.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ElevatorTest {

    private Elevator elevator;
    private List<MineLevel> mineLevels;

    @BeforeEach
    void setUp() {
        Image dummyImage = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
        elevator = new Elevator(0, 0, 10, 10, dummyImage);
        mineLevels = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            MineLevel m = new MineLevel(100 * i);
            m.unlocked = true;
            m.addToBalance(100);
            mineLevels.add(m);
        }
    }

    @Test
    void testElevatorCollectsMoney() throws InterruptedException {
        elevator.startMovingOnce(mineLevels);
        Thread.sleep(1500);  // wait for one cycle
        int collected = elevator.collectCarriedMoney();
        assertTrue(collected > 0, "Elevator should collect money from mines");
    }

    @Test
    void testElevatorStartsMoving() {
        assertFalse(elevator.isMoving(), "Elevator should not be moving initially");
        elevator.startMovingOnce(mineLevels);
        assertTrue(elevator.isMoving(), "Elevator should start moving");
    }
}