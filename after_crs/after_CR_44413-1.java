/*No need to reset msg after sent a forward msg

The forwarded conversation will transfor the content as draft
to the conversation which has the same recipient and no drafts

Change-Id:Ia8b8cebf3832e6524bda487231d928b4c8caa628Signed-off-by: Roger Chen <cxr514033970@gmail.com>*/




//Synthetic comment -- diff --git a/src/com/android/mms/ui/ComposeMessageActivity.java b/src/com/android/mms/ui/ComposeMessageActivity.java
//Synthetic comment -- index 48be2a9..cf99aa1 100644

//Synthetic comment -- @@ -2433,7 +2433,12 @@

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
//Synthetic comment -- @@ -3564,10 +3569,6 @@

mScrollOnSend = true;   // in the next onQueryComplete, scroll the list to the end.
}
}

private void resetMessage() {







