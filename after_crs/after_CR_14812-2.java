/*Replaced deprecated Contacts Api with ContactsContract

Change-Id:I4816781c7b48b27ec64a63d51db02646c3d6c1a5*/




//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/view/AutoComplete4.java b/samples/ApiDemos/src/com/example/android/apis/view/AutoComplete4.java
//Synthetic comment -- index c6fa08b..3e196d7 100644

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

//Synthetic comment -- @@ -39,8 +41,8 @@
setContentView(R.layout.autocomplete_4);

ContentResolver content = getContentResolver();
        Cursor cursor = content.query(ContactsContract.Contacts.CONTENT_URI,
                PEOPLE_PROJECTION, null, null, null);
ContactListAdapter adapter = new ContactListAdapter(this, cursor);

AutoCompleteTextView textView = (AutoCompleteTextView)
//Synthetic comment -- @@ -61,50 +63,40 @@
final LayoutInflater inflater = LayoutInflater.from(context);
final TextView view = (TextView) inflater.inflate(
android.R.layout.simple_dropdown_item_1line, parent, false);
            view.setText(cursor.getString(COLUMN_DISPLAY_NAME));
return view;
}

@Override
public void bindView(View view, Context context, Cursor cursor) {
            ((TextView) view).setText(cursor.getString(COLUMN_DISPLAY_NAME));
}

@Override
public String convertToString(Cursor cursor) {
            return cursor.getString(COLUMN_DISPLAY_NAME);
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

    private static final int COLUMN_DISPLAY_NAME = 1;
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
//Synthetic comment -- index 17e59f1..30867b3 100644

//Synthetic comment -- @@ -16,36 +16,70 @@

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
                if (columnIndex != COLUMN_TYPE) {
                    return false;
                }
                int type = cursor.getInt(COLUMN_TYPE);
                String label = null;
                //Custom type? Then get the custom label
                if (type == ContactsContract.CommonDataKinds.Phone.TYPE_CUSTOM) {
                    label = cursor.getString(COLUMN_LABEL);
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

    private static final int COLUMN_TYPE = 1;
    private static final int COLUMN_LABEL = 2;
}
\ No newline at end of file







