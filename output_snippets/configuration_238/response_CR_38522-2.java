//<Beginning of snippet n. 0>
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.test.InstrumentationTestCase;
import android.content.res.Configuration;

public class ApplicationTest extends InstrumentationTestCase {
    private boolean isOrientationSupported;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        isOrientationSupported = checkDeviceOrientationSupport();
    }

    public void testApplication() {
        if (!isOrientationSupported) {
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

    private boolean checkDeviceOrientationSupport() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT &&
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
import android.content.res.Configuration;

public class DialogTest extends InstrumentationTestCase {
    private boolean isOrientationSupported;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        isOrientationSupported = checkDeviceOrientationSupport();
    }

    public void testDialog() {
        if (!isOrientationSupported) {
            return;
        }
        
        assertFalse(d.isOnSaveInstanceStateCalled);
        assertFalse(TestDialog.isOnRestoreInstanceStateCalled);

        OrientationTestUtils.toggleOrientationSync(mActivity, mInstrumentation);
        assertTrue(d.isOnSaveInstanceStateCalled);
    }

    private boolean checkDeviceOrientationSupport() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT &&
               getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }
}
//<End of snippet n. 1>