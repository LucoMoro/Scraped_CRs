/*Updated deprecated Contacts API to ContactsContract

Change-Id:I1d51b5eba579e841d6024eec08670ae8ff595f94*/




//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/app/AlertDialogSamples.java b/samples/ApiDemos/src/com/example/android/apis/app/AlertDialogSamples.java
//Synthetic comment -- index de5f711..40af808 100644

//Synthetic comment -- @@ -30,7 +30,7 @@
import android.widget.Button;
import android.widget.Toast;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.example.android.apis.R;

//Synthetic comment -- @@ -201,17 +201,17 @@
.create();
case DIALOG_MULTIPLE_CHOICE_CURSOR:
String[] projection = new String[] {
                        ContactsContract.Contacts._ID,
                        ContactsContract.Contacts.DISPLAY_NAME,
                        ContactsContract.Contacts.SEND_TO_VOICEMAIL
};
                Cursor cursor = managedQuery(ContactsContract.Contacts.CONTENT_URI, projection, null, null, null);
return new AlertDialog.Builder(AlertDialogSamples.this)
.setIcon(R.drawable.ic_popup_reminder)
.setTitle(R.string.alert_dialog_multi_choice_cursor)
.setMultiChoiceItems(cursor,
                            ContactsContract.Contacts.SEND_TO_VOICEMAIL,
                            ContactsContract.Contacts.DISPLAY_NAME,
new DialogInterface.OnMultiChoiceClickListener() {
public void onClick(DialogInterface dialog, int whichButton,
boolean isChecked) {








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/app/ContactsSelectInstrumentation.java b/samples/ApiDemos/src/com/example/android/apis/app/ContactsSelectInstrumentation.java
//Synthetic comment -- index dcb8b83..e34f4cf 100644

//Synthetic comment -- @@ -20,14 +20,11 @@
import android.app.Instrumentation;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.KeyEvent;
import android.provider.ContactsContract;
import android.os.Bundle;
import android.util.Log;

/**
* This is an example implementation of the {@link android.app.Instrumentation}
* class, allowing you to run tests against application code.  The
//Synthetic comment -- @@ -61,7 +58,7 @@

// Monitor for the expected start activity call.
ActivityMonitor am = addMonitor(IntentFilter.create(
            Intent.ACTION_VIEW, ContactsContract.Contacts.CONTENT_ITEM_TYPE), null, true);

// We are going to enqueue a couple key events to simulate the user
// selecting an item in the list.







