//<Beginning of snippet n. 0>

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.test.InstrumentationTestCase;

public class ApplicationTest extends InstrumentationTestCase {

    public void testApplicationLifecycle() {
        MockApplication mockApp = new MockApplication();
        assertTrue(mockApp.isConstructorCalled);
        assertTrue(mockApp.isOnCreateCalled);

        if (isOrientationChangeSupported()) {
            runTestOnUiThread(new Runnable() {
                public void run() {
                    OrientationTestUtils.toggleOrientation(activity);
                }
            });
        } else {
            skipTest("Device does not support both orientations.");
        }
    }

    private boolean isOrientationChangeSupported() {
        return hasSystemFeature(Context.FEATURE_SENSOR_ORIENTATION);
    }
}

//<End of snippet n. 0>

//<Beginning of snippet n. 1>

import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;

public class DialogTest extends InstrumentationTestCase {

    public void testDialogLifecycle() {
        TestDialog d = new TestDialog();

        assertFalse(d.isOnSaveInstanceStateCalled);
        assertFalse(TestDialog.isOnRestoreInstanceStateCalled);

        if (isOrientationChangeSupported()) {
            OrientationTestUtils.toggleOrientationSync(mActivity, mInstrumentation);
            assertTrue(d.isOnSaveInstanceStateCalled);
        } else {
            skipTest("Device does not support both orientations.");
        }
    }

    private boolean isOrientationChangeSupported() {
        return hasSystemFeature(Context.FEATURE_SENSOR_ORIENTATION);
    }
}

//<End of snippet n. 1>