/*CTS Test: Set fullscreenMode as false before checking.

[Comment]
The following test was failing during the view test cases.
android.view.inputmethod.cts.BaseInputConnectionTest#testReportFullscreenMode

Reason:
The mFullScreenMode is set as true when the test check for false status.

Solution:
This patch will set as false before it check for the false status as it
is implemented for true condition.

Change-Id:I7705cea0dd129564a85cc45aa210587076583897Signed-off-by: Solaiyappan Saravanan <saravanan.s@ti.com>*/
//Synthetic comment -- diff --git a/tests/tests/view/src/android/view/inputmethod/cts/BaseInputConnectionTest.java b/tests/tests/view/src/android/view/inputmethod/cts/BaseInputConnectionTest.java
//Synthetic comment -- index 8540a7c..f5f2286 100644

//Synthetic comment -- @@ -345,6 +345,7 @@
public void testReportFullscreenMode() {
InputMethodManager imManager = (InputMethodManager) mInstrumentation.getTargetContext()
.getSystemService(Context.INPUT_METHOD_SERVICE);
assertFalse(imManager.isFullscreenMode());
mConnection.reportFullscreenMode(true);
assertTrue(imManager.isFullscreenMode());







