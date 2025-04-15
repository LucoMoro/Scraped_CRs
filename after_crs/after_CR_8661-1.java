/*Extend ANDROID with CDMA mobile technology support . Feature Complete

This contribution is final functional step of the first release of the CDMA extension of the
Android telephony layers.
It contains changes in the phone related applications, the application
framework telephony packages and in the RIL daemon library space.
The implementation of the CDMA support requires architectural changes in the
telephony package and extensions of the RIL interface.
The application interface (SDK interface) will be extended to provide
CDMA specific features/information to the phone related application and other
applications.
Where ever possible the actual used radio technology is transparent for the
application using mobile connections.

This increment is tested on the Android emulator with a RIL simulator tool and
also tested on a reference HW platform.
The CDMA extension of the telephony stack can be used for Android phones
supporting either CDMA mobile technology only
or world mode including GSM/WCDMA and CDMA.
The following CDMA technologies are considered: IS-95, CDMA2000 1xRTT, CDMA2000
1x EVDO.

This contribution implements the following functionality:

- start up,
- access the CDMA subscription and other information from memory of from the
  card (either SIM, USIM or RUIM),
- register to the network,
- provides registration status to the application for displaying
- be able to handle incoming and outgoing voice calls,
- provide phone and call settings in the settings application
- provide supplementary services in the settings application
- provide supplementary services by in-call menues
- handles TTY and enhance voice privacy
- supports automatic radio technology change for a world mode phone from
  CDMA to GSM/UMTS or vice versa
- send and receive SMS
- configure and receive Broadcast SMS
- receive WAP Push SMS

Signed-off by : Saverio Labella,    saverio.labella@teleca.com
                Sigmar Lingner,     sigmar.lingner@teleca.com*/




//Synthetic comment -- diff --git a/src/com/android/mms/transaction/MessageStatusReceiver.java b/src/com/android/mms/transaction/MessageStatusReceiver.java
//Synthetic comment -- index d51a92b..73b4e16 100644

//Synthetic comment -- @@ -17,8 +17,6 @@

package com.android.mms.transaction;

import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.ContentValues;
//Synthetic comment -- @@ -27,9 +25,11 @@
import android.database.Cursor;
import android.net.Uri;
import android.provider.Telephony.Sms;
import android.telephony.SmsMessage;
import android.util.Log;

import com.google.android.mms.util.SqliteWrapper;

public class MessageStatusReceiver extends BroadcastReceiver {
public static final String MESSAGE_STATUS_RECEIVED_ACTION =
"com.android.mms.transaction.MessageStatusReceiver.MESSAGE_STATUS_RECEIVED";








//Synthetic comment -- diff --git a/src/com/android/mms/transaction/SmsMessageSender.java b/src/com/android/mms/transaction/SmsMessageSender.java
//Synthetic comment -- index 49e2b60..0db65ab 100644

//Synthetic comment -- @@ -33,7 +33,7 @@
import android.provider.Telephony.Sms;
import android.provider.Telephony.Threads;
import android.provider.Telephony.Sms.Conversations;
import android.telephony.SmsManager;

import java.util.ArrayList;
import java.util.Arrays;








//Synthetic comment -- diff --git a/src/com/android/mms/transaction/SmsReceiverService.java b/src/com/android/mms/transaction/SmsReceiverService.java
//Synthetic comment -- index 39c44b2..2f762e2 100644

//Synthetic comment -- @@ -20,13 +20,6 @@
import static android.content.Intent.ACTION_BOOT_COMPLETED;
import static android.provider.Telephony.Sms.Intents.SMS_RECEIVED_ACTION;

import android.app.Activity;
import android.app.Service;
import android.content.ContentResolver;
//Synthetic comment -- @@ -47,12 +40,19 @@
import android.provider.Telephony.Sms.Intents;
import android.provider.Telephony.Sms.Outbox;
import android.telephony.ServiceState;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Config;
import android.util.Log;
import android.widget.Toast;

import com.android.internal.telephony.TelephonyIntents;
import com.android.mms.R;
import com.android.mms.ui.ClassZeroActivity;
import com.android.mms.util.SendingProgressTokenManager;
import com.google.android.mms.MmsException;
import com.google.android.mms.util.SqliteWrapper;

/**
* This service essentially plays the role of a "worker thread", allowing us to store
* incoming messages to the database, update notifications, etc. without blocking the








//Synthetic comment -- diff --git a/src/com/android/mms/ui/ComposeMessageActivity.java b/src/com/android/mms/ui/ComposeMessageActivity.java
//Synthetic comment -- index b4a0070..5d9d13d 100644

//Synthetic comment -- @@ -26,7 +26,7 @@
import static com.android.mms.ui.MessageListAdapter.COLUMN_MSG_TYPE;
import static com.android.mms.ui.MessageListAdapter.PROJECTION;

import com.android.internal.telephony.GsmAlphabet;
import com.android.mms.ExceedMessageSizeException;
import com.android.mms.R;
import com.android.mms.ResolutionException;
//Synthetic comment -- @@ -82,7 +82,7 @@
import android.provider.Telephony.Mms;
import android.provider.Telephony.Sms;
import android.provider.Telephony.Threads;
import android.telephony.SmsMessage;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
//Synthetic comment -- @@ -396,7 +396,7 @@
// in the size of the header to determine how many will fit.
mMsgCount = mSeptets / (SmsMessage.MAX_USER_DATA_SEPTETS_WITH_HEADER) + 1;
remainingInCurrentMessage = SmsMessage.MAX_USER_DATA_SEPTETS_WITH_HEADER
                        - (mSeptets % SmsMessage.MAX_USER_DATA_SEPTETS_WITH_HEADER);
} else {
mMsgCount = 1;
remainingInCurrentMessage = SmsMessage.MAX_USER_DATA_SEPTETS - mSeptets;
//Synthetic comment -- @@ -1387,9 +1387,11 @@
}

private void hideTopPanelIfNecessary() {
        if ( (((mSubjectTextEditor != null) &&
                (mSubjectTextEditor.getVisibility() != View.VISIBLE)) ||
(mSubjectTextEditor == null)) &&
                (((mRecipientsEditor != null) &&
                (mRecipientsEditor.getVisibility() != View.VISIBLE)) ||
(mRecipientsEditor == null))) {
mTopPanel.setVisibility(View.GONE);
}
//Synthetic comment -- @@ -1816,7 +1818,8 @@
android.R.drawable.ic_menu_edit);
}

        if ((mAttachmentEditor == null) ||
                (mAttachmentEditor.getAttachmentType() == AttachmentEditor.TEXT_ONLY)) {
menu.add(0, MENU_ADD_ATTACHMENT, 0, R.string.add_attachment).setIcon(
R.drawable.ic_menu_attachment);
}








//Synthetic comment -- diff --git a/src/com/android/mms/ui/ManageSimMessages.java b/src/com/android/mms/ui/ManageSimMessages.java
//Synthetic comment -- index 372ded6..faf88b9 100644

//Synthetic comment -- @@ -22,10 +22,8 @@
import com.android.mms.transaction.MessagingNotification;

import android.app.Activity;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
//Synthetic comment -- @@ -35,7 +33,7 @@
import android.os.Handler;
import android.provider.Telephony.Sms;
import android.provider.Telephony.Threads;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
//Synthetic comment -- @@ -47,11 +45,11 @@
import android.widget.TextView;

/**
 * Displays a list of the SMS messages stored on the ICC.
*/
public class ManageSimMessages extends Activity
implements View.OnCreateContextMenuListener {
    private static final Uri ICC_URI = Uri.parse("content://sms/icc");
private static final String TAG = "ManageSimMessages";
private static final int MENU_COPY_TO_PHONE_MEMORY = 0;
private static final int MENU_DELETE_FROM_SIM = 1;
//Synthetic comment -- @@ -146,7 +144,7 @@

private void startQuery() {
try {
            mQueryHandler.startQuery(0, null, ICC_URI, null, null, null, null);
} catch (SQLiteException e) {
SqliteWrapper.checkSQLiteException(this, e);
}
//Synthetic comment -- @@ -214,7 +212,7 @@

private void registerSimChangeObserver() {
mContentResolver.registerContentObserver(
                ICC_URI, true, simChangeObserver);
}

private void copyToPhoneMemory(Cursor cursor) {
//Synthetic comment -- @@ -238,14 +236,14 @@
int messageStatus = cursor.getInt(
cursor.getColumnIndexOrThrow("status"));

        return (messageStatus == SmsManager.STATUS_ON_ICC_READ) ||
               (messageStatus == SmsManager.STATUS_ON_ICC_UNREAD);
}

private void deleteFromSim(Cursor cursor) {
String messageIndexString =
                cursor.getString(cursor.getColumnIndexOrThrow("index_on_icc"));
        Uri simUri = ICC_URI.buildUpon().appendPath(messageIndexString).build();

SqliteWrapper.delete(this, mContentResolver, simUri, null, null);
}







