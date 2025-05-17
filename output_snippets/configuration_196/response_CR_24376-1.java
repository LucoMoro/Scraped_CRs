<<Beginning of snippet n. 0>>
import android.content.Context;
import android.view.WindowManager;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ScreenManagerTest {
    private Context context;
    private WindowManager windowManager;

    @Before
    public void setUp() {
        context = Mockito.mock(Context.class);
        windowManager = Mockito.mock(WindowManager.class);
    }

    @Test
    public void testScreenOn() {
        when(context.getSystemService(Context.WINDOW_SERVICE)).thenReturn(windowManager);
        ScreenManager screenManager = new ScreenManager(context);
        screenManager.turnScreenOn();

        verify(windowManager).setFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON, 
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
    }

    @Test
    public void testScreenOff() {
        when(context.getSystemService(Context.WINDOW_SERVICE)).thenReturn(windowManager);
        ScreenManager screenManager = new ScreenManager(context);
        screenManager.turnScreenOff();

        verify(windowManager).clearFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
    }
}
<<End of snippet n. 0>>