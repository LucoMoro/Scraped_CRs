/*Fix PopupWindowTest#testShowAtLocation to Support 0 Padding

Bug 2941912

Change the assertion that depended upon the popup window's theme
having > 0 padding. Removing the PopupWindow theme's background to
simulate a lack of padding causes this test to fail unless the
assertions allow zero padding.

Change-Id:I9e2aae710526182b03b9637a29c75cf01c9cbc4d*/
//Synthetic comment -- diff --git a/tests/tests/widget/src/android/widget/cts/PopupWindowTest.java b/tests/tests/widget/src/android/widget/cts/PopupWindowTest.java
//Synthetic comment -- index 542eddc..335c406 100644

//Synthetic comment -- @@ -443,8 +443,8 @@
assertTrue(mPopupWindow.isShowing());
mPopupWindow.getContentView().getLocationInWindow(viewInWindowXY);
mPopupWindow.getContentView().getLocationOnScreen(viewOnScreenXY);
        assertTrue(viewInWindowXY[0] > 0);
        assertTrue(viewInWindowXY[1] > 0);
assertEquals(viewInWindowXY[0] + xOff, viewOnScreenXY[0]);
assertEquals(viewInWindowXY[1] + yOff, viewOnScreenXY[1]);








