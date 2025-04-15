/*Block forwarded CMA until messages were sent.

The previous CMA will load the sending message as draft in onRestart()
after forwarded CMA finished itself in sending messages due to Mms Uri
hasn't been moved to OUTBOX yet.

Change-Id:I9de04b4ceb287cc1ecfe3f7327ddc38c09c76278Signed-off-by: Roger Chen <cxr514033970@gmail.com>*/
//Synthetic comment -- diff --git a/src/com/android/mms/data/WorkingMessage.java b/src/com/android/mms/data/WorkingMessage.java
//Synthetic comment -- index 250a63a..60a0a86 100755

//Synthetic comment -- @@ -1398,7 +1398,6 @@
mmsUri = SqliteWrapper.insert(mActivity, mContentResolver, Mms.Outbox.CONTENT_URI,
values);
}
            mStatusListener.onMessageSent();

// If user tries to send the message, it's a signal the inputted text is
// what they wanted.
//Synthetic comment -- @@ -1472,6 +1471,8 @@
} catch (Exception e) {
Log.e(TAG, "Failed to send message: " + mmsUri + ", threadId=" + threadId, e);
}
MmsWidgetProvider.notifyDatasetChanged(mActivity);
}









//Synthetic comment -- diff --git a/src/com/android/mms/ui/ComposeMessageActivity.java b/src/com/android/mms/ui/ComposeMessageActivity.java
//Synthetic comment -- index 42bda28..bcf361e 100644

//Synthetic comment -- @@ -2542,6 +2542,12 @@

@Override
public void onMessageSent() {
// This callback can come in on any thread; put it on the main thread to avoid
// concurrency problems
runOnUiThread(new Runnable() {
//Synthetic comment -- @@ -3730,10 +3736,6 @@

mScrollOnSend = true;   // in the next onQueryComplete, scroll the list to the end.
}
        // But bail out if we are supposed to exit after the message is sent.
        if (mExitOnSent) {
            finish();
        }
}

private void resetMessage() {







