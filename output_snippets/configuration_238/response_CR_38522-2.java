//<Beginning of snippet n. 0>


import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.test.InstrumentationTestCase;

public class ApplicationTest extends InstrumentationTestCase {

    private boolean isPortraitLandscapeSupported;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        isPortraitLandscapeSupported = checkOrientationSupport();
    }

    public void testApplicationLifecycle() {
        if (!isPortraitLandscapeSupported) {
            System.out.println("Skipping test: Device does not support both orientations.");
            return;
        }

        assertTrue(mockApp.isConstructorCalled);
        assertTrue(mockApp.isOnCreateCalled);

        runTestOnUiThread(new Runnable() {
            public void run() {
                OrientationTestUtils.toggleOrientation(activity);
            }
        });
    }

    private boolean checkOrientationSupport() {
        // Implement logic to determine if the device supports both orientations
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
    
    private boolean isPortraitLandscapeSupported;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        isPortraitLandscapeSupported = checkOrientationSupport();
    }

    public void testDialogLifecycle() {
        if (!isPortraitLandscapeSupported) {
            System.out.println("Skipping test: Device does not support both orientations.");
            return;
        }

        assertFalse(d.isOnSaveInstanceStateCalled);
        assertFalse(TestDialog.isOnRestoreInstanceStateCalled);

        try {
            OrientationTestUtils.toggleOrientationSync(mActivity, mInstrumentation);
        } catch (Exception e) {
            fail("Failed to toggle orientation: " + e.getMessage());
        }

        assertTrue(d.isOnSaveInstanceStateCalled);
    }

    private boolean checkOrientationSupport() {
        // Implement logic to determine if the device supports both orientations
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT || 
               getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }
}

//<End of snippet n. 1>