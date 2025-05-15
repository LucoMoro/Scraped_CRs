<<Beginning of snippet n. 0>>
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private BroadcastReceiver screenReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeReceiver();
    }

    private void initializeReceiver() {
        screenReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (Intent.ACTION_SCREEN_ON.equals(intent.getAction())) {
                    // Handle screen on
                    handleScreenOn();
                } else if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
                    // Handle screen off
                    handleScreenOff();
                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(screenReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(screenReceiver);
    }

    private void handleScreenOn() {
        // Logic for handling screen on
    }

    private void handleScreenOff() {
        // Logic for handling screen off
    }

    // Test cases for screen on/off events
    public void testScreenOnOff() {
        // Implement test cases to verify behavior on screen on/off transitions
    }
}
//<End of snippet n. 0>>