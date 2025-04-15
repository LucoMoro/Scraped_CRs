/*Fix an NullPointerException when finished

No need to continue the querying when it was finished.
Otherwise it will be thrown an NullPointerException,
since RecipientEditor need to be initialized after
the querying completed.

Change-Id:Ie96e8c690b2227971bcd10cc4dab72eb0ec3bacfSigned-off-by: Roger Chen <cxr514033970@gmail.com>*/
//Synthetic comment -- diff --git a/src/com/android/mms/ui/ComposeMessageActivity.java b/src/com/android/mms/ui/ComposeMessageActivity.java
//Synthetic comment -- index 085822f..104d68a 100644

//Synthetic comment -- @@ -2083,6 +2083,9 @@
protected void onStop() {
super.onStop();

// Allow any blocked calls to update the thread's read status.
mConversation.blockMarkAsRead(false);








