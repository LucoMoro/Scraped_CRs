/*Remove isWatchingCursor Assertionhttp://b/2893002Don't always assume isWatchingCursor is always false. Alternate
InputMethodManager implementations may return true.

Change-Id:I7594302bf6bee9391e5d78bf4ec11ddafb50938b*/




//Synthetic comment -- diff --git a/tests/tests/view/src/android/view/inputmethod/cts/InputMethodManagerTest.java b/tests/tests/view/src/android/view/inputmethod/cts/InputMethodManagerTest.java
//Synthetic comment -- index d92f446..5b7940f 100755

//Synthetic comment -- @@ -164,9 +164,6 @@
connection.reportFullscreenMode(true);
assertTrue(imManager.isFullscreenMode());

IBinder token = view.getWindowToken();

// Show and hide input method.







