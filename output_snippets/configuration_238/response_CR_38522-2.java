//<Beginning of snippet n. 0>
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.test.InstrumentationTestCase;

public class ApplicationTest extends InstrumentationTestCase {

    public void testApplicationLifecycle() {
        assertTrue(mockApp.isConstructorCalled);
        assertTrue(mockApp.isOnCreateCalled);

        // Check if the device supports orientation changes
        if (getInstrumentation().getTargetContext().getResources().getConfiguration().orientation != Configuration.ORIENTATION_UNDEFINED) {
            runTestOnUiThread(new Runnable() {
                public void run() {
                    OrientationTestUtils.toggleOrientation(activity);
                }
            });
        } else {
            // Skip test as the device does not support orientation changes
            assumeTrue(false);
        }
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
        assertFalse(d.isOnSaveInstanceStateCalled);
        assertFalse(TestDialog.isOnRestoreInstanceStateCalled);

        // Check if the device supports orientation changes
        if (getInstrumentation().getTargetContext().getResources().getConfiguration().orientation != Configuration.ORIENTATION_UNDEFINED) {
            OrientationTestUtils.toggleOrientationSync(mActivity, mInstrumentation);
            assertTrue(d.isOnSaveInstanceStateCalled);
        } else {
            // Skip test as the device does not support orientation changes
            assumeTrue(false);
        }
    }
}
//<End of snippet n. 1>