/*Updated deprecated Contacts API to ContactsContract

Change-Id:I1d51b5eba579e841d6024eec08670ae8ff595f94*/
//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/app/AlertDialogSamples.java b/samples/ApiDemos/src/com/example/android/apis/app/AlertDialogSamples.java
//Synthetic comment -- index de5f711..6714175 100644

//Synthetic comment -- @@ -30,7 +30,7 @@
import android.widget.Button;
import android.widget.Toast;
import android.database.Cursor;
import android.provider.Contacts;

import com.example.android.apis.R;

//Synthetic comment -- @@ -186,13 +186,15 @@
/* User clicked on a check box do some stuff */
}
})
                .setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
public void onClick(DialogInterface dialog, int whichButton) {

/* User clicked Yes so do some stuff */
}
})
                .setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {
public void onClick(DialogInterface dialog, int whichButton) {

/* User clicked No so do some stuff */
//Synthetic comment -- @@ -201,17 +203,18 @@
.create();
case DIALOG_MULTIPLE_CHOICE_CURSOR:
String[] projection = new String[] {
                        Contacts.People._ID,
                        Contacts.People.NAME,
                        Contacts.People.SEND_TO_VOICEMAIL
};
                Cursor cursor = managedQuery(Contacts.People.CONTENT_URI, projection, null, null, null);
return new AlertDialog.Builder(AlertDialogSamples.this)
.setIcon(R.drawable.ic_popup_reminder)
.setTitle(R.string.alert_dialog_multi_choice_cursor)
.setMultiChoiceItems(cursor,
                            Contacts.People.SEND_TO_VOICEMAIL,
                            Contacts.People.NAME,
new DialogInterface.OnMultiChoiceClickListener() {
public void onClick(DialogInterface dialog, int whichButton,
boolean isChecked) {








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/app/ContactsSelectInstrumentation.java b/samples/ApiDemos/src/com/example/android/apis/app/ContactsSelectInstrumentation.java
//Synthetic comment -- index dcb8b83..e34f4cf 100644

//Synthetic comment -- @@ -20,14 +20,11 @@
import android.app.Instrumentation;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.view.KeyEvent;
import android.provider.Contacts;
import android.os.Bundle;
import android.util.Log;

import java.util.Map;

/**
* This is an example implementation of the {@link android.app.Instrumentation}
* class, allowing you to run tests against application code.  The
//Synthetic comment -- @@ -61,7 +58,7 @@

// Monitor for the expected start activity call.
ActivityMonitor am = addMonitor(IntentFilter.create(
            Intent.ACTION_VIEW, Contacts.People.CONTENT_ITEM_TYPE), null, true);

// We are going to enqueue a couple key events to simulate the user
// selecting an item in the list.







