/*Fix AbsListViewTest#testPointToPosition QVGA Landscape

Bug 8135

Use the height of the first item of the ListView to calculate the
position of the second item and use the value in the pointToPosition
call rather than using a hard coded value.

Change-Id:Ifee653f7ade261022b7d9a6422edb0ef9bdb3717*/




//Synthetic comment -- diff --git a/tests/tests/widget/src/android/widget/cts/AbsListViewTest.java b/tests/tests/widget/src/android/widget/cts/AbsListViewTest.java
//Synthetic comment -- index a70aec6..0f04bac 100644

//Synthetic comment -- @@ -504,11 +504,16 @@

setAdapter();

        View row = mListView.getChildAt(0);
        int rowHeight = row.getHeight();
        int middleOfSecondRow = rowHeight + rowHeight/2;

int position1 = mListView.pointToPosition(0, 0);
        int position2 = mListView.pointToPosition(50, middleOfSecondRow);

assertEquals(mAdapter_countries.getItemId(position1), mListView.pointToRowId(0, 0));
        assertEquals(mAdapter_countries.getItemId(position2),
                mListView.pointToRowId(50, middleOfSecondRow));

assertTrue(position2 > position1);
}







