```
//<Beginning of snippet n. 0>
public class ScreenManager {

    private boolean isScreenOn;

    public ScreenManager() {
        this.isScreenOn = false;
    }

    public void turnScreenOn() {
        this.isScreenOn = true;
        // Notify observers or perform action when the screen is turned on
    }

    public void turnScreenOff() {
        this.isScreenOn = false;
        // Notify observers or perform action when the screen is turned off
    }

    public boolean isScreenOn() {
        return this.isScreenOn;
    }
}

// Unit Tests
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

public class ScreenManagerTest {

    private ScreenManager screenManager;

    @BeforeEach
    public void setUp() {
        screenManager = new ScreenManager();
    }

    @Test
    public void testTurnScreenOn() {
        screenManager.turnScreenOn();
        assertTrue(screenManager.isScreenOn(), "Screen should be on");
    }

    @Test
    public void testTurnScreenOff() {
        screenManager.turnScreenOn();
        screenManager.turnScreenOff();
        assertFalse(screenManager.isScreenOn(), "Screen should be off");
    }

    @Test
    public void testScreenStateInitial() {
        assertFalse(screenManager.isScreenOn(), "Screen should be initially off");
    }
}
//<End of snippet n. 0>