/*Replaced deprecated Contacts Api with ContactsContract

Change-Id:I4816781c7b48b27ec64a63d51db02646c3d6c1a5*/
//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/view/AutoComplete4.java b/samples/ApiDemos/src/com/example/android/apis/view/AutoComplete4.java
//Synthetic comment -- index c6fa08b..9310b38 100644

//Synthetic comment -- @@ -22,13 +22,15 @@
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Contacts;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.CursorAdapter;
import android.widget.Filterable;
import android.widget.TextView;

//Synthetic comment -- @@ -39,8 +41,11 @@
setContentView(R.layout.autocomplete_4);

ContentResolver content = getContentResolver();
        Cursor cursor = content.query(Contacts.People.CONTENT_URI,
                PEOPLE_PROJECTION, null, null, Contacts.People.DEFAULT_SORT_ORDER);
ContactListAdapter adapter = new ContactListAdapter(this, cursor);

AutoCompleteTextView textView = (AutoCompleteTextView)
//Synthetic comment -- @@ -61,50 +66,40 @@
final LayoutInflater inflater = LayoutInflater.from(context);
final TextView view = (TextView) inflater.inflate(
android.R.layout.simple_dropdown_item_1line, parent, false);
            view.setText(cursor.getString(5));
return view;
}

@Override
public void bindView(View view, Context context, Cursor cursor) {
            ((TextView) view).setText(cursor.getString(5));
}

@Override
public String convertToString(Cursor cursor) {
            return cursor.getString(5);
}

@Override
public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
            if (getFilterQueryProvider() != null) {
                return getFilterQueryProvider().runQuery(constraint);
}

            StringBuilder buffer = null;
            String[] args = null;
            if (constraint != null) {
                buffer = new StringBuilder();
                buffer.append("UPPER(");
                buffer.append(Contacts.ContactMethods.NAME);
                buffer.append(") GLOB ?");
                args = new String[] { constraint.toString().toUpperCase() + "*" };
            }

            return mContent.query(Contacts.People.CONTENT_URI, PEOPLE_PROJECTION,
                    buffer == null ? null : buffer.toString(), args,
                    Contacts.People.DEFAULT_SORT_ORDER);
}

        private ContentResolver mContent;        
}

    private static final String[] PEOPLE_PROJECTION = new String[] {
        Contacts.People._ID,
        Contacts.People.PRIMARY_PHONE_ID,
        Contacts.People.TYPE,
        Contacts.People.NUMBER,
        Contacts.People.LABEL,
        Contacts.People.NAME,
};
}
\ No newline at end of file








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/view/AutoComplete5.java b/samples/ApiDemos/src/com/example/android/apis/view/AutoComplete5.java
//Synthetic comment -- index 7406da4..3fdd091 100644

//Synthetic comment -- @@ -22,7 +22,7 @@
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Contacts;
import android.widget.AutoCompleteTextView;

public class AutoComplete5 extends Activity {
//Synthetic comment -- @@ -32,8 +32,8 @@
setContentView(R.layout.autocomplete_5);

ContentResolver content = getContentResolver();
        Cursor cursor = content.query(Contacts.People.CONTENT_URI,
                PEOPLE_PROJECTION, null, null, Contacts.People.DEFAULT_SORT_ORDER);
AutoComplete4.ContactListAdapter adapter =
new AutoComplete4.ContactListAdapter(this, cursor);

//Synthetic comment -- @@ -41,13 +41,4 @@
findViewById(R.id.edit);
textView.setAdapter(adapter);
}

    private static final String[] PEOPLE_PROJECTION = new String[] {
        Contacts.People._ID,
        Contacts.People.PRIMARY_PHONE_ID,
        Contacts.People.TYPE,
        Contacts.People.NUMBER,
        Contacts.People.LABEL,
        Contacts.People.NAME
    };
}
\ No newline at end of file








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/view/ExpandableList2.java b/samples/ApiDemos/src/com/example/android/apis/view/ExpandableList2.java
//Synthetic comment -- index 5784122..4934740 100644

//Synthetic comment -- @@ -17,49 +17,51 @@
package com.example.android.apis.view;

import android.app.ExpandableListActivity;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts.People;
import android.widget.ExpandableListAdapter;
import android.widget.SimpleCursorTreeAdapter;


/**
* Demonstrates expandable lists backed by Cursors
*/
public class ExpandableList2 extends ExpandableListActivity {
    private int mGroupIdColumnIndex; 
    
    private String mPhoneNumberProjection[] = new String[] {
            People.Phones._ID, People.Phones.NUMBER
};

    
private ExpandableListAdapter mAdapter;
    

@Override
public void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);

// Query for people
        Cursor groupCursor = managedQuery(People.CONTENT_URI,
                new String[] {People._ID, People.NAME}, null, null, null);

// Cache the ID column index
        mGroupIdColumnIndex = groupCursor.getColumnIndexOrThrow(People._ID);

// Set up our adapter
mAdapter = new MyExpandableListAdapter(groupCursor,
this,
android.R.layout.simple_expandable_list_item_1,
android.R.layout.simple_expandable_list_item_1,
                new String[] {People.NAME}, // Name for group layouts
new int[] {android.R.id.text1},
                new String[] {People.NUMBER}, // Number for child layouts
new int[] {android.R.id.text1});
setListAdapter(mAdapter);
}
//Synthetic comment -- @@ -75,18 +77,13 @@

@Override
protected Cursor getChildrenCursor(Cursor groupCursor) {
            // Given the group, we return a cursor for all the children within that group 

            // Return a cursor that points to this contact's phone numbers
            Uri.Builder builder = People.CONTENT_URI.buildUpon();
            ContentUris.appendId(builder, groupCursor.getLong(mGroupIdColumnIndex));
            builder.appendEncodedPath(People.Phones.CONTENT_DIRECTORY);
            Uri phoneNumbersUri = builder.build();

// The returned Cursor MUST be managed by us, so we use Activity's helper
// functionality to manage it for us.
            return managedQuery(phoneNumbersUri, mPhoneNumberProjection, null, null, null);
}

}
}
\ No newline at end of file








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/view/Gallery2.java b/samples/ApiDemos/src/com/example/android/apis/view/Gallery2.java
//Synthetic comment -- index 2eea1ff..c48e5a0 100644

//Synthetic comment -- @@ -17,9 +17,8 @@
package com.example.android.apis.view;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.provider.Contacts.People;
import android.os.Bundle;
import android.widget.Gallery;
import android.widget.SimpleCursorAdapter;
//Synthetic comment -- @@ -37,16 +36,17 @@
setContentView(R.layout.gallery_2);

// Get a cursor with all people
        Cursor c = getContentResolver().query(People.CONTENT_URI, null, null, null, null);
startManagingCursor(c);
        
SpinnerAdapter adapter = new SimpleCursorAdapter(this,
// Use a template that displays a text view
android.R.layout.simple_gallery_item,
// Give the cursor to the list adatper
c,
// Map the NAME column in the people database to...
                new String[] {People.NAME},
// The "text1" view defined in the XML template
new int[] { android.R.id.text1 });

//Synthetic comment -- @@ -54,4 +54,8 @@
g.setAdapter(adapter);
}

}
\ No newline at end of file








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/view/List2.java b/samples/ApiDemos/src/com/example/android/apis/view/List2.java
//Synthetic comment -- index 4f37dd8..88539ad 100644

//Synthetic comment -- @@ -18,34 +18,40 @@

import android.app.ListActivity;
import android.database.Cursor;
import android.provider.Contacts.People;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;

/**
 * A list view example where the 
* data comes from a cursor.
*/
public class List2 extends ListActivity {
    
@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);

// Get a cursor with all people
        Cursor c = getContentResolver().query(People.CONTENT_URI, null, null, null, null);
startManagingCursor(c);

        ListAdapter adapter = new SimpleCursorAdapter(this, 
// Use a template that displays a text view
                android.R.layout.simple_list_item_1, 
// Give the cursor to the list adatper
                c, 
// Map the NAME column in the people database to...
                new String[] {People.NAME} ,
// The "text1" view defined in the XML template
                new int[] {android.R.id.text1}); 
setListAdapter(adapter);
}
}
\ No newline at end of file








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/view/List3.java b/samples/ApiDemos/src/com/example/android/apis/view/List3.java
//Synthetic comment -- index 17e59f1..c6dd327 100644

//Synthetic comment -- @@ -16,36 +16,73 @@

package com.example.android.apis.view;


import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Contacts.Phones;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;

/**
 * A list view example where the 
* data comes from a cursor, and a
* SimpleCursorListAdapter is used to map each item to a two-line
* display.
*/
public class List3 extends ListActivity {
    
@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);

// Get a cursor with all phones
        Cursor c = getContentResolver().query(Phones.CONTENT_URI, null, null, null, null);
startManagingCursor(c);
        
// Map Cursor columns to views defined in simple_list_item_2.xml
        ListAdapter adapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_2, c, 
                        new String[] { Phones.NAME, Phones.NUMBER }, 
new int[] { android.R.id.text1, android.R.id.text2 });
setListAdapter(adapter);
}
  
}
\ No newline at end of file








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/view/List7.java b/samples/ApiDemos/src/com/example/android/apis/view/List7.java
//Synthetic comment -- index d44ed56..9a285db 100644

//Synthetic comment -- @@ -20,10 +20,9 @@
// class is in a sub-package.
import com.example.android.apis.R;


import android.app.ListActivity;
import android.database.Cursor;
import android.provider.Contacts.People;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
//Synthetic comment -- @@ -36,10 +35,23 @@
* A list view example where the data comes from a cursor.
*/
public class List7 extends ListActivity implements OnItemSelectedListener {
    private static String[] PROJECTION = new String[] {
        People._ID, People.NAME, People.NUMBER
};

@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
//Synthetic comment -- @@ -48,34 +60,73 @@
getListView().setOnItemSelectedListener(this);

// Get a cursor with all people
        Cursor c = getContentResolver().query(People.CONTENT_URI, PROJECTION, null, null, null);
startManagingCursor(c);
        mPhoneColumnIndex = c.getColumnIndex(People.NUMBER);

ListAdapter adapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1, // Use a template
                                                        // that displays a
                                                        // text view
                c, // Give the cursor to the list adatper
                new String[] {People.NAME}, // Map the NAME column in the
                                            // people database to...
                new int[] {android.R.id.text1}); // The "text1" view defined in
                                            // the XML template
setListAdapter(adapter);
}

    public void onItemSelected(AdapterView parent, View v, int position, long id) {
if (position >= 0) {
Cursor c = (Cursor) parent.getItemAtPosition(position);
            mPhone.setText(c.getString(mPhoneColumnIndex));
}
}

    public void onNothingSelected(AdapterView parent) {
mPhone.setText(R.string.list_7_nothing);

}

    private int mPhoneColumnIndex;
private TextView mPhone;
}
\ No newline at end of file







