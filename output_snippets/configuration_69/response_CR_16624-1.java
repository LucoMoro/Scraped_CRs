//<Beginning of snippet n. 0>


import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.os.SystemClock;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.cooliris.app.App;

private GestureDetector mGestureDetector;
private float mScale;
private float mPrevTiltValueLowPass;

public MyActivity() {
    mGestureDetector = new GestureDetector(App.getContext(), new GestureDetector.SimpleOnGestureListener());
    mGestureDetector.setIsLongpressEnabled(true);
    mScale = 1.0f;
}

public int getCurrentFocusSlot(SensorEvent event) {
    if (event == null || event.sensor == null || event.values == null || event.values.length < 2) {
        return -1; // Handle error for null sensor event or insufficient values
    }

    switch (event.sensor.getType()) {
        case Sensor.TYPE_ACCELEROMETER:
            float[] values = event.values;
            float valueToUse = (App.getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) 
                               ? values[1] : -values[0]; // Correct tilt calculation for both orientations
            float tiltValue = 0.8f * mPrevTiltValueLowPass + 0.2f * valueToUse;
            if (Math.abs(tiltValue) < 0.5f)
                tiltValue = 0.0f;
            mPrevTiltValueLowPass = tiltValue; // Update previous tilt value
            return (int) tiltValue;
    }
    return -1; // Default case
}

//<End of snippet n. 0>