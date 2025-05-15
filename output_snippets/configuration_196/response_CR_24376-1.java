//<Beginning of snippet n. 0>
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

public class ScreenReceiverActivity extends AppCompatActivity {

    private BroadcastReceiver screenReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_SCREEN_ON.equals(intent.getAction())) {
                // Handle screen on
            } else if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
                // Handle screen off
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Register receiver
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(screenReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister receiver
        unregisterReceiver(screenReceiver);
    }
}

// Unit test class
@RunWith(AndroidJUnit4.class)
public class ScreenReceiverTest {

    private ScreenReceiverActivity activity;
    private Intent screenOnIntent;
    private Intent screenOffIntent;

    @Before
    public void setUp() {
        activity = new ScreenReceiverActivity();
        screenOnIntent = new Intent(Intent.ACTION_SCREEN_ON);
        screenOffIntent = new Intent(Intent.ACTION_SCREEN_OFF);
    }

    @Test
    public void testScreenOnReceiver() {
        activity.onReceive(activity, screenOnIntent);
        // Validate handling of screen on
    }

    @Test
    public void testScreenOffReceiver() {
        activity.onReceive(activity, screenOffIntent);
        // Validate handling of screen off
    }

    @After
    public void tearDown() {
        activity.onDestroy();
    }
}
//<End of snippet n. 0>