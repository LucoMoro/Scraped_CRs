/*Fix "The forwarded msg cannot be discarded"

The forwarded msg is different from the msg which
sent from 3rd-party, it is a draft with the content
"Mms.Draft.CONTENT_URI", but the draft state wasn't
set anywhere. So we should force to set the state
after the conversation of workingmessage was set.
Otherwise, it cannot be discarded since mHasMmsDraft
was false.

Change-Id:I52e023da61b29701a2c3147812e82c3be159cefeSigned-off-by: Roger Chen <cxr514033970@gmail.com>*/




//Synthetic comment -- diff --git a/src/com/android/mms/ui/ComposeMessageActivity.java b/src/com/android/mms/ui/ComposeMessageActivity.java
//Synthetic comment -- index cb5b634..8a3f623 100644

//Synthetic comment -- @@ -1802,12 +1802,12 @@

// Load the draft for this thread, if we aren't already handling
// existing data, such as a shared picture or forwarded message.
        boolean isForwardedMessage = handleForwardedMessage();
// We don't attempt to handle the Intent.ACTION_SEND when saveInstanceState is non-null.
// saveInstanceState is non-null when this activity is killed. In that case, we already
// handled the attachment or the send, so we don't try and parse the intent again.
boolean intentHandled = savedInstanceState == null &&
            (handleSendIntent() || isForwardedMessage);
if (!intentHandled) {
loadDraft();
}
//Synthetic comment -- @@ -1815,6 +1815,13 @@
// Let the working message know what conversation it belongs to
mWorkingMessage.setConversation(mConversation);

        // Actually, the forwarded Mms is already a draft with the content
        // Mms.Draft.CONTENT_URI, but the draft state wasn't set. So force
        // to sync the state after set the conversation.
        if (isForwardedMessage && mWorkingMessage.requiresMms()) {
            mWorkingMessage.saveAsMms(true);
        }

// Show the recipients editor if we don't have a valid thread. Hide it otherwise.
if (mConversation.getThreadId() <= 0) {
// Hide the recipients editor so the call to initRecipientsEditor won't get







