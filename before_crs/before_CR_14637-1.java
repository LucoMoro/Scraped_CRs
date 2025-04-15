/*Fix AbsListViewTest#testPointToPosition QVGA Landscape

Bug 8135

Use the height of the first item of the ListView to calculate the
position of the second item and use the value in the pointToPosition
call rather than using a hard coded value.

Change-Id:Ifee653f7ade261022b7d9a6422edb0ef9bdb3717*/
//Synthetic comment -- diff --git a/tests/tests/widget/src/android/widget/cts/AbsListViewTest.java b/tests/tests/widget/src/android/widget/cts/AbsListViewTest.java
//Synthetic comment -- index a70aec6..058731b 100644

//Synthetic comment -- @@ -34,6 +34,7 @@
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.text.Editable;
//Synthetic comment -- @@ -504,11 +505,16 @@

setAdapter();

int position1 = mListView.pointToPosition(0, 0);
        int position2 = mListView.pointToPosition(50, 200);

assertEquals(mAdapter_countries.getItemId(position1), mListView.pointToRowId(0, 0));
        assertEquals(mAdapter_countries.getItemId(position2), mListView.pointToRowId(50, 200));

assertTrue(position2 > position1);
}







