/*Fix MovieTest#testGetMovieProperties

Issue 2663463

Drawable#getIntrinsicWidth() returns scaled pixels, but
Drawable#width() does not. Scale the return values from width() and
height(). Most of the time the API returns pixels, but it seems
that getIntrinsicWidth has returned scaled dimensions even from
Donut.

Change-Id:Iab1d22b2b3df84771777e0b2c247a193c01589dc*/
//Synthetic comment -- diff --git a/tests/tests/graphics/src/android/graphics/cts/MovieTest.java b/tests/tests/graphics/src/android/graphics/cts/MovieTest.java
//Synthetic comment -- index c377d5e..5621212 100644

//Synthetic comment -- @@ -28,6 +28,8 @@
import android.graphics.Movie;
import android.graphics.Paint;
import android.test.ActivityInstrumentationTestCase2;
import dalvik.annotation.TestTargets;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetNew;
//Synthetic comment -- @@ -199,11 +201,14 @@
public void testGetMovieProperties() {
assertEquals(1000, mMovie.duration());
assertFalse(mMovie.isOpaque());
        int expectedHeight = getActivity().getResources().getDrawable(MOVIE)
                .getIntrinsicHeight();
        assertEquals(expectedHeight, mMovie.height());
        int expectedWidth = getActivity().getResources().getDrawable(MOVIE)
                .getIntrinsicWidth();
        assertEquals(expectedWidth, mMovie.width());
}
}
\ No newline at end of file







