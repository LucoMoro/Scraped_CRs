/*Add extra conditions to test HeaderListViewAdapter isEmpty

Fix for Issue ID 18316. Test that HeaderListViewAdapter isEmpty() returns false when HeaderListViewAdapter getCount() returns a non-zero value. So that the list cannot be considered empty when a header or footer is present.https://code.google.com/p/android/issues/detail?id=18316Change-Id:I2e5db20bacf9f17303adc1d433d983036a16535cSigned-off-by: Emma Sajic <esajic@effectivelateralsolutions.co.uk>*/
//Synthetic comment -- diff --git a/tests/tests/widget/src/android/widget/cts/HeaderViewListAdapterTest.java b/tests/tests/widget/src/android/widget/cts/HeaderViewListAdapterTest.java
//Synthetic comment -- index e583dce..c930370 100644

//Synthetic comment -- @@ -81,6 +81,21 @@
HeaderViewFullAdapter fullAdapter = new HeaderViewFullAdapter();
headerViewListAdapter = new HeaderViewListAdapter(null, null, fullAdapter);
assertFalse(headerViewListAdapter.isEmpty());
}

public void testRemoveHeader() {







