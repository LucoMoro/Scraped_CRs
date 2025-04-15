/*Lengthen LONG_TEXT to avoid testScrollTo() fails

testScrollTo() fails if the width of the test device is much longer
Signed-off-by: sammi_ms <Sammi_MS@asus.com>

Change-Id:Ia0127a1ea822f6eab57c19dcc47a48e9cde5c787*/
//Synthetic comment -- diff --git a/tests/tests/text/src/android/text/method/cts/TouchTest.java b/tests/tests/text/src/android/text/method/cts/TouchTest.java
//Synthetic comment -- index 3071fd6..36e46ef 100755

//Synthetic comment -- @@ -34,6 +34,10 @@
private static final String LONG_TEXT = "Scrolls the specified widget to the specified " +
"coordinates, except constrains the X scrolling position to the horizontal regions " +
"of the text that will be visible after scrolling to the specified Y position." +
"This is the description of the test.";

private boolean mReturnFromTouchEvent;







