/*Replaced deprecated Contacts Api with ContactsContract

Change-Id:I4816781c7b48b27ec64a63d51db02646c3d6c1a5*/




//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/view/AutoComplete4.java b/samples/ApiDemos/src/com/example/android/apis/view/AutoComplete4.java
//Synthetic comment -- index c6fa08b..bd643e6 100644

//Synthetic comment -- @@ -23,7 +23,7 @@
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
//Synthetic comment -- @@ -39,8 +39,8 @@
setContentView(R.layout.autocomplete_4);

ContentResolver content = getContentResolver();
        Cursor cursor = content.query(ContactsContract.Contacts.CONTENT_URI,
                PEOPLE_PROJECTION, null, null, null);
ContactListAdapter adapter = new ContactListAdapter(this, cursor);

AutoCompleteTextView textView = (AutoCompleteTextView)
//Synthetic comment -- @@ -86,25 +86,23 @@
if (constraint != null) {
buffer = new StringBuilder();
buffer.append("UPPER(");
                buffer.append(ContactsContract.Contacts.DISPLAY_NAME);
buffer.append(") GLOB ?");
args = new String[] { constraint.toString().toUpperCase() + "*" };
}

            return mContent.query(ContactsContract.Contacts.CONTENT_URI, PEOPLE_PROJECTION,
                    buffer == null ? null : buffer.toString(), args, null);
}

private ContentResolver mContent;        
}

private static final String[] PEOPLE_PROJECTION = new String[] {
        ContactsContract.Contacts._ID,
        ContactsContract.CommonDataKinds.Phone.TYPE,
        ContactsContract.CommonDataKinds.Phone.NUMBER,
        ContactsContract.CommonDataKinds.Phone.LABEL,
        ContactsContract.Contacts.DISPLAY_NAME
};
}








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/view/AutoComplete5.java b/samples/ApiDemos/src/com/example/android/apis/view/AutoComplete5.java
//Synthetic comment -- index 7406da4..00d724e 100644

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
                PEOPLE_PROJECTION, null, null, null);
AutoComplete4.ContactListAdapter adapter =
new AutoComplete4.ContactListAdapter(this, cursor);

//Synthetic comment -- @@ -43,11 +43,10 @@
}

private static final String[] PEOPLE_PROJECTION = new String[] {
        ContactsContract.Contacts._ID,
        ContactsContract.CommonDataKinds.Phone.TYPE,
        ContactsContract.CommonDataKinds.Phone.NUMBER,
        ContactsContract.CommonDataKinds.Phone.LABEL,
        ContactsContract.Contacts.DISPLAY_NAME
};
}








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/view/Gallery2.java b/samples/ApiDemos/src/com/example/android/apis/view/Gallery2.java
//Synthetic comment -- index 2eea1ff..de3edb0 100644

//Synthetic comment -- @@ -17,9 +17,8 @@
package com.example.android.apis.view;

import android.app.Activity;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.os.Bundle;
import android.widget.Gallery;
import android.widget.SimpleCursorAdapter;
//Synthetic comment -- @@ -37,7 +36,8 @@
setContentView(R.layout.gallery_2);

// Get a cursor with all people
        Cursor c = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
startManagingCursor(c);

SpinnerAdapter adapter = new SimpleCursorAdapter(this,
//Synthetic comment -- @@ -46,12 +46,11 @@
// Give the cursor to the list adatper
c,
// Map the NAME column in the people database to...
                new String[] {ContactsContract.Contacts.DISPLAY_NAME},
// The "text1" view defined in the XML template
new int[] { android.R.id.text1 });

Gallery g = (Gallery) findViewById(R.id.gallery);
g.setAdapter(adapter);
}
}








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/view/List2.java b/samples/ApiDemos/src/com/example/android/apis/view/List2.java
//Synthetic comment -- index 4f37dd8..3d9fba6 100644

//Synthetic comment -- @@ -18,7 +18,7 @@

import android.app.ListActivity;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
//Synthetic comment -- @@ -34,7 +34,8 @@
super.onCreate(savedInstanceState);

// Get a cursor with all people
        Cursor c = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
startManagingCursor(c);

ListAdapter adapter = new SimpleCursorAdapter(this, 
//Synthetic comment -- @@ -43,7 +44,7 @@
// Give the cursor to the list adatper
c, 
// Map the NAME column in the people database to...
                new String[] {ContactsContract.Contacts.DISPLAY_NAME},
// The "text1" view defined in the XML template
new int[] {android.R.id.text1}); 
setListAdapter(adapter);








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/view/List3.java b/samples/ApiDemos/src/com/example/android/apis/view/List3.java
//Synthetic comment -- index 17e59f1..dbf3fb9 100644

//Synthetic comment -- @@ -16,11 +16,10 @@

package com.example.android.apis.view;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;

//Synthetic comment -- @@ -37,15 +36,18 @@
super.onCreate(savedInstanceState);

// Get a cursor with all phones
        Cursor c = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null, null);
startManagingCursor(c);

// Map Cursor columns to views defined in simple_list_item_2.xml
ListAdapter adapter = new SimpleCursorAdapter(this,
android.R.layout.simple_list_item_2, c, 
                        new String[] {
                            ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.NUMBER
                        },
new int[] { android.R.id.text1, android.R.id.text2 });
setListAdapter(adapter);
}
}







