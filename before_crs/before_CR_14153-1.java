/*Remove @BrokenTest MovieTest Annotations

Bug 1790416

Remove the annotations because the tests work now.

Change-Id:Ib01416ea66a31a81884b9dfb196de21ed8c64041*/
//Synthetic comment -- diff --git a/tests/tests/graphics/src/android/graphics/cts/MovieTest.java b/tests/tests/graphics/src/android/graphics/cts/MovieTest.java
//Synthetic comment -- index 56421fd..c377d5e 100644

//Synthetic comment -- @@ -28,12 +28,10 @@
import android.graphics.Movie;
import android.graphics.Paint;
import android.test.ActivityInstrumentationTestCase2;
import dalvik.annotation.BrokenTest;
import dalvik.annotation.TestTargets;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.ToBeFixed;

@TestTargetClass(Movie.class)
public class MovieTest extends ActivityInstrumentationTestCase2<MockActivity> {
//Synthetic comment -- @@ -53,11 +51,9 @@
@TestTargetNew(
level = TestLevel.COMPLETE,
method = "draw",
        args = {android.graphics.Canvas.class, float.class, float.class, 
android.graphics.Paint.class}
)
    @ToBeFixed(bug = "1790416", explanation = "mMovie shouldn't be null")
    @BrokenTest("mMovie is null")
public void testDraw1() {
Canvas c = new Canvas();
Paint p = new Paint();
//Synthetic comment -- @@ -69,8 +65,6 @@
method = "draw",
args = {android.graphics.Canvas.class, float.class, float.class}
)
    @ToBeFixed(bug = "1790416", explanation = "mMovie shouldn't be null")
    @BrokenTest("mMovie is null")
public void testDraw2() {
Canvas c = new Canvas();
mMovie.draw(c, 100, 200);
//Synthetic comment -- @@ -81,8 +75,6 @@
method = "decodeFile",
args = {java.lang.String.class}
)
    @ToBeFixed(bug = "1790416", explanation = "mMovie shouldn't be null")
    @BrokenTest("mMovie is null")
public void testDecodeFile() throws Exception {
mMovie = null;
File imagefile = new File("/sqlite_stmt_journals", "animated.gif");
//Synthetic comment -- @@ -140,8 +132,6 @@
method = "decodeByteArray",
args = {byte[].class, int.class, int.class}
)
    @ToBeFixed(bug="1491795", explanation="always return null")
    @BrokenTest("mMovie is null")
public void testDecodeByteArray() throws Exception {
mMovie = null;
InputStream is = getActivity().getResources().openRawResource(MOVIE);
//Synthetic comment -- @@ -157,8 +147,6 @@
method = "decodeStream",
args = {java.io.InputStream.class}
)
    @ToBeFixed(bug = "1790416", explanation = "mMovie shouldn't be null")
    @BrokenTest("mMovie is null")
public void testDecodeStream() {
assertFalse(mMovie.isOpaque());
mMovie = null;
//Synthetic comment -- @@ -179,8 +167,6 @@
method = "setTime",
args = {int.class}
)
    @ToBeFixed(bug = "1790416", explanation = "mMovie shouldn't be null")
    @BrokenTest("mMovie is null")
public void testSetTime() {
assertTrue(mMovie.setTime(1000));
assertFalse(mMovie.setTime(Integer.MAX_VALUE));
//Synthetic comment -- @@ -210,8 +196,6 @@
args = {}
)
})
    @ToBeFixed(bug = "1790416", explanation = "mMovie shouldn't be null")
    @BrokenTest("mMovie is null")
public void testGetMovieProperties() {
assertEquals(1000, mMovie.duration());
assertFalse(mMovie.isOpaque());







