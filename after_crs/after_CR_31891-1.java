/*Modification of testSetHorizontalSpacing()

Whenever the horizontal resolution exceeds 1056 dots,

testSetHorizontalSpacing() is failed.

The parameter of mGridView.setNumColumns() is changed from 22 to 28.

The test case is also passed on a terminal  of horizontal resolution
of 1344 dots.

Change-Id:Id268f167319f833bd35f7e60b04c461101d7042a*/




//Synthetic comment -- diff --git a/tests/tests/widget/src/android/widget/cts/GridViewTest.java b/tests/tests/widget/src/android/widget/cts/GridViewTest.java
//Synthetic comment -- index 84c07f7..7f88c96 100755

//Synthetic comment -- @@ -312,7 +312,14 @@
mGridView.setStretchMode(GridView.NO_STRETCH);
// Number of columns should be big enough, otherwise the
// horizontal spacing cannot be correctly verified.
        // Kubota Yasukazu 27/01/2012
        // if horizontal resolution is greater than 1056 dots,
        // this test case is failed.
        // if Number of columns is 28, this test case is passed
        // upto 1344 dots,
        //         mGridView.setNumColumns(22);
        mGridView.setNumColumns(28);	// Kubota Yasukazu


// this test case can not be ran in UI thread.
mActivity.runOnUiThread(new Runnable() {







