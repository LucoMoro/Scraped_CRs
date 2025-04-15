/*Replaced deprecated Contacts Api with ContactsContract

Change-Id:I4816781c7b48b27ec64a63d51db02646c3d6c1a5*/




//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/view/AutoComplete4.java b/samples/ApiDemos/src/com/example/android/apis/view/AutoComplete4.java
//Synthetic comment -- index c6fa08b..9310b38 100644

//Synthetic comment -- @@ -22,13 +22,15 @@
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.CursorAdapter;
import android.widget.FilterQueryProvider;
import android.widget.Filterable;
import android.widget.TextView;

//Synthetic comment -- @@ -39,8 +41,11 @@
setContentView(R.layout.autocomplete_4);

ContentResolver content = getContentResolver();
        Cursor cursor = content.query(ContactsContract.Contacts.CONTENT_URI,
                PEOPLE_PROJECTION, null, null, null);

        mColumnDisplayName = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);

ContactListAdapter adapter = new ContactListAdapter(this, cursor);

AutoCompleteTextView textView = (AutoCompleteTextView)
//Synthetic comment -- @@ -61,50 +66,40 @@
final LayoutInflater inflater = LayoutInflater.from(context);
final TextView view = (TextView) inflater.inflate(
android.R.layout.simple_dropdown_item_1line, parent, false);
            view.setText(cursor.getString(mColumnDisplayName));
return view;
}

@Override
public void bindView(View view, Context context, Cursor cursor) {
            ((TextView) view).setText(cursor.getString(mColumnDisplayName));
}

@Override
public String convertToString(Cursor cursor) {
            return cursor.getString(mColumnDisplayName);
}

@Override
public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
            FilterQueryProvider filter = getFilterQueryProvider();
            if (filter != null) {
                return filter.runQuery(constraint);
}

            Uri uri = Uri.withAppendedPath(
                    ContactsContract.Contacts.CONTENT_FILTER_URI,
                    Uri.encode(constraint.toString()));
            return mContent.query(uri, PEOPLE_PROJECTION, null, null, null);
}

        private ContentResolver mContent;
}

    public static final String[] PEOPLE_PROJECTION = new String[] {
        ContactsContract.Contacts._ID,
        ContactsContract.Contacts.DISPLAY_NAME
};

    private static int mColumnDisplayName;
}
\ No newline at end of file








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/view/AutoComplete5.java b/samples/ApiDemos/src/com/example/android/apis/view/AutoComplete5.java
//Synthetic comment -- index 7406da4..3fdd091 100644

//Synthetic comment -- @@ -22,7 +22,7 @@
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.AutoCompleteTextView;

public class AutoComplete5 extends Activity {
//Synthetic comment -- @@ -32,8 +32,8 @@
setContentView(R.layout.autocomplete_5);

ContentResolver content = getContentResolver();
        Cursor cursor = content.query(ContactsContract.Contacts.CONTENT_URI,
                AutoComplete4.PEOPLE_PROJECTION, null, null, null);
AutoComplete4.ContactListAdapter adapter =
new AutoComplete4.ContactListAdapter(this, cursor);

//Synthetic comment -- @@ -41,13 +41,4 @@
findViewById(R.id.edit);
textView.setAdapter(adapter);
}
}
\ No newline at end of file








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/view/ExpandableList2.java b/samples/ApiDemos/src/com/example/android/apis/view/ExpandableList2.java
//Synthetic comment -- index 5784122..4934740 100644

//Synthetic comment -- @@ -17,49 +17,51 @@
package com.example.android.apis.view;

import android.app.ExpandableListActivity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.ExpandableListAdapter;
import android.widget.SimpleCursorTreeAdapter;

/**
* Demonstrates expandable lists backed by Cursors
*/
public class ExpandableList2 extends ExpandableListActivity {
    private int mContactIdColumnIndex;

    private static final String[] PEOPLE_PROJECTION = new String[] {
        ContactsContract.Contacts._ID,
        ContactsContract.Contacts.DISPLAY_NAME
};

    private static final String[] PHONE_PROJECTION = new String[] {
        ContactsContract.CommonDataKinds.Phone._ID,
        ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
        ContactsContract.CommonDataKinds.Phone.NUMBER
    };

private ExpandableListAdapter mAdapter;

@Override
public void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);

// Query for people
        Cursor groupCursor = managedQuery(ContactsContract.Contacts.CONTENT_URI,
                PEOPLE_PROJECTION, null, null, null);

// Cache the ID column index
        mContactIdColumnIndex = groupCursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID);

// Set up our adapter
mAdapter = new MyExpandableListAdapter(groupCursor,
this,
android.R.layout.simple_expandable_list_item_1,
android.R.layout.simple_expandable_list_item_1,
                new String[] {ContactsContract.Contacts.DISPLAY_NAME}, // Name for group layouts
new int[] {android.R.id.text1},
                new String[] {ContactsContract.CommonDataKinds.Phone.NUMBER}, // Number for child layouts
new int[] {android.R.id.text1});
setListAdapter(mAdapter);
}
//Synthetic comment -- @@ -75,18 +77,13 @@

@Override
protected Cursor getChildrenCursor(Cursor groupCursor) {
            int contactId = groupCursor.getInt(mContactIdColumnIndex);
// The returned Cursor MUST be managed by us, so we use Activity's helper
// functionality to manage it for us.
            return managedQuery(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    PHONE_PROJECTION,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId,
                    null, null);
}
}
}
\ No newline at end of file








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/view/Gallery2.java b/samples/ApiDemos/src/com/example/android/apis/view/Gallery2.java
//Synthetic comment -- index 2eea1ff..c48e5a0 100644

//Synthetic comment -- @@ -17,9 +17,8 @@
package com.example.android.apis.view;

import android.app.Activity;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.os.Bundle;
import android.widget.Gallery;
import android.widget.SimpleCursorAdapter;
//Synthetic comment -- @@ -37,16 +36,17 @@
setContentView(R.layout.gallery_2);

// Get a cursor with all people
        Cursor c = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                PEOPLE_PROJECTION, null, null, null);
startManagingCursor(c);

SpinnerAdapter adapter = new SimpleCursorAdapter(this,
// Use a template that displays a text view
android.R.layout.simple_gallery_item,
// Give the cursor to the list adatper
c,
// Map the NAME column in the people database to...
                new String[] {ContactsContract.Contacts.DISPLAY_NAME},
// The "text1" view defined in the XML template
new int[] { android.R.id.text1 });

//Synthetic comment -- @@ -54,4 +54,8 @@
g.setAdapter(adapter);
}

    private static final String[] PEOPLE_PROJECTION = new String[] {
        ContactsContract.Contacts._ID,
        ContactsContract.Contacts.DISPLAY_NAME
    };
}
\ No newline at end of file








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/view/List2.java b/samples/ApiDemos/src/com/example/android/apis/view/List2.java
//Synthetic comment -- index 4f37dd8..88539ad 100644

//Synthetic comment -- @@ -18,34 +18,40 @@

import android.app.ListActivity;
import android.database.Cursor;
import android.provider.ContactsContract;
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
        Cursor c = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                PEOPLE_PROJECTION, null, null, null);
startManagingCursor(c);

        ListAdapter adapter = new SimpleCursorAdapter(this,
// Use a template that displays a text view
                android.R.layout.simple_list_item_1,
// Give the cursor to the list adatper
                c,
// Map the NAME column in the people database to...
                new String[] {ContactsContract.Contacts.DISPLAY_NAME},
// The "text1" view defined in the XML template
                new int[] {android.R.id.text1});
setListAdapter(adapter);
}

    private static final String[] PEOPLE_PROJECTION = new String[] {
        ContactsContract.Contacts._ID,
        ContactsContract.Contacts.DISPLAY_NAME
    };
}
\ No newline at end of file








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/view/List3.java b/samples/ApiDemos/src/com/example/android/apis/view/List3.java
//Synthetic comment -- index 17e59f1..c6dd327 100644

//Synthetic comment -- @@ -16,36 +16,73 @@

package com.example.android.apis.view;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

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
        Cursor c = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                PHONE_PROJECTION, null, null, null);
startManagingCursor(c);

        mColumnType = c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
        mColumnLabel = c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LABEL);

// Map Cursor columns to views defined in simple_list_item_2.xml
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_2, c,
                        new String[] {
                            ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.NUMBER
                        },
new int[] { android.R.id.text1, android.R.id.text2 });
        //Used to display a readable string for the phone type
        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                //Let the adapter handle the binding if the column is not TYPE
                if (columnIndex != mColumnType) {
                    return false;
                }
                int type = cursor.getInt(mColumnType);
                String label = null;
                //Custom type? Then get the custom label
                if (type == ContactsContract.CommonDataKinds.Phone.TYPE_CUSTOM) {
                    label = cursor.getString(mColumnLabel);
                }
                //Get the readable string
                String text = (String) ContactsContract.CommonDataKinds.Phone.getTypeLabel(
                        getResources(), type, label);
                //Set text
                ((TextView) view).setText(text);
                return true;
            }
        });
setListAdapter(adapter);
}

    private static final String[] PHONE_PROJECTION = new String[] {
        ContactsContract.CommonDataKinds.Phone._ID,
        ContactsContract.CommonDataKinds.Phone.TYPE,
        ContactsContract.CommonDataKinds.Phone.LABEL,
        ContactsContract.CommonDataKinds.Phone.NUMBER
    };

    private int mColumnType;
    private int mColumnLabel;
}
\ No newline at end of file








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/view/List7.java b/samples/ApiDemos/src/com/example/android/apis/view/List7.java
//Synthetic comment -- index d44ed56..9a285db 100644

//Synthetic comment -- @@ -20,10 +20,9 @@
// class is in a sub-package.
import com.example.android.apis.R;

import android.app.ListActivity;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
//Synthetic comment -- @@ -36,10 +35,23 @@
* A list view example where the data comes from a cursor.
*/
public class List7 extends ListActivity implements OnItemSelectedListener {
    private static final String[] PEOPLE_PROJECTION = new String[] {
        ContactsContract.Contacts._ID,
        ContactsContract.Contacts.DISPLAY_NAME,
        ContactsContract.Contacts.HAS_PHONE_NUMBER
};

    private static final String[] PHONE_PROJECTION = new String[] {
        ContactsContract.CommonDataKinds.Phone._ID,
        ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
        ContactsContract.CommonDataKinds.Phone.TYPE,
        ContactsContract.CommonDataKinds.Phone.LABEL,
        ContactsContract.CommonDataKinds.Phone.NUMBER
    };

    private int mColumnHasPhoneNumber;
    private int mColumnContactId;

@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
//Synthetic comment -- @@ -48,34 +60,73 @@
getListView().setOnItemSelectedListener(this);

// Get a cursor with all people
        Cursor c = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                PEOPLE_PROJECTION, null, null, null);
startManagingCursor(c);

        mColumnHasPhoneNumber = c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
        mColumnContactId = c.getColumnIndex(ContactsContract.Contacts._ID);

ListAdapter adapter = new SimpleCursorAdapter(this,
                // Use a template that displays a text view
                android.R.layout.simple_list_item_1,
                // Give the cursor to the list adatper
                c,
                // Map the DISPLAY_NAME column in the people database to...
                new String[] {ContactsContract.Contacts.DISPLAY_NAME},
                // The "text1" view defined in the XML template
                new int[] {android.R.id.text1});
setListAdapter(adapter);
}

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
if (position >= 0) {
            //Get current Contact cursor
Cursor c = (Cursor) parent.getItemAtPosition(position);
            int hasPhone = c.getInt(mColumnHasPhoneNumber);
            String text = "Nothing...";
            //Only if contact has a PhoneNumber
            if (hasPhone == 1) {
                int contactId = c.getInt(mColumnContactId);
                //Filter the Phone cursor the the current Contact
                Cursor phoneCursor = getContentResolver().query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        PHONE_PROJECTION,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId,
                        null, null);
                startManagingCursor(phoneCursor);
                //Phone Number present?
                if (phoneCursor.getCount() > 0) {
                    //only display the first number
                    phoneCursor.moveToFirst();
                    int columnPhoneType = phoneCursor.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.TYPE);
                    int columnPhoneNumber = phoneCursor.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.NUMBER);
                    int type = phoneCursor.getInt(columnPhoneType);
                    String phone = phoneCursor.getString(columnPhoneNumber);
                    String label = null;
                    //Custom type? Then get the custom label
                    if (type == ContactsContract.CommonDataKinds.Phone.TYPE_CUSTOM) {
                        int columnPhoneLabel = phoneCursor.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.LABEL);
                        label = phoneCursor.getString(columnPhoneLabel);
                    }
                    //Get the readable string
                    String numberType = (String)ContactsContract.CommonDataKinds.Phone
                        .getTypeLabel(getResources(), type, label);
                    text = numberType + ": " + phone;
                }
                stopManagingCursor(phoneCursor);
                phoneCursor.close();
            }
            mPhone.setText(text);
}
}

    public void onNothingSelected(AdapterView<?> parent) {
mPhone.setText(R.string.list_7_nothing);
}

private TextView mPhone;
}
\ No newline at end of file







