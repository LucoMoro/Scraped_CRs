/*If Display ratio (Height/Width) is 1.7, the button which is generated dynamically is longer and catches the input event.
The mockview’s onTouchEvent is not called and causes test failure.
We set the height of button as 1/3 of mockview’s Height to avoid to block the event delivery.

Change-Id:Icb97c775625bcd86df9a887bee4b84b073048806*/
//Synthetic comment -- diff --git a/tests/tests/view/src/android/view/cts/ViewTest.java b/tests/tests/view/src/android/view/cts/ViewTest.java
//Synthetic comment -- index 9065592..c9a8ec3 100644

//Synthetic comment -- @@ -246,10 +246,11 @@
Rect rect = new Rect();
final Button button = new Button(mActivity);
final int WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT;
runTestOnUiThread(new Runnable() {
public void run() {
mActivity.addContentView(button,
                        new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
}
});
getInstrumentation().waitForIdleSync();







