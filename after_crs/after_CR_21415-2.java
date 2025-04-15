/*Added view.invalidate() in testComputeScroll.

This test examines the value of flag set in computeScroll() is true or not.
The process of computeScroll() is done asynchronously form different process when scroll information needs to be updated after requestLayout().

However, computeScroll() is only called when scroll information needs updated.
If it does not require updated information this test fails.

To avoid this, I called view.invalidate() to modify to redraw the view.

Change-Id:I66ea1a4200b8ed2be8f142a11a9a23f4b4fb65f3*/




//Synthetic comment -- diff --git a/tests/tests/view/src/android/view/cts/ViewTest.java b/tests/tests/view/src/android/view/cts/ViewTest.java
//Synthetic comment -- index 6706c37..74a5f8c 100644

//Synthetic comment -- @@ -571,6 +571,7 @@
runTestOnUiThread(new Runnable() {
public void run() {
view.requestLayout();
                view.invalidate();
}
});
getInstrumentation().waitForIdleSync();







