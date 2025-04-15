/*Adding waitForIdleSync in testTouchMode.

There are cases where checking of the status change of main thread is done before setting the focus on UI thread.

In this case, test case fails.

To avoid this, I added waitForIdleSync after UI thread is created to wait for focus setting to be completed.*/
//Synthetic comment -- diff --git a/tests/tests/view/src/android/view/cts/ViewTest.java b/tests/tests/view/src/android/view/cts/ViewTest.java
//Synthetic comment -- index 3b9d751..726b0f7 100644

//Synthetic comment -- @@ -3936,6 +3936,7 @@
fitWindowsView.requestFocus();
}
});
assertTrue(mockView.isFocusableInTouchMode());
assertFalse(fitWindowsView.isFocusableInTouchMode());
assertTrue(mockView.isFocusable());
//Synthetic comment -- @@ -3953,12 +3954,14 @@
mockView.requestFocus();
}
});
assertTrue(mockView.isFocused());
runTestOnUiThread(new Runnable() {
public void run() {
fitWindowsView.requestFocus();
}
});
assertFalse(fitWindowsView.isFocused());
assertTrue(mockView.isInTouchMode());
assertTrue(fitWindowsView.isInTouchMode());
//Synthetic comment -- @@ -3972,6 +3975,7 @@
fitWindowsView.requestFocus();
}
});
assertFalse(mockView.isFocused());
assertTrue(fitWindowsView.isFocused());
assertFalse(mockView.isInTouchMode());







