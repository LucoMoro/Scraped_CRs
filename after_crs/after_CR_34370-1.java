/*Fix CTS case testScrollTo.

For 1080p landscape device, the text will be fully displayed without scrolling bar. The text width is less than 1920.
that's, "width - tv.getWidth() - 1" will be a negative value. and tv.getScrollX() will be zero.
And the following assertion failed.
   assertEquals(width - tv.getWidth() - 1, tv.getScrollX());*/




//Synthetic comment -- diff --git a/tests/tests/text/src/android/text/method/cts/TouchTest.java b/tests/tests/text/src/android/text/method/cts/TouchTest.java
//Synthetic comment -- index 7217db7..6cd15ff 100755

//Synthetic comment -- @@ -40,6 +40,10 @@
private static final String LONG_TEXT = "Scrolls the specified widget to the specified " +
"coordinates, except constrains the X scrolling position to the horizontal regions " +
"of the text that will be visible after scrolling to the specified Y position." +
            "This is the description of the test." + 
	    "Scrolls the specified widget to the specified " +
            "coordinates, except constrains the X scrolling position to the horizontal regions " +
            "of the text that will be visible after scrolling to the specified Y position." +
"This is the description of the test.";

private boolean mReturnFromTouchEvent;







