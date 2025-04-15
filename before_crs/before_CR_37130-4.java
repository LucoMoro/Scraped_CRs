/*Make the liftcycle of cursor under the Activity

The cursors of ComposeCardActivity and ConversationList
were never be closed. So the database will throw an
CursorWindowAllocationException if the cursors which not
be closed are too many to be allocated then the database
will return a NULL cursor back to onQueryComplete().
After that Mms will be crashed by the NullPointerException.

Change-Id:Ib8730b326edf9177bbcce48a42b85405acff51baSigned-off-by: Roger Chen <cxr514033970@gmail.com>*/
//Synthetic comment -- diff --git a/src/com/android/mms/ui/ComposeMessageActivity.java b/src/com/android/mms/ui/ComposeMessageActivity.java
//Synthetic comment -- index 4fa1878..308ff11 100644

//Synthetic comment -- @@ -2090,6 +2090,13 @@
mConversation.blockMarkAsRead(false);

if (mMsgListAdapter != null) {
mMsgListAdapter.changeCursor(null);
}









//Synthetic comment -- diff --git a/src/com/android/mms/ui/ConversationList.java b/src/com/android/mms/ui/ConversationList.java
//Synthetic comment -- index abd0517..1039d67 100644

//Synthetic comment -- @@ -281,6 +281,13 @@
// multi-select mode (if we're in it) and remove all the selections.
getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

mListAdapter.changeCursor(null);
}








