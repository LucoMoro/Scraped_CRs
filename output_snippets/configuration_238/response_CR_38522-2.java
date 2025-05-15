//<Beginning of snippet n. 0>
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.test.InstrumentationTestCase;

public class ApplicationTest extends InstrumentationTestCase {
    private MockApp mockApp;
    private Context context;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = getInstrumentation().getTargetContext();
        mockApp = new MockApp();
        
        if (!OrientationTestUtils.isOrientationSupported(context)) {
            skipTest("Orientation changes not supported on this device");
        }
    }

    public void testApplicationLifecycle() throws Throwable {
        assertTrue(mockApp.isConstructorCalled);
        assertTrue(mockApp.isOnCreateCalled);

        runTestOnUiThread(new Runnable() {
            public void run() {
                if (OrientationTestUtils.isOrientationChangeSupported(context)) {
                    OrientationTestUtils.toggleOrientation(activity);
                }
            }
        });
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
    private MockDialog d;
    private Instrumentation mInstrumentation;
    private Activity mActivity;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mActivity = getActivity(); // Assume this method is defined elsewhere

        if (!OrientationTestUtils.isOrientationSupported(mActivity)) {
            skipTest("Orientation changes not supported on this device");
        }
    }

    public void testDialogLifecycle() throws Throwable {
        assertFalse(d.isOnSaveInstanceStateCalled);
        assertFalse(TestDialog.isOnRestoreInstanceStateCalled);

        if (OrientationTestUtils.isOrientationChangeSupported(mActivity)) {
            OrientationTestUtils.toggleOrientationSync(mActivity, mInstrumentation);
        }

        assertTrue(d.isOnSaveInstanceStateCalled);
    }
}
//<End of snippet n. 1>