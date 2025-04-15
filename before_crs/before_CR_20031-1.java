/*In API demo list 7, when use onItemSelectedListener, there is no effect when user click item on List7. So change it to onItemClickListener.

Change-Id:I7cc5c63575197caf4e8a20cb29a0881d2152a409*/
//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/view/List7.java b/samples/ApiDemos/src/com/example/android/apis/view/List7.java
//Synthetic comment -- index ff5406f..64950db 100644

//Synthetic comment -- @@ -27,7 +27,7 @@
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
//Synthetic comment -- @@ -35,7 +35,7 @@
/**
* A list view example where the data comes from a cursor.
*/
public class List7 extends ListActivity implements OnItemSelectedListener {

private TextView mPhone;

//Synthetic comment -- @@ -56,7 +56,7 @@
super.onCreate(savedInstanceState);
setContentView(R.layout.list_7);
mPhone = (TextView) findViewById(R.id.phone);
        getListView().setOnItemSelectedListener(this);

// Get a cursor with all numbers.
// This query will only return contacts with phone numbers
//Synthetic comment -- @@ -76,7 +76,7 @@
setListAdapter(adapter);
}

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
if (position >= 0) {
//Get current cursor
Cursor c = (Cursor) parent.getItemAtPosition(position);







