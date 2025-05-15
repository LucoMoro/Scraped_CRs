//<Beginning of snippet n. 0>
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.test.InstrumentationTestCase;

public class ApplicationTest extends InstrumentationTestCase {
    private boolean isOrientationSupported;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        isOrientationSupported = getInstrumentation().getTargetContext().getResources().getConfiguration().smallestScreenWidthDp >= 600;

        if (!isOrientationSupported) {
            // Skip tests if the device does not support both orientations
            fail("Test skipped: Device does not support both orientations");
        }
    }

    public void testOrientationToggle() throws Throwable {
        assertTrue(mockApp.isConstructorCalled);
        assertTrue(mockApp.isOnCreateCalled);

        runTestOnUiThread(new Runnable() {
            public void run() {
                if (isOrientationSupported) {
                    OrientationTestUtils.toggleOrientation(activity);
                }
            }
        });
    }

    @Override
    protected void tearDown() throws Exception {
        // Implement proper cleanup after test execution if necessary
        super.tearDown();
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
import android.content.res.Configuration;

public class DialogTest extends InstrumentationTestCase {
    private boolean isOrientationSupported;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        isOrientationSupported = getInstrumentation().getTargetContext().getResources().getConfiguration().smallestScreenWidthDp >= 600;

        if (!isOrientationSupported) {
            // Skip tests if the device does not support both orientations
            fail("Test skipped: Device does not support both orientations");
        }
    }

    public void testOrientationToggleSync() throws Throwable {
        assertFalse(d.isOnSaveInstanceStateCalled);
        assertFalse(TestDialog.isOnRestoreInstanceStateCalled);

        if (isOrientationSupported) {
            OrientationTestUtils.toggleOrientationSync(mActivity, mInstrumentation);
        }

        assertTrue(d.isOnSaveInstanceStateCalled);
    }

    @Override
    protected void tearDown() throws Exception {
        // Implement proper cleanup after test execution if necessary
        super.tearDown();
    }
}
//<End of snippet n. 1>