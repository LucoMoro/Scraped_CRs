<<Beginning of snippet n. 0>>
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ScreenManager {
    private boolean isScreenOn = false;

    public void turnScreenOn() {
        isScreenOn = true;
    }

    public void turnScreenOff() {
        isScreenOn = false;
    }

    public boolean isScreenOn() {
        return isScreenOn;
    }
}

class ScreenManagerTest {

    @Test
    void testTurnScreenOn() {
        ScreenManager screenManager = new ScreenManager();
        screenManager.turnScreenOn();
        assertTrue(screenManager.isScreenOn());
    }

    @Test
    void testTurnScreenOff() {
        ScreenManager screenManager = new ScreenManager();
        screenManager.turnScreenOn(); // Incase we want to test the off state after it's turned on
        screenManager.turnScreenOff();
        assertFalse(screenManager.isScreenOn());
    }

    @Test
    void testImmediateToggle() {
        ScreenManager screenManager = new ScreenManager();
        screenManager.turnScreenOn();
        assertTrue(screenManager.isScreenOn());
        screenManager.turnScreenOff();
        assertFalse(screenManager.isScreenOn());
    }

    @Test
    void testDelayedTurnOn() throws InterruptedException {
        ScreenManager screenManager = new ScreenManager();
        Thread.sleep(100);
        screenManager.turnScreenOn();
        assertTrue(screenManager.isScreenOn());
    }

    @Test
    void testDelayedTurnOff() throws InterruptedException {
        ScreenManager screenManager = new ScreenManager();
        screenManager.turnScreenOn();
        Thread.sleep(100);
        screenManager.turnScreenOff();
        assertFalse(screenManager.isScreenOn());
    }

    @Test
    void testRapidToggling() {
        ScreenManager screenManager = new ScreenManager();
        for (int i = 0; i < 5; i++) {
            screenManager.turnScreenOn();
            assertTrue(screenManager.isScreenOn());
            screenManager.turnScreenOff();
            assertFalse(screenManager.isScreenOn());
        }
    }

    @Test
    void testInvalidState() {
        ScreenManager screenManager = new ScreenManager();
        assertFalse(screenManager.isScreenOn());
        assertDoesNotThrow(() -> screenManager.turnScreenOff());
    }
}
<<End of snippet n. 0>>