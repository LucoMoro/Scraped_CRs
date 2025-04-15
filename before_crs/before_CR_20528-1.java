/*The layout height should be the value scaled by the density.

Change-Id:Ibdb463d9a50a592a9ffc60d5a8c94b1dbd4353f6*/
//Synthetic comment -- diff --git a/tests/tests/widget/src/android/widget/cts/AdapterViewTest.java b/tests/tests/widget/src/android/widget/cts/AdapterViewTest.java
old mode 100644
new mode 100755
//Synthetic comment -- index 97a5753..12815b2

//Synthetic comment -- @@ -310,8 +310,9 @@
setArrayAdapter(mAdapterView);

// LastVisiblePosition should be adapter's getCount - 1,by mocking method
        // TODO: the +50 is a gross hack
        mAdapterView.layout(0, 0, LAYOUT_WIDTH, LAYOUT_HEIGHT+50);
assertEquals(FRUIT.length - 1, mAdapterView.getLastVisiblePosition());
}








