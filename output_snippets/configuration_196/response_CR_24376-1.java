<<Beginning of snippet n. 0>>
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class ScreenStateTest {

    @Test
    public void testScreenOn() {
        ScreenStateListener listener = mock(ScreenStateListener.class);
        ScreenManager screenManager = new ScreenManager(listener);

        screenManager.onScreenOn();
        
        verify(listener).onScreenStateChange(true);
        assertTrue(screenManager.isScreenOn());
    }

    @Test
    public void testScreenOff() {
        ScreenStateListener listener = mock(ScreenStateListener.class);
        ScreenManager screenManager = new ScreenManager(listener);
        
        screenManager.onScreenOff();
        
        verify(listener).onScreenStateChange(false);
        assertFalse(screenManager.isScreenOn());
    }

    @Test
    public void testRapidScreenToggle() {
        ScreenStateListener listener = mock(ScreenStateListener.class);
        ScreenManager screenManager = new ScreenManager(listener);

        screenManager.onScreenOn();
        verify(listener).onScreenStateChange(true);
        
        screenManager.onScreenOff();
        verify(listener).onScreenStateChange(false);
        
        screenManager.onScreenOn();
        verify(listener).onScreenStateChange(true);
        
        assertTrue(screenManager.isScreenOn());
    }
}
<<End of snippet n. 0>>