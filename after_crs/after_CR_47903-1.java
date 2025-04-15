/*Add extra conditions to test HeaderListViewAdapter isEmpty

Fix for Issue ID 18316. Test that HeaderListViewAdapter isEmpty() returns false when HeaderListViewAdapter getCount() returns a non-zero value. So that the list cannot be considered empty when a header or footer is present.https://code.google.com/p/android/issues/detail?id=18316Change-Id:I2e5db20bacf9f17303adc1d433d983036a16535cSigned-off-by: Emma Sajic <esajic@effectivelateralsolutions.co.uk>*/




//Synthetic comment -- diff --git a/tests/tests/widget/src/android/widget/cts/HeaderViewListAdapterTest.java b/tests/tests/widget/src/android/widget/cts/HeaderViewListAdapterTest.java
//Synthetic comment -- index e583dce..c930370 100644

//Synthetic comment -- @@ -81,6 +81,21 @@
HeaderViewFullAdapter fullAdapter = new HeaderViewFullAdapter();
headerViewListAdapter = new HeaderViewListAdapter(null, null, fullAdapter);
assertFalse(headerViewListAdapter.isEmpty());
                
        ListView lv = new ListView(getContext());
        ArrayList<ListView.FixedViewInfo> header = new ArrayList<ListView.FixedViewInfo>(4);
        header.add(lv.new FixedViewInfo());
        headerViewListAdapter = new HeaderViewListAdapter(header, null, null);
        assertEquals(1, headerViewListAdapter.getHeadersCount());
        assertFalse(headerViewListAdapter.isEmpty());
        
        lv = new ListView(getContext());
        ArrayList<ListView.FixedViewInfo> footer = new ArrayList<ListView.FixedViewInfo>(4);
        footer.add(lv.new FixedViewInfo());
        headerViewListAdapter = new HeaderViewListAdapter(null, footer, null);
        assertEquals(1, headerViewListAdapter.getFootersCount());
        assertFalse(headerViewListAdapter.isEmpty());
        
}

public void testRemoveHeader() {







