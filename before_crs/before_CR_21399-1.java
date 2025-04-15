/*Using DelayedCheck class in testWindowFocusChanged.

After executing findViewById() and launchActivity(), there are some cases where test case is failed since flags that are set asynchronously are checked only once immediately after that event.

To avoid this, I changed the code to monitor the change of flag by using DelayedCheck class.*/
//Synthetic comment -- diff --git a/tests/tests/view/src/android/view/cts/ViewTest.java b/tests/tests/view/src/android/view/cts/ViewTest.java
//Synthetic comment -- index 726b0f7..204684f 100644

//Synthetic comment -- @@ -3527,17 +3527,47 @@
)
})
public void testWindowFocusChanged() {
        MockView view = (MockView) mActivity.findViewById(R.id.mock_view);
        assertTrue(view.hasCalledOnWindowFocusChanged());
        assertTrue(view.hasCalledDispatchWindowFocusChanged());

view.reset();
        assertFalse(view.hasCalledOnWindowFocusChanged());
        assertFalse(view.hasCalledDispatchWindowFocusChanged());

StubActivity activity = launchActivity("com.android.cts.stub", StubActivity.class, null);
        assertTrue(view.hasCalledOnWindowFocusChanged());
        assertTrue(view.hasCalledDispatchWindowFocusChanged());

activity.finish();
}







