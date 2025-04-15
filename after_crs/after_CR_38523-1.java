/*Fix window focus change timing issue

The state change of window focus is an asynchronous operation between
system server and its client application. Some related APIs are
highly depending on the window focus state such as
View.onWindowFocusChanged(boolean hasWindowFocus) and
InputMethodManager.isActive(View view). For the instrumentations
below which need to check above conditions they should be waiting
until window focus change is done.

android.view.cts.ViewTest#testInputConnection
android.view.cts.ViewTest#testWindowFocusChanged
android.view.inputmethod.cts.InputMethodManagerTest#testInputMethodManager

Change-Id:I6db9adbbee9ab79fd5ac65b8ff3bd9e215874d97*/




//Synthetic comment -- diff --git a/tests/tests/view/src/android/view/cts/ViewTest.java b/tests/tests/view/src/android/view/cts/ViewTest.java
//Synthetic comment -- index be60976..c794777 100644

//Synthetic comment -- @@ -3510,7 +3510,16 @@
)
})
public void testWindowFocusChanged() {
        final MockView view = (MockView) mActivity.findViewById(R.id.mock_view);

        // Wait until the window has been focused.
        new DelayedCheck(TIMEOUT_DELTA) {
            @Override
            protected boolean check() {
                return view.hasWindowFocus();
            }
        }.run();

assertTrue(view.hasCalledOnWindowFocusChanged());
assertTrue(view.hasCalledDispatchWindowFocusChanged());

//Synthetic comment -- @@ -3519,6 +3528,15 @@
assertFalse(view.hasCalledDispatchWindowFocusChanged());

StubActivity activity = launchActivity("com.android.cts.stub", StubActivity.class, null);

        // Wait until the window lost focus.
        new DelayedCheck(TIMEOUT_DELTA) {
            @Override
            protected boolean check() {
                return !view.hasWindowFocus();
            }
        }.run();

assertTrue(view.hasCalledOnWindowFocusChanged());
assertTrue(view.hasCalledDispatchWindowFocusChanged());

//Synthetic comment -- @@ -4419,15 +4437,27 @@
args = {}
)
})
    public void testInputConnection() throws Throwable {
final InputMethodManager imm = InputMethodManager.getInstance(getActivity());
final MockView view = (MockView) mActivity.findViewById(R.id.mock_view);
final ViewGroup viewGroup = (ViewGroup) mActivity.findViewById(R.id.viewlayout_root);
final MockEditText editText = new MockEditText(mActivity);

        // Wait until the window has been focused.
        new DelayedCheck(TIMEOUT_DELTA) {
            @Override
            protected boolean check() {
                return view.hasWindowFocus();
            }
        }.run();

        runTestOnUiThread(new Runnable() {
            public void run() {
                viewGroup.addView(editText);
                editText.requestFocus();
            }
        });
        getInstrumentation().waitForIdleSync();

new DelayedCheck(TIMEOUT_DELTA) {
@Override
//Synthetic comment -- @@ -4436,7 +4466,12 @@
}
}.run();

        runTestOnUiThread(new Runnable() {
            public void run() {
                imm.showSoftInput(editText, 0);
            }
        });
        getInstrumentation().waitForIdleSync();
assertTrue(editText.hasCalledOnCreateInputConnection());
assertTrue(editText.hasCalledOnCheckIsTextEditor());
assertTrue(imm.isActive(editText));








//Synthetic comment -- diff --git a/tests/tests/view/src/android/view/inputmethod/cts/InputMethodManagerTest.java b/tests/tests/view/src/android/view/inputmethod/cts/InputMethodManagerTest.java
//Synthetic comment -- index f997c13..fe518bb 100755

//Synthetic comment -- @@ -31,6 +31,7 @@
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.cts.DelayedCheck;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
//Synthetic comment -- @@ -148,6 +149,14 @@
public void testInputMethodManager() throws Throwable {
Window window = mActivity.getWindow();
final EditText view = (EditText) window.findViewById(R.id.entry);

        new DelayedCheck(1000) {
            @Override
            protected boolean check() {
                return view.hasWindowFocus();
            }
        }.run();

runTestOnUiThread(new Runnable() {
@Override
public void run() {







