/*CTS API test for getTextBounds (java.lang.String, int, int, android.graphics.Rect)

Cts Api test that returns in bounds the smallest rectangle that can enclose a given String
This is made to improve CTS api coverage.

Change-Id:I9bbae1068777adbdc112fc1752efe60bba4f103dSigned-off-by: Gatej Constantin <cgatej1985ro@gmail.com>*/




//Synthetic comment -- diff --git a/tests/tests/graphics/src/android/graphics/cts/PaintTest.java b/tests/tests/graphics/src/android/graphics/cts/PaintTest.java
//Synthetic comment -- index 32d8e4d..591a6ee 100644

//Synthetic comment -- @@ -33,6 +33,7 @@
import android.os.Build;
import android.test.AndroidTestCase;
import android.text.SpannedString;
import android.graphics.Rect;

import java.util.Locale;

//Synthetic comment -- @@ -977,4 +978,13 @@
}
}

    public void testGetTextBounds1()
	{
	final String Text = "test";
	Paint p = new Paint();
	Rect bounds = new Rect();
	assertTrue(bounds.isEmpty());
	p.getTextBounds(Text, 0, Text.length(), bounds);
	assertFalse(bounds.isEmpty());
	}
}







