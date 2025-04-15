/*No need to reset msg after sent a forward msg

The forwarded conversation will transfor the content as draft
to the conversation which has the same recipient and no drafts

Change-Id:Ia8b8cebf3832e6524bda487231d928b4c8caa628Signed-off-by: Roger Chen <cxr514033970@gmail.com>*/




//Synthetic comment -- diff --git a/src/com/android/mms/ui/ComposeMessageActivity.java b/src/com/android/mms/ui/ComposeMessageActivity.java
//Synthetic comment -- index 42bda28..bd0db82 100644

//Synthetic comment -- @@ -2537,7 +2537,12 @@

@Override
public void onPreMessageSent() {
        if (mExitOnSent) {
            // But bail out if we are supposed to exit after the message is sent.
            finish();
        } else {
            runOnUiThread(mResetMessageRunnable);
        }
}

@Override
//Synthetic comment -- @@ -3730,10 +3735,6 @@

mScrollOnSend = true;   // in the next onQueryComplete, scroll the list to the end.
}
}

private void resetMessage() {







