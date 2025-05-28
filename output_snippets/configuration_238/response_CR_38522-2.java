//<Beginning of snippet n. 0>
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.test.InstrumentationTestCase;

public class ApplicationTest extends InstrumentationTestCase {
    private boolean isOrientationSupported;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        isOrientationSupported = checkDeviceOrientationSupport();
    }

    public void testApplication() {
        assertTrue(mockApp.isConstructorCalled);
        assertTrue(mockApp.isOnCreateCalled);

        if (isOrientationSupported) {
            runTestOnUiThread(new Runnable() {
                public void run() {
                    OrientationTestUtils.toggleOrientation(activity);
                }
            });
        }
    }

    private boolean checkDeviceOrientationSupport() {
        // Logic to check if the device supports both orientatons (portrait and landscape)
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ||
               getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
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
    private boolean isOrientationSupported;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        isOrientationSupported = checkDeviceOrientationSupport();
    }

    public void testDialog() {
        assertFalse(d.isOnSaveInstanceStateCalled);
        assertFalse(TestDialog.isOnRestoreInstanceStateCalled);

        if (isOrientationSupported) {
            OrientationTestUtils.toggleOrientationSync(mActivity, mInstrumentation);
            assertTrue(d.isOnSaveInstanceStateCalled);
        }
    }

    private boolean checkDeviceOrientationSupport() {
        // Logic to check if the device supports both orientations (portrait and landscape)
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ||
               getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }
}
//<End of snippet n. 1>