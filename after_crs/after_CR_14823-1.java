/*Fix for issue 895 (missing android: prefix), Removed unused imports

Change-Id:I5eeca1db7eb97251729dff728b4c482de4b14885*/




//Synthetic comment -- diff --git a/core/java/android/app/ListActivity.java b/core/java/android/app/ListActivity.java
//Synthetic comment -- index 19b99c8..9088920 100644

//Synthetic comment -- @@ -18,9 +18,7 @@

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
//Synthetic comment -- @@ -68,7 +66,7 @@
*               android:layout_weight=&quot;1&quot;
*               android:drawSelectorOnTop=&quot;false&quot;/&gt;
* 
 *     &lt;TextView android:id=&quot;@id/android:empty&quot;
*               android:layout_width=&quot;fill_parent&quot; 
*               android:layout_height=&quot;fill_parent&quot;
*               android:background=&quot;#FF0000&quot;
//Synthetic comment -- @@ -307,7 +305,7 @@
}

private AdapterView.OnItemClickListener mOnClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View v, int position, long id)
{
onListItemClick((ListView)parent, v, position, id);
}







