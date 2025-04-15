/*Focus is set to SMS messages list view when "Manage SIM messages" is selected

This change sets the focus to SMS list view after the SMS messages are
retrieved from SIM card.

Change-Id:I11b686c56aa9e181c5532095613f788d737fb87d*/
//Synthetic comment -- diff --git a/src/com/android/mms/ui/ManageSimMessages.java b/src/com/android/mms/ui/ManageSimMessages.java
//Synthetic comment -- index 1c8792c..ac2ceea 100644

//Synthetic comment -- @@ -329,6 +329,7 @@
mMessage.setVisibility(View.GONE);
setTitle(getString(R.string.sim_manage_messages_title));
setProgressBarIndeterminateVisibility(false);
break;
case SHOW_EMPTY:
mSimList.setVisibility(View.GONE);







