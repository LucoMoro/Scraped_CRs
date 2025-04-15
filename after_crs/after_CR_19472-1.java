/*Remove Broken LayoutTest Tests

Bug 3188260

The annotations said "unsure if asserted widths are correct"...
They probably are not due to varying screen sizes and orientations.

Change-Id:I64404ce7de3913bb43e7766a7d111843da2ee535*/




//Synthetic comment -- diff --git a/tests/tests/text/src/android/text/cts/LayoutTest.java b/tests/tests/text/src/android/text/cts/LayoutTest.java
//Synthetic comment -- index 9d33190..4c305bb 100644

//Synthetic comment -- @@ -16,6 +16,11 @@

package android.text.cts;

import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.ToBeFixed;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
//Synthetic comment -- @@ -30,13 +35,6 @@
import android.text.Layout.Alignment;
import android.text.style.StrikethroughSpan;

@TestTargetClass(Layout.class)
public class LayoutTest extends AndroidTestCase {
private final static int LINE_COUNT = 5;
//Synthetic comment -- @@ -342,34 +340,6 @@

@TestTargetNew(
level = TestLevel.COMPLETE,
method = "getLineForVertical",
args = {int.class}
)







