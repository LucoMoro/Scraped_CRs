
//<Beginning of snippet n. 0>


import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.test.InstrumentationTestCase;

/**
assertTrue(mockApp.isConstructorCalled);
assertTrue(mockApp.isOnCreateCalled);

runTestOnUiThread(new Runnable() {
public void run() {
OrientationTestUtils.toggleOrientation(activity);

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
assertFalse(d.isOnSaveInstanceStateCalled);
assertFalse(TestDialog.isOnRestoreInstanceStateCalled);

OrientationTestUtils.toggleOrientationSync(mActivity, mInstrumentation);

assertTrue(d.isOnSaveInstanceStateCalled);

//<End of snippet n. 1>








