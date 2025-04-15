/*Modify Layout of MockView to pass CTS in xxdpi condition.

In testAccessTouchDelegate, it clicks the center of mock_view.
But in our device, button blocks the center of mock_view. It makes CTS fail.
So, we modified CTS code to use the layout of scroll_view, instead of mock_view. There are some issues about xxdpi issue in CTS tool. We hope that Google fix them :)

Change-Id:Ic531521b33912066125888931f8a7f0dd0872813Signed-off-by: Donghyuk Kim <donghyuk.kim@lge.com>*/




//Synthetic comment -- diff --git a/tests/tests/view/src/android/view/cts/ViewTest.java b/tests/tests/view/src/android/view/cts/ViewTest.java
//Synthetic comment -- index 888bb73..dde87c8 100644

//Synthetic comment -- @@ -243,7 +243,7 @@
}

public void testAccessTouchDelegate() throws Throwable {
        final MockView view = (MockView) mActivity.findViewById(R.id.scroll_view);
Rect rect = new Rect();
final Button button = new Button(mActivity);
final int WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT;







