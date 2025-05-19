//<Beginning of snippet n. 0>
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.test.InstrumentationTestCase;

public class ApplicationTest extends InstrumentationTestCase {
    private MockApplication mockApp;
    private Context context;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = getInstrumentation().getTargetContext();
        mockApp = new MockApplication();
    }

    public void testOrientationChange() throws Throwable {
        if ((context.getResources().getConfiguration().orientation != Configuration.ORIENTATION_PORTRAIT) &&
            (context.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE)) {
            return;
        }

        assertTrue(mockApp.isConstructorCalled);
        assertTrue(mockApp.isOnCreateCalled);

        runTestOnUiThread(new Runnable() {
            public void run() {
                OrientationTestUtils.toggleOrientation(context);
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
import android.app.Activity;

public class DialogTest extends InstrumentationTestCase {
    private Activity mActivity;
    private Instrumentation mInstrumentation;
    private TestDialog d;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mActivity = getActivity();
        mInstrumentation = getInstrumentation();
        d = new TestDialog(mActivity);
    }

    public void testDialogOrientationChange() throws Throwable {
        if ((mActivity.getResources().getConfiguration().orientation != Configuration.ORIENTATION_PORTRAIT) &&
            (mActivity.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE)) {
            return;
        }

        assertFalse(d.isOnSaveInstanceStateCalled);
        assertFalse(TestDialog.isOnRestoreInstanceStateCalled);

        OrientationTestUtils.toggleOrientationSync(mActivity, mInstrumentation);

        assertTrue(d.isOnSaveInstanceStateCalled);
    }
}
//<End of snippet n. 1>