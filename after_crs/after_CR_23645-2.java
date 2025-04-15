/*Add some strings for screen where the text stayed in

When the text - "Scrolls the specified widget to the specified
coordinates, except constrains the X scrolling position to the
horizontal regions of the text that will be visible after scrolling
to the specified Y position." - has stayed in the screen, it is not
scrolled. Therefore, some strings is added to make the text scroll.

Change-Id:I149f3d89ba60266527a08cf5fd3562f22272a304*/




//Synthetic comment -- diff --git a/tests/tests/text/src/android/text/method/cts/TouchTest.java b/tests/tests/text/src/android/text/method/cts/TouchTest.java
//Synthetic comment -- index 6b53f1d..7217db7 100755

//Synthetic comment -- @@ -39,7 +39,9 @@
private Activity mActivity;
private static final String LONG_TEXT = "Scrolls the specified widget to the specified " +
"coordinates, except constrains the X scrolling position to the horizontal regions " +
            "of the text that will be visible after scrolling to the specified Y position." +
            "This is the description of the test.";

private boolean mReturnFromTouchEvent;

public TouchTest() {







