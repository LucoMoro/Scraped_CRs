/*DialogTest onPanelClosed Called Properly Now

Bug 2219899

onPanelClosed is now called properly in Froyo, so undo the change:https://android-git.corp.google.com/g/#change,31241Change-Id:I499e61810d96d8b6d80adde3c4b1d7c660353572*/
//Synthetic comment -- diff --git a/tests/tests/app/src/android/app/cts/DialogTest.java b/tests/tests/app/src/android/app/cts/DialogTest.java
//Synthetic comment -- index 2b5e36b..ea7deca 100644

//Synthetic comment -- @@ -968,7 +968,7 @@
assertFalse(d.isOnContextMenuClosedCalled);
// Closed context menu
sendKeys(KeyEvent.KEYCODE_BACK);
        assertFalse(d.isOnPanelClosedCalled);
// Here isOnContextMenuClosedCalled should be true, see bug 1716918.
assertFalse(d.isOnContextMenuClosedCalled);








