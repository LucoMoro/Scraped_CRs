/*Adding DelayedCheck() in testHasWindowFocus.

After executing findViewById(), there are some cases where test case is failed since flags that are set asynchronously are checked only once immediately after that event.

To avoid this, I changed the code to monitor the change of flag.

Change-Id:Iaf30f18144a5ef1887334bc65cbef59ea5de70ac*/
//Synthetic comment -- diff --git a/tests/tests/view/src/android/view/cts/ViewTest.java b/tests/tests/view/src/android/view/cts/ViewTest.java
//Synthetic comment -- index 3370e2b..6706c37 100644

//Synthetic comment -- @@ -2276,8 +2276,14 @@
assertFalse(view.hasWindowFocus());

// mAttachInfo is not null
        view = mActivity.findViewById(R.id.fit_windows);
        assertTrue(view.hasWindowFocus());
}

@TestTargetNew(







