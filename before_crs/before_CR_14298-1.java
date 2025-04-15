/*Fix ViewTest#testGet*VisibleRect* for QVGA

Bug 2447678

Instead of resizing the test view to 200x300 resize the widget
to a size that will be visible on the screen by setting it to
half te size of the window. This works in both portrait and
landscape as well.

Change-Id:Ic30bc7c396e39f64ccc89eb4a1a853390883a89e*/
//Synthetic comment -- diff --git a/tests/tests/view/src/android/view/cts/ViewTest.java b/tests/tests/view/src/android/view/cts/ViewTest.java
//Synthetic comment -- index af7bf97..3b9d751 100644

//Synthetic comment -- @@ -789,7 +789,12 @@
getInstrumentation().waitForIdleSync();
assertFalse(view.getGlobalVisibleRect(rect, point));

        final LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(200, 300);
runTestOnUiThread(new Runnable() {
public void run() {
view.setLayoutParams(layoutParams3);
//Synthetic comment -- @@ -799,8 +804,8 @@
assertTrue(view.getGlobalVisibleRect(rect, point));
assertEquals(rcParent.left, rect.left);
assertEquals(rcParent.top, rect.top);
        assertEquals(rect.left + 200, rect.right);
        assertEquals(rect.top + 300, rect.bottom);
assertEquals(ptParent.x, point.x);
assertEquals(ptParent.y, point.y);
}
//Synthetic comment -- @@ -843,7 +848,12 @@
getInstrumentation().waitForIdleSync();
assertFalse(view.getGlobalVisibleRect(rect));

        final LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(200, 300);
runTestOnUiThread(new Runnable() {
public void run() {
view.setLayoutParams(layoutParams3);
//Synthetic comment -- @@ -853,8 +863,8 @@
assertTrue(view.getGlobalVisibleRect(rect));
assertEquals(rcParent.left, rect.left);
assertEquals(rcParent.top, rect.top);
        assertEquals(rect.left + 200, rect.right);
        assertEquals(rect.top + 300, rect.bottom);
}

@TestTargets({
//Synthetic comment -- @@ -2900,9 +2910,6 @@
method = "getLocalVisibleRect",
args = {android.graphics.Rect.class}
)
    @ToBeFixed(bug = "1695243", explanation =
            "the javadoc for getLocalVisibleRect() is incomplete." +
            "1. not clear what is supposed to happen if the input Rect is null.")
public void testGetLocalVisibleRect() throws Throwable {
final View view = mActivity.findViewById(R.id.mock_view);
Rect rect = new Rect();
//Synthetic comment -- @@ -2931,7 +2938,12 @@
getInstrumentation().waitForIdleSync();
assertFalse(view.getLocalVisibleRect(rect));

        final LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(200, 300);
runTestOnUiThread(new Runnable() {
public void run() {
view.setLayoutParams(layoutParams3);
//Synthetic comment -- @@ -2942,8 +2954,8 @@
assertTrue(view.getLocalVisibleRect(rect));
assertEquals(20, rect.left);
assertEquals(-30, rect.top);
        assertEquals(200 + 20, rect.right);
        assertEquals(300 - 30, rect.bottom);

try {
view.getLocalVisibleRect(null);
//Synthetic comment -- @@ -4794,10 +4806,10 @@
public String tag;
public View firstChild;
}
    
private static final class MockRunnable implements Runnable {
public boolean hasRun = false;
        
public void run() {
hasRun = true;
}







