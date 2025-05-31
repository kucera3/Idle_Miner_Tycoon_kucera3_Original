package UnitTests;
import Workable.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MineLevelTest {

    private MineLevel mine;

    @BeforeEach
    void setUp() {
        mine = new MineLevel(100);
        mine.unlocked = true;
    }

    @Test
    void testInitialLevelIsOne() {
        assertEquals(1, mine.getLevel(), "Initial level should be 1");
    }

    @Test
    void testUpgradeIncreasesLevel() {
        mine.upgradeLevel();
        assertEquals(2, mine.getLevel(), "Level should increase to 2 after one upgrade");
    }

    @Test
    void testAddToBalance() {
        mine.addToBalance(200);
        assertEquals(200, mine.getBalance(), "Balance should reflect added amount");
    }

    @Test
    void testMiningTimeReducesWithLevel() {
        int timeAtLevel1 = mine.getMiningTime();
        mine.upgradeLevel(); // level 2
        int timeAtLevel2 = mine.getMiningTime();
        assertTrue(timeAtLevel2 < timeAtLevel1, "Mining time should reduce with level");
    }
}
