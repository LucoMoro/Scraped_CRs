/*Fix memory leaks and corner cases in MMS app

The following problems will be fixed with these changes
1) IndexOutOfBounds exception when viewing a slide show. mCurrentElement should
   not exceed the size of all entries. This caused IndexOutOfBounds exception
2) Redundant call for registerSimChangeObserver causes OOM issues.
   SimChangeObserver is registered in onResume function. So the call in
   onQueryComplete can be removed.
3) Check for empty sender address before trying to get associated contact number
   when an SMS is received
4) Fix Cursor leak while receiving sms*/
//Synthetic comment -- diff --git a/src/com/android/mms/dom/smil/SmilPlayer.java b/src/com/android/mms/dom/smil/SmilPlayer.java
//Synthetic comment -- index 0fc7b07..356c6a0 100644

//Synthetic comment -- @@ -510,7 +510,12 @@
}

private synchronized TimelineEntry reloadCurrentEntry() {
        return mAllEntries.get(mCurrentElement);
}

private synchronized void actionPause() {
//Synthetic comment -- @@ -567,6 +572,8 @@
if (isReloadAction()) {
actionReload();
entry = reloadCurrentEntry();
if (isPausedState()) {
mAction = SmilPlayerAction.PAUSE;
}








//Synthetic comment -- diff --git a/src/com/android/mms/transaction/SmsReceiverService.java b/src/com/android/mms/transaction/SmsReceiverService.java
old mode 100644
new mode 100755
//Synthetic comment -- index f3e094d..d2097b0

//Synthetic comment -- @@ -52,6 +52,7 @@
import android.telephony.ServiceState;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

//Synthetic comment -- @@ -411,9 +412,14 @@
// excess messages.
Long threadId = values.getAsLong(Sms.THREAD_ID);
String address = values.getAsString(Sms.ADDRESS);
        Contact cacheContact = Contact.get(address,true);
        if (cacheContact != null) {
            address = cacheContact.getNumber();
}

if (((threadId == null) || (threadId == 0)) && (address != null)) {








//Synthetic comment -- diff --git a/src/com/android/mms/ui/ManageSimMessages.java b/src/com/android/mms/ui/ManageSimMessages.java
//Synthetic comment -- index 077e2d6..fa41291 100644

//Synthetic comment -- @@ -139,7 +139,6 @@
updateState(SHOW_LIST);
}
startManagingCursor(mCursor);
                registerSimChangeObserver();
} else {
// Let user know the SIM is empty
updateState(SHOW_EMPTY);








//Synthetic comment -- diff --git a/src/com/android/mms/util/Recycler.java b/src/com/android/mms/util/Recycler.java
//Synthetic comment -- index 7c503a1..15a15e6 100644

//Synthetic comment -- @@ -427,8 +427,10 @@
null, "date DESC");     // get in newest to oldest order

if (msgs.getCount() >= limit) {
return true;
}
}
} finally {
cursor.close();







